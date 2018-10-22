package sd;

import Classes.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class MulticastServer extends Thread implements Serializable {

    private final static String MULTICAST_ADDRESS = "224.0.224.0";
    private final static int PORT = 4321;
    protected static int TCP_PORT = 1234;
    private static InetAddress group;
    private final String id;//Long.toString(System.currentTimeMillis());
    private final String mainPath = "C:/Users/j/Desktop/multicast/";
    private final String userPath = mainPath + "users";
    private final String albumPath = mainPath + "albuns";
    private final String authorPath = mainPath + "authors";
    private final String musicPath = mainPath + "music";
    private final String musicFilePath = mainPath + "musica";
    private HashMap<String, user> users;
    private ArrayList<author> authors;
    private ArrayList<album> albums;
    private ArrayList<String> notificacoes;
    private ArrayList<music> musicas;
    private ArrayList<File> fileMusicas;

    public MulticastServer() {
        super();
        id = Long.toString(ThreadLocalRandom.current().nextInt(0, 99));
        System.out.println("Server id# " + id);

        // TCPHandler(TCP_PORT);
        try {
            group = InetAddress.getByName(MULTICAST_ADDRESS);
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        MulticastSocket socket = newMulticastSocket();
        SharedMessage msg = new SharedMessage();
        newReceiverThread(socket, msg);
        newSenderThread(socket, msg);

        File[] temp = new File(musicFilePath).listFiles();
        System.out.println("Musicas");
        if (temp == null) {
            fileMusicas = new ArrayList<>();
        } else {
            fileMusicas = new ArrayList<>(Arrays.asList(temp));
            listMusic();
        }
        musicas = new ArrayList<>((ArrayList) readFromFile(musicPath));
        users = new HashMap<>(100);
        authors = new ArrayList<>(Arrays.asList((author[]) readFromFile(authorPath)));
        albums = new ArrayList<>(Arrays.asList((album[]) readFromFile(albumPath)));
        notificacoes = new ArrayList<>();
        readUserFile();
        for (music m : musicas) {
            System.out.println(m.toString());
        }

        /*author[] a = {new author("Eminem"), new author("Drake"), new author("Michael Jackson"),
                new author("Angus Young"), new author("Brian Johnson"), new author("Bon Scott"), new author("Axl Rose"),
                new author("Roger Waters"), new author("David Gilmour"), new author("Syd Barrett"),
                new author("Richard Wright"), new author("Nick Mason"), new author("Bob Klose"),
                new author("Damon Albarn"), new author("Jamie Hewlett"), new author("Paul Simonon")};
        band[] b = {new band("AC/DC"), new band("Pink Floyd"), new band("Gorillaz")};
        b[0].addAuthor(a[3]);
        b[0].addAuthor(a[4]);
        b[0].addAuthor(a[5]);
        b[0].addAuthor(a[6]);
        b[1].addAuthor(a[7]);
        b[1].addAuthor(a[8]);
        b[1].addAuthor(a[9]);
        b[1].addAuthor(a[10]);
        b[1].addAuthor(a[11]);
        b[1].addAuthor(a[12]);
        b[2].addAuthor(a[13]);
        b[2].addAuthor(a[14]);
        b[2].addAuthor(a[15]);
        album[] al = {new album("Back in Black", "Rock", new Timestamp(1980, 7, 25, 0, 0, 0,0)),
                new album("The Dark Side of The Moon", "Rock", new Timestamp(1973, 3, 1, 0, 0,0 ,0)),
                new album("Kamikaze", "Hip-Hop", new Timestamp(2018, 8, 31, 0, 0,0 ,0)),
                new album("Scorpion", "Hip-Hop", new Timestamp(2018, 6, 29, 0, 0,0, 0)),
                new album("Thriller", "Pop", new Timestamp(1982, 11, 30, 0, 0,0,0)),
                new album("Demon days", "Pop", new Timestamp(2015, 5, 11 , 0,0,0,0))};
        //String nome, band b, boolean publico, album album, String lyrics, Timestamp timelenght)
        musicas.add(new music("Any Colour You Like", b[1], true, al[1], ""));
        musicas.add(new music("Back In Black", b[0], true, al[0], ""));
        musicas.add(new music("Beat it", a[2], true, al[4], ""));
        musicas.add(new music("Billie Jean", a[2], true, al[4], ""));
        musicas.add(new music("Dare", b[2], true, al[5], ""));
        musicas.add(new music("Dirty Harry", b[2], true, al[5], ""));
        musicas.add(new music("Fall", a[0], true, al[2], ""));
        musicas.add(new music("Gods Plan", a[1], true, al[3], ""));
        musicas.add(new music("Have a drink on me", b[0], true, al[0], ""));
        musicas.add(new music("In My Feelings", a[1], true, al[3], ""));
        musicas.add(new music("Kamikaze", a[0], true, al[2], ""));
        musicas.add(new music("Kids with guns", b[2], true, al[5], ""));
        musicas.add(new music("Lucky You", a[0], true, al[2], ""));
        musicas.add(new music("Money", b[1], true, al[1], ""));
        musicas.add(new music("Thriller", a[2], true, al[4], ""));
        musicas.add(new music("Us and Them", b[1], true, al[1], ""));
        musicas.add(new music("You shook me all night long", b[0], true, al[0], ""));
        al[0].addMusicas(musicas.get(1));
        al[0].addMusicas(musicas.get(8));
        al[0].addMusicas(musicas.get(16));
        al[1].addMusicas(musicas.get(0));
        al[1].addMusicas(musicas.get(13));
        al[1].addMusicas(musicas.get(15));
        al[2].addMusicas(musicas.get(6));
        al[2].addMusicas(musicas.get(10));
        al[2].addMusicas(musicas.get(12));
        al[3].addMusicas(musicas.get(7));
        al[3].addMusicas(musicas.get(9));
        al[4].addMusicas(musicas.get(2));
        al[4].addMusicas(musicas.get(3));
        al[4].addMusicas(musicas.get(14));
        al[5].addMusicas(musicas.get(4));
        al[5].addMusicas(musicas.get(5));
        al[5].addMusicas(musicas.get(11));
        b[0].addAlbum(al[0]);
        b[1].addAlbum(al[1]);
        b[2].addAlbum(al[5]);
        a[0].addAlbum(al[2]);
        a[1].addAlbum(al[3]);
        a[2].addAlbum(al[4]);
        writeToFile(albumPath, al);
        writeToFile(musicPath, musicas);
        writeToFile(authorPath, a);
        writeToFile(mainPath + "bands", b);*/
    }

    public static void main(String[] args) {
        new MulticastServer();
    }

    public void readUserFile() {
        File f = new File(userPath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println(e);
            }
            registerUser(new user(true, 0, "admin", "admin", "admin", 0, "", 0));
            return;
        }
        users = (HashMap<String, user>) readFromFile(userPath);
    }

    public Object readFromFile(String mainPath) {
        Object o;
        try {
            FileInputStream fis = new FileInputStream(new File(mainPath));
            ObjectInputStream ois = new ObjectInputStream(fis);
            o = ois.readObject();
            fis.close();
            ois.close();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return o;
    }

    public void writeToFile(String mainPath, Object o) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(mainPath));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            fos.close();
            oos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean registerUser(user u) {
        // Check if user pode ser registado(parametros validos)
        if (users.containsKey(u.getUsername())) {
            System.out.println("Username " + u.getUsername() + " já existe");
            return false;
        }
        users.put(u.getUsername(), u);
        writeToFile(userPath, users);
        return true;
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
        for (File file : fileMusicas) {
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
                    if (r == 0) {
                        System.out.println("Downloading file");
                        byte[] music = new byte[1024];
                        int read = in.read(music);
                        String nome_musica = new String(music, 0, read);
                        TCP.downloadFile(musicFilePath, fileMusicas, nome_musica, in);
                    }
                    // Download
                    else {
                        System.out.println("Uploading file");
                        byte[] music = new byte[1024];
                        int i;
                        while (true) {
                            int read = in.read(music);
                            String nome_musica = new String(music, 0, read);
                            System.out.println("Searching for " + nome_musica);
                            for (i = 0; i < fileMusicas.size(); i++) {
                                if (fileMusicas.get(i).getName().equals(nome_musica)) {
                                    System.out.println("Ficheiro encontrado");
                                    out.write(1);
                                    break;
                                }
                            }
                            if (i == fileMusicas.size()) {
                                System.out.println("Ficheiro não encontrado");
                                out.write(0);
                            } else {
                                break;
                            }
                        }
                        System.out.println("Sending " + fileMusicas.get(i).length() + " bytes");
                        // Writing the file to disk
                        // Instantiating a new out stream object
                        TCP.uploadFile(fileMusicas.get(i), out);

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
                    case "register":
                        //user(boolean editor, int idade, String username, String password, String nome, int phone_num, String address, int num_cc)
                        user u = new user(false, Integer.parseInt(msg[3]), msg[4], msg[5], msg[6], Integer.parseInt(msg[7]), msg[8], Integer.parseInt(msg[9]));
                        return registerUser(u) ? "response;register;true" : "response;register;false";
                    case "login":
                        System.out.println("Login request");
                        user u1 = users.get(msg[3]);
                        if (u1 == null || !u1.getPassword().equals(msg[4])) {
                            return "response;login;false;false";
                        }
                        return "response;login;true;" + (u1.isEditor() ? "true" : "false");
                    case "edit":
                        switch (msg[3]) {
                            case "music":
                            case "author":
                            case "album":
                        }
                    case "music_search":
                        switch (msg[3]) {
                            case "artista": {
                                ArrayList<String> resposta = new ArrayList<>();
                                for (music m : musicas) {
                                    author a = m.getAuthor();
                                    if (a != null) {
                                        if (a.getNome().equals(msg[4])) {
                                            resposta.add(m.getNome());
                                        }
                                    } else {
                                        band b = m.getBand();
                                        for (author a1 : b.getAuthors()) {
                                            if (a1.getNome().equals(msg[4])) {
                                                resposta.add(m.getNome());
                                            }
                                        }
                                    }
                                }
                                String ret = "response;music_search;" + resposta.size() + ";";
                                for (String s : resposta) {
                                    ret += s + ";";
                                }
                                return ret;
                            }
                            case "genero": {
                                ArrayList<String> resposta = new ArrayList<>();
                                for (album a : albums) {
                                    if (a.getGenero().equals(msg[4])) {
                                        for (music m : a.getMusicas()) {
                                            resposta.add(m.getNome());
                                        }
                                    }
                                }
                                String ret = "response;music_search;" + resposta.size() + ";";
                                for (String s : resposta) {
                                    ret += s + ";";
                                }
                                return ret;
                            }
                            case "album": {
                                ArrayList<String> resposta = new ArrayList<>();
                                for (album a : albums) {
                                    if (a.getNome().equals(msg[4])) {
                                        for (music m : a.getMusicas()) {
                                            resposta.add(m.getNome());
                                        }
                                        String ret = "response;music_search;" + resposta.size() + ";";
                                        for (String s : resposta) {
                                            ret += s + ";";
                                        }
                                        return ret;
                                    }
                                }
                                String ret = "response;music_search;" + resposta.size() + ";";
                                for (String s : resposta) {
                                    ret += s + ";";
                                }
                                return ret;
                            }
                        }
                    case "details":
                        switch (msg[3]) {
                            case "album":
                                for (album a : albums) {
                                    if (a.getNome().equals(msg[4])) {
                                        return a.toString();
                                    }
                                }
                            case "artista":
                                for (author a : authors) {
                                    if (a.getNome().equals(msg[4])) {
                                        return a.toString();
                                    }
                                }
                            case "musica":
                                for (music m : musicas) {
                                    if (m.getNome().equals(msg[4])) {
                                        return m.toString();
                                    }
                                }
                        }
                    case "critic":
                        for (album a : albums) {
                            if (a.getNome().equals(msg[3])) {
                                a.addCritica(new critica(Integer.parseInt(msg[4]), msg[5]));
                                return "ign";
                            }
                        }
                    case "give_editor":
                        users.get(msg[3]).setEditor(true);
                        return "ign";
                    /*case "list_users":
                        for (String key : users.keySet()){
                            System.out.println(users.get(key).toString());
                        }
                        return "ign";*/


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
