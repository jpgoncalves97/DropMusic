package sd;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class MulticastServer extends Thread {

    protected static String MULTICAST_ADDRESS = "224.0.224.0";
    protected static int PORT = 4321;
    protected static int TCP_PORT = 1234;
    protected static InetAddress group;
    protected String id;
    private ArrayList<File> musicas;
    private String musicFilePath;

    public MulticastServer() {
        super();
        //id = System.currentTimeMillis();
        id = Long.toString(ThreadLocalRandom.current().nextInt(0, 99));
        System.out.println("Server id# " + id);
        /*musicFilePath = "C:/Users/j/Desktop/musica_server";
        File[] temp = new File(musicFilePath).listFiles();
        System.out.println("Musicas");
        if (temp == null){
            musicas = new ArrayList<>();
        } else {
            musicas = new ArrayList<>(Arrays.asList(temp));
            listMusic();
        }
        TCPHandler(TCP_PORT);*/
        try {
            group = InetAddress.getByName(MULTICAST_ADDRESS);
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        MulticastSocket socket = newMulticastSocket();
        SharedMessage msg = new SharedMessage();
        newReceiverThread(socket, msg);
        newSenderThread(socket, msg);
    }

    public static void main(String[] args) throws Exception {
        new MulticastServer();
    }

    public void TCPHandler(int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(port);
                } catch (IOException e) {
                    System.out.println("Failed to open TCP Server socket\n" + e);
                    return;
                }
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        TCPThread(clientSocket);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        }).start();
    }

    public void listMusic() {
        for (File file : musicas) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }

    public void TCPThread(Socket clientSocket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = clientSocket.getInputStream();
                    OutputStream out = clientSocket.getOutputStream();
                    // Upload
                    int r = in.read();
                    System.out.println(r);
                    if (r == 0) {
                        byte[] music = new byte[1024];
                        int read = in.read(music);
                        String nome_musica = new String(music, 0, read);
                        TCP.downloadFile(musicFilePath, musicas, nome_musica, in);
                    }
                    // Download
                    else {
                        byte[] music = new byte[1024];
                        int i;
                        while (true) {
                            int read = in.read(music);
                            String nome_musica = new String(music, 0, read);
                            System.out.println("Searching for " + nome_musica);
                            for (i = 0; i < musicas.size(); i++) {
                                if (musicas.get(i).getName().equals(nome_musica)) {
                                    System.out.println("Ficheiro encontrado");
                                    out.write(1);
                                    break;
                                }
                            }
                            if (i == musicas.size()) {
                                System.out.println("Ficheiro não encontrado");
                                out.write(0);
                            } else {
                                break;
                            }
                        }
                        System.out.println("Sending " + musicas.get(i).length() + " bytes");
                        // Writing the file to disk
                        // Instantiating a new out stream object
                        TCP.uploadFile(musicas.get(i), out);

                    }
                    clientSocket.close();

                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }).start();
    }

    public static MulticastSocket newMulticastSocket() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
        } catch (IOException e) {

        }
        return socket;
    }

    public String decodeMessage(String[] msg) {
        if (msg[0].equals(id) || msg[0].equals("0")) {
            if (msg[1].equals("request")) {
                switch (msg[2]) {
                    case "server_id":
                        return "response;server_id;" + id;
                    case "echo":
                        return "response;echo;" + msg[3];
                    default:
                        return "ign";
                }
            } else {
                return "ign";
            }
        }
        return "ign";
    }

    public static void sendString(MulticastSocket socket, String msg) throws IOException {
        byte[] buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MulticastServer.PORT);
        socket.send(packet);
    }

    public static String receiveString(MulticastSocket socket) throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packetToString(packet);
    }

    public static String packetToString(DatagramPacket d) {
        return new String(d.getData(), d.getOffset(), d.getLength());
    }

    public void newReceiverThread(MulticastSocket socket, SharedMessage msg) {
        new MulticastReceiverThread(socket, msg, new Runnable() {
            public void run() {
                while (true) {
                    try {
                        String request;
                        // Ignore response packets
                        do {
                            request = receiveString(socket);
                        } while (request.contains("response"));
                        String response = decodeMessage(request.split(";"));
                        if (!response.equals("ign")) {
                            System.out.println("Received: " + request);
                            synchronized (msg) {
                                msg.setMsg(response);
                                msg.notify();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void newSenderThread(MulticastSocket socket, SharedMessage msg) {
        new MulticastSenderThread(socket, msg, new Runnable() {
            public void run() {

                while (true) {
                    String m;
                    try {
                        byte[] buffer;
                        synchronized (msg) {
                            if (msg.isNull()) {
                                try {
                                    msg.wait();
                                } catch (InterruptedException e) {
                                    System.out.println("Thread interrompida: " + e);
                                }
                            }
                            m = id + ";" + msg.getMsg();
                            buffer = m.getBytes();
                        }
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MulticastServer.PORT);
                        socket.send(packet);
                        System.out.println("Sending response: " + m);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}

class MulticastReceiverThread extends Thread {

    private MulticastSocket socket;
    private SharedMessage msg;

    MulticastReceiverThread(MulticastSocket socket, SharedMessage msg, Runnable runnable) {
        super(runnable);
        this.msg = msg;
        this.socket = socket;
    }

}

class MulticastSenderThread extends Thread {

    private MulticastSocket socket;
    private SharedMessage msg;

    MulticastSenderThread(MulticastSocket socket, SharedMessage msg, Runnable runnable) {
        super(runnable);
        this.msg = msg;
        this.socket = socket;
    }

}

class SharedMessage {

    private ArrayList<String> msgList;

    SharedMessage() {
        msgList = new ArrayList<>();
    }

    public void print() {
        System.out.println("Message list");
        for (int i = 0; i < msgList.size(); i++) {
            System.out.println(msgList.get(i));
        }
    }

    public void setMsg(String msg) {
        msgList.add(msg);
    }

    public boolean isNull() {
        return msgList.isEmpty();
    }

    public String getMsg() {

        if (msgList.isEmpty()) return null;
        else {
            String s = msgList.get(0);
            msgList.remove(0);
            return s;
        }
    }
}
