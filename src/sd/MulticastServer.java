package sd;

import Classes.*;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;

import static java.net.NetworkInterface.getNetworkInterfaces;

class MulticastServer extends Thread {

    protected static String MULTICAST_ADDRESS = "224.0.224.0";
    protected static int PORT = 4321;
    protected static int TCP_PORT = 1234;
    protected long id;
    private ArrayList<File> musicas;

    public MulticastServer() {
        super();
        id = System.currentTimeMillis();
        System.out.println("Server id# " + id);
        File[] temp = new File("C:/Users/j/Desktop/musica").listFiles();
        System.out.println("Musicas");
        if (temp == null){
            musicas = new ArrayList<>();
        } else {
            musicas = new ArrayList<>(Arrays.asList(temp));
            for (File file : musicas) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            }
        }
        TCPHandler();
        /*MulticastSocket socket = newMulticastSocket();
        newReceiverThread(socket);
        newSenderThread(socket);*/
    }

    public void newFileSender(Socket socket) {

    }

    public static void main(String[] args) throws IOException {
        new MulticastServer();
    }

    public void TCPHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(TCP_PORT);
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

    public void TCPThread(Socket clientSocket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = clientSocket.getInputStream();
                    OutputStream output = clientSocket.getOutputStream();
                    // Upload
                    if (in.read() == 0){

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
                                    output.write(1);
                                    break;
                                }
                            }
                            if (i == musicas.size()) {
                                System.out.println("Ficheiro não encontrado");
                                output.write(0);
                            } else {
                                break;
                            }
                        }
                        System.out.println("Sending " + musicas.get(i).length() + " bytes");
                        // Writing the file to disk
                        // Instantiating a new output stream object

                        FileInputStream fin = new FileInputStream(musicas.get(i));
                        int bytesRead;
                        byte[] buffer = new byte[1024];
                        while ((bytesRead = fin.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                        // Closing the FileOutputStream handle
                        output.close();
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
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
        } catch (IOException e) {
            System.out.println("Error creating socket");
        }
        return socket;
    }

    public void newReceiverThread(MulticastSocket socket) {
        new MulticastReceiverThread(socket, new Runnable() {
            public void run() {
                while (true) {
                    try {
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String[] msg = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(";");
                        for (String s : msg) {
                            System.out.println(s);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void newSenderThread(MulticastSocket socket) {
        new MulticastSenderThread(socket, new Runnable() {
            public void run() {
                InetAddress group;
                try {
                    group = InetAddress.getByName(MulticastServer.MULTICAST_ADDRESS);
                    while (true) {
                        try {
                            byte[] buffer = new byte[1024];
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MulticastServer.PORT);
                            socket.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}

class MulticastReceiverThread extends Thread {

    private MulticastSocket socket;

    MulticastReceiverThread(MulticastSocket socket, Runnable runnable) {
        super(runnable);
        this.socket = socket;
    }

}

class MulticastSenderThread extends Thread {

    private MulticastSocket socket;

    MulticastSenderThread(MulticastSocket socket, Runnable runnable) {
        super(runnable);
        this.socket = socket;
    }

}

