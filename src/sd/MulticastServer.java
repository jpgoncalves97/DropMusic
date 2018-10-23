package sd;

import Classes.*;

import java.io.*;
import java.net.*;
import java.util.*;

class MulticastServer extends Thread implements Serializable {

    private final static String MULTICAST_ADDRESS = "224.0.224.0";
    private final static int PORT = 4321;
    protected static int TCP_PORT = 1234;
    private MulticastSocket socket;
    private final String id = Long.toString(System.currentTimeMillis());
    private final String mainPath = "C:/Users/j/Desktop/multicast/";
    private final String userPath = mainPath + "users";
    private final String albumPath = mainPath + "albuns";
    private final String authorPath = mainPath + "authors";
    private final String musicPath = mainPath + "music";
    private final String musicFilePath = mainPath + "musica";
    private HashMap<String, user> users;
    private ArrayList<author> authors;
    private ArrayList<album> albums;
    private ArrayList<notificacao> notificacoes;
    private ArrayList<music> musicas;
    private ArrayList<File> fileMusicas;
    private LinkedList<String> requests;

    public MulticastServer() {
        super();
        System.out.println("Server id# " + id);

        // TCPHandler(TCP_PORT);

        File[] temp = new File(musicFilePath).listFiles();
        System.out.println("Musicas");
        if (temp == null) {
            fileMusicas = new ArrayList<>();
        } else {
            fileMusicas = new ArrayList<>(Arrays.asList(temp));
            listMusic();
        }
        requests = new LinkedList<>();
        musicas = new ArrayList<>((ArrayList) readFromFile(musicPath));
        users = new HashMap<>(100);
        authors = new ArrayList<>(Arrays.asList((author[]) readFromFile(authorPath)));
        albums = new ArrayList<>(Arrays.asList((album[]) readFromFile(albumPath)));
        notificacoes = new ArrayList<>();
        readUserFile();
        for (music m : musicas) {
            System.out.println(m.toString());
        }

        socket = newMulticastSocket();
        newReceiverThread();
        newSenderThread();
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
            System.out.println(e);
        }
        return socket;
    }

    public void sendNotifications(user u) {
        for (notificacao n : notificacoes) {
            if (n.getUsername().equals(u.getUsername())) {
                sendString(socket, n.getMensagem());
            }
        }
    }

    public void decodeMessage(String[] msg) {

        if (msg[0].equals(id) || msg[0].equals("0")) {
            if (msg[1].equals("request")) {
                switch (msg[2]) {
                    case "server_id":
                        sendString(socket, id + ";" + "response;server_id;");
                        return;
                    case "register":
                        //user(boolean editor, int idade, String username, String password, String nome, int phone_num, String address, int num_cc)
                        user u = new user(false, Integer.parseInt(msg[3]), msg[4], msg[5], msg[6], Integer.parseInt(msg[7]), msg[8], Integer.parseInt(msg[9]));
                        u.setOnline(true);
                        sendNotifications(u);
                        sendString(socket, registerUser(u) ? "response;register;true" : "response;register;false");
                        return;
                    case "login":
                        System.out.println("Login request");
                        user u1 = users.get(msg[3]);
                        if (u1 == null || !u1.getPassword().equals(msg[4])) {
                            sendString(socket, "response;login;false;false");
                        } else {
                            u1.setOnline(true);
                            sendNotifications(u1);
                        }
                        sendString(socket, "response;login;true;" + (u1.isEditor() ? "true" : "false"));
                        return;
                    case "edit":
                        switch (msg[3]) {
                            case "music":
                                for (music m : musicas) {
                                    if (m.getNome().equals(msg[4])) {
                                        switch (msg[5]) {
                                            case "letra":
                                                m.setLyrics(msg[6]);
                                                break;
                                            case "nome":
                                                m.setNome(msg[6]);
                                        }
                                        return;
                                    }
                                }
                            case "author":
                                switch (msg[5]) {
                                    case "nome":
                                        for (author a : authors) {
                                            if (a.getNome().equals(msg[4])) {
                                                a.setNome(msg[5]);
                                                return;
                                            }
                                        }
                                    case "descricao":
                                        for (author a : authors) {
                                            if (a.getNome().equals(msg[4])) {
                                                a.setNome(msg[5]);
                                                return;
                                            }
                                        }
                                }
                            case "album":
                                for (album a : albums) {
                                    if (a.getNome().equals(msg[4])) {
                                        switch (msg[5]) {
                                            case "nome":
                                                a.setNome(msg[6]);
                                                break;
                                            case "genero":
                                                a.setGenero(msg[6]);
                                                break;
                                            case "descricao":
                                                a.setDescricao(msg[6]);
                                        }
                                        return;
                                    }
                                }
                        }
                    case "music_search":
                        switch (msg[4]) {
                            case "artista": {
                                ArrayList<String> resposta = new ArrayList<>();
                                for (music m : musicas) {
                                    if (!m.canGetMusic(msg[3])) {
                                        continue;
                                    }
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
                                sendString(socket, ret);
                                return;
                            }
                            case "genero": {
                                ArrayList<String> resposta = new ArrayList<>();
                                for (album a : albums) {
                                    if (a.getGenero().equals(msg[4])) {
                                        for (music m : a.getMusicas()) {
                                            if (!m.canGetMusic(msg[3])) continue;
                                            resposta.add(m.getNome());
                                        }
                                    }
                                }
                                String ret = "response;music_search;" + resposta.size() + ";";
                                for (String s : resposta) {
                                    ret += s + ";";
                                }
                                sendString(socket, ret);
                                return;
                            }
                            case "album": {
                                ArrayList<String> resposta = new ArrayList<>();
                                for (album a : albums) {
                                    if (a.getNome().equals(msg[4])) {
                                        for (music m : a.getMusicas()) {
                                            if (!m.canGetMusic(msg[3])) {
                                                continue;
                                            }
                                            resposta.add(m.getNome());
                                        }
                                        String ret = "response;music_search;" + resposta.size() + ";";
                                        for (String s : resposta) {
                                            ret += s + ";";
                                        }
                                        sendString(socket, ret);
                                        return;
                                    }
                                }
                                String ret = "response;music_search;" + resposta.size() + ";";
                                for (String s : resposta) {
                                    ret += s + ";";
                                }
                                sendString(socket, ret);
                                return;
                            }
                        }
                    case "details":
                        switch (msg[3]) {
                            case "album":
                                for (album a : albums) {
                                    if (a.getNome().equals(msg[4])) {
                                        sendString(socket, id + ";" + a.toString());
                                        return;
                                    }
                                }
                            case "artista":
                                for (author a : authors) {
                                    if (a.getNome().equals(msg[4])) {
                                        sendString(socket, a.toString());
                                        return;
                                    }
                                }
                            case "musica":
                                for (music m : musicas) {
                                    if (m.getNome().equals(msg[4])) {
                                        sendString(socket, m.toString());
                                        return;
                                    }
                                }
                        }
                    case "critic":
                        for (album a : albums) {
                            if (a.getNome().equals(msg[3])) {
                                a.addCritica(new critica(Integer.parseInt(msg[4]), msg[5]));
                                return;
                            }
                        }
                    case "give_editor":
                        users.get(msg[3]).setEditor(true);
                        String notificacao = "response;notification;" + msg[3] + ";Obteve privilégios de editor";
                        if (users.get(msg[3]).isOnline()) {
                            sendString(socket, notificacao);
                        } else {
                            notificacoes.add(new notificacao(msg[3], notificacao));
                        }
                }
            }
        }
    }

    public static void sendString(MulticastSocket socket, String msg) {
        byte[] buffer = msg.getBytes();
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MulticastServer.PORT);
            socket.send(packet);
        } catch (IOException e) {
            System.out.println(e);
        }
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

    public void newReceiverThread() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        String request;
                        // Ignore response packets
                        do {
                            request = receiveString(socket);
                        } while (request.contains("response"));
                        synchronized (requests) {
                            requests.add(request);
                            requests.notify();
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        }).start();
    }

    public void newSenderThread() {
        new Thread(new Runnable() {
            public void run() {

                while (true) {
                    String request;
                    byte[] buffer;
                    synchronized (requests) {
                        if (requests.size() == 0) {
                            try {
                                requests.wait();
                            } catch (InterruptedException e) {
                                System.out.println("Thread interrompida: " + e);
                            }
                        }
                        request = id + ";" + requests.getFirst();
                        requests.removeFirst();
                    }
                    decodeMessage(request.split(";"));
                }
            }
        }).start();
    }

}