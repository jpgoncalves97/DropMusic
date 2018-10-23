package sd;

import Classes.*;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.*;

class MulticastServer extends Thread implements Serializable {

    private final static String MULTICAST_ADDRESS = "224.0.224.0";
    private final static int PORT = 4321;
    protected static int TCP_PORT = 1234;
    private MulticastSocket socket;
    private final String id = Long.toString(System.currentTimeMillis());
    private final String mainPath = "C:/multicast/";
    private final String userPath = mainPath + "users";
    private final String albumPath = mainPath + "albuns";
    private final String authorPath = mainPath + "authors";
    private final String musicPath = mainPath + "music";
    private final String bandPath = mainPath + "bands";

    private final String musicFilePath = mainPath + "musica";
    private HashMap<String, user> users;
    private ArrayList<author> authors;
    private ArrayList<album> albums;
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
            //listMusic();
        }
        if (!(new File(userPath).exists()) ||
                !(new File(albumPath).exists()) ||
                !(new File(authorPath).exists()) ||
                !(new File(musicPath).exists())) {
            defaultFiles();
        }
        requests = new LinkedList<>();
        musicas = new ArrayList<>(Arrays.asList((music[]) readFromFile(musicPath)));
        users = new HashMap<>(100);
        authors = new ArrayList<>(Arrays.asList((author[]) readFromFile(authorPath)));
        albums = new ArrayList<>(Arrays.asList((album[]) readFromFile(albumPath)));
        readUserFile();

        socket = newMulticastSocket();
        newReceiverThread();
        newSenderThread();

        //listAllFiles();
    }

    public void defaultFiles(){
        author[] a = {new author("Eminem"), new author("Drake"), new author("Michael Jackson"),
                new author("Angus Young"), new author("Brian Johnson"), new author("Bon Scott"), new author("Axl Rose"),
                new author("Roger Waters"), new author("David Gilmour"), new author("Syd Barrett"),
                new author("Richard Wright"), new author("Nick Mason"), new author("Bob Klose"),
                new author("Damon Albarn"), new author("Jamie Hewlett"), new author("Paul Simonon")};
        band[] b = {new band("AC/DC"), new band("Pink Floyd"), new band("Gorillaz")};
        b[0].setAuthors(new ArrayList<>(Arrays.asList(a[3], a[4], a[5], a[6])));
        b[1].setAuthors(new ArrayList<>(Arrays.asList(a[7], a[8], a[9], a[10], a[11], a[12])));
        b[2].setAuthors(new ArrayList<>(Arrays.asList(a[13], a[14], a[15])));
        @SuppressWarnings("dep-ann")
        album[] al = {new album("Back in Black", "Rock", new Timestamp(1980, 7, 25, 0, 0, 0, 0)),
                new album("The Dark Side of The Moon", "Rock", new Timestamp(1973, 3, 1, 0, 0, 0, 0)),
                new album("Kamikaze", "Hip-Hop", new Timestamp(2018, 8, 31, 0, 0, 0, 0)),
                new album("Scorpion", "Hip-Hop", new Timestamp(2018, 6, 29, 0, 0, 0, 0)),
                new album("Thriller", "Pop", new Timestamp(1982, 11, 30, 0, 0, 0, 0)),
                new album("Demon days", "Pop", new Timestamp(2015, 5, 11, 0, 0, 0, 0))};
        b[0].addAlbum(al[0]);
        b[1].addAlbum(al[1]);
        b[2].addAlbum(al[5]);
        //String nome, band b, boolean publico, album album, String lyrics, Timestamp timelenght)
        music[] m = {new music("Any Colour You Like", b[1], true, al[1], ""),
                new music("Back In Black", b[0], true, al[0], ""),
                new music("Beat it", a[2], true, al[4], ""),
                new music("Billie Jean", a[2], true, al[4], ""),
                new music("Dare", b[2], true, al[5], ""),
                new music("Dirty Harry", b[2], true, al[5], ""),
                new music("Fall", a[0], true, al[2], ""),
                new music("Gods Plan", a[1], true, al[3], ""),
                new music("Have a drink on me", b[0], true, al[0], ""),
                new music("In My Feelings", a[1], true, al[3], ""),
                new music("Kamikaze", a[0], true, al[2], ""),
                new music("Kids with guns", b[2], true, al[5], ""),
                new music("Lucky You", a[0], true, al[2], ""),
                new music("Money", b[1], true, al[1], ""),
                new music("Thriller", a[2], true, al[4], ""),
                new music("Us and Them", b[1], true, al[1], ""),
                new music("You shook me all night long", b[0], true, al[0], "")};
        al[0].addMusicas(m[1]);
        al[0].addMusicas(m[8]);
        al[0].addMusicas(m[16]);
        al[1].addMusicas(m[0]);
        al[1].addMusicas(m[13]);
        al[1].addMusicas(m[15]);
        al[2].addMusicas(m[6]);
        al[2].addMusicas(m[10]);
        al[2].addMusicas(m[12]);
        al[3].addMusicas(m[7]);
        al[3].addMusicas(m[9]);
        al[4].addMusicas(m[2]);
        al[4].addMusicas(m[3]);
        al[4].addMusicas(m[14]);
        al[5].addMusicas(m[4]);
        al[5].addMusicas(m[5]);
        al[5].addMusicas(m[11]);
        a[0].addAlbum(al[2]);
        a[1].addAlbum(al[3]);
        a[2].addAlbum(al[4]);
        al[0].setAuthors(b[0].getAuthors());
        al[1].setAuthors(b[1].getAuthors());
        al[2].setAuthors(new ArrayList<>(Arrays.asList(a[0])));
        al[3].setAuthors(new ArrayList<>(Arrays.asList(a[1])));
        al[4].setAuthors(new ArrayList<>(Arrays.asList(a[2])));
        al[5].setAuthors(b[2].getAuthors());
        writeToFile(albumPath, al);
        writeToFile(musicPath, m);
        writeToFile(authorPath, a);
        writeToFile(bandPath, b);
    }
    public void listAllFiles() {
        System.out.println("Users: ");
        for (String user : users.keySet()) {
            System.out.println(users.get(user).toString());
        }
        System.out.println("Músicas: ");
        for (music m : musicas) {
            System.out.println(m.toString());
        }
        System.out.println("Artistas: ");
        for (author a : authors) {
            System.out.println(a.toString());
        }
        System.out.println("Albuns: ");
        for (album a : albums) {
            System.out.println(a.toString());
        }
        System.out.println("Bandas: ");
        band[] b = (band [])readFromFile(bandPath);
        for (int i = 0; i < b.length; i++){
            System.out.println(b[i].toString());
        }
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
            registerUser(new user(false, 0, "a", "a", "a", 0, "", 0));
            registerUser(new user(false, 0, "b", "b", "b", 0, "", 0));
            registerUser(new user(false, 0, "c", "c", "c", 0, "", 0));

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
            System.out.println("Error reading from file: " + e);
            return null;
        }
        return o;
    }

    public void writeToFile(String path, Object o) {
        File f = new File(path);
        if (!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e){
                System.out.println("Error creating file: " + e);
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
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
        Iterator<String> it = u.getNotificacoes().iterator();
        while (it.hasNext()) {
            MulticastServer.sendString(socket, it.next());
            it.remove();
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
                        sendString(socket, id + ";" + (registerUser(u) ? "response;register;true" : "response;register;false"));
                        sendNotifications(u);
                        return;
                    case "login":
                        user u1 = users.get(msg[3]);
                        if (u1 == null || !u1.getPassword().equals(msg[4])) {
                            sendString(socket, id + ";response;login;false;false");
                        } else {
                            u1.setOnline(true);
                            sendString(socket, id + ";response;login;true;" + (u1.isEditor() ? "true" : "false"));
                            sendNotifications(u1);
                        }
                        return;
                    case "logout":
                        users.get(msg[3]).setOnline(false);
                        sendString(socket, id + ";response;ignore");
                        return;
                        // Feito até aqui
                        // --------------------------------------------------------------------------------------------------
                    case "edit":
                        switch (msg[4]) {
                            case "music":
                                for (music m : musicas) {
                                    if (m.getNome().equals(msg[5])) {
                                        switch (msg[6]) {
                                            case "letra":
                                                m.setLyrics(msg[7]);
                                                break;
                                            case "nome":
                                                m.setNome(msg[7]);
                                                break;
                                        }
                                        sendString(socket, id + ";response;ignore");
                                        return;
                                    }
                                }
                            case "author":
                                for (author a : authors) {
                                    if (a.getNome().equals(msg[5])) {
                                        switch (msg[6]) {
                                            case "nome":
                                                a.setNome(msg[7]);
                                                sendString(socket, id + ";response;ignore");
                                                return;
                                            case "descricao":
                                                a.setDescricao(msg[7], msg[3]);
                                                for (String username : a.getEditores()) {
                                                    user u2 = users.get(username);
                                                    String n = id + ";response;notification;" + username +
                                                            ";Foi alterada a descricao do artista " + a.getNome();
                                                    if (u2.isOnline()) {
                                                        sendString(socket, n);
                                                        return;
                                                    } else {
                                                        u2.addNotificacao(n);
                                                        sendString(socket, id + ";response;ignore");
                                                        return;
                                                    }
                                                }
                                        }
                                    }
                                }
                            case "album":
                                for (album a : albums) {
                                    if (a.getNome().equals(msg[4])) {
                                        switch (msg[5]) {
                                            case "nome":
                                                a.setNome(msg[6]);
                                                sendString(socket, id + ";response;ignore");
                                                return;
                                            case "genero":
                                                a.setGenero(msg[6]);
                                                sendString(socket, id + ";response;ignore");
                                                return;
                                            case "descricao":
                                                a.setDescricao(msg[6], msg[4]);
                                                for (String username : users.keySet()) {
                                                    sendNotifications(users.get(username));
                                                }
                                                sendString(socket, id + ";response;ignore");
                                                return;
                                        }
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
                                        if (a.getNome().equals(msg[5])) {
                                            resposta.add(m.getNome());
                                        }
                                    } else {
                                        band b = m.getBand();
                                        for (author a1 : b.getAuthors()) {
                                            if (a1.getNome().equals(msg[5])) {
                                                resposta.add(m.getNome());
                                            }
                                        }
                                    }
                                }
                                String ret = id + ";response;music_search;" + resposta.size() + ";";
                                for (String s : resposta) {
                                    ret += s + ";";
                                }
                                sendString(socket, ret);
                                return;
                            }
                            case "genero": {
                                ArrayList<String> resposta = new ArrayList<>();
                                for (album a : albums) {
                                    if (a.getGenero().equals(msg[5])) {
                                        for (music m : a.getMusicas()) {
                                            if (!m.canGetMusic(msg[3])) continue;
                                            resposta.add(m.getNome());
                                        }
                                    }
                                }
                                String ret = id + ";response;music_search;" + resposta.size() + ";";
                                for (String s : resposta) {
                                    ret += s + ";";
                                }
                                sendString(socket, ret);
                                return;
                            }
                            case "album": {
                                ArrayList<String> resposta = new ArrayList<>();
                                for (album a : albums) {
                                    if (a.getNome().equals(msg[5])) {
                                        for (music m : a.getMusicas()) {
                                            if (!m.canGetMusic(msg[3])) {
                                                continue;
                                            }
                                            resposta.add(m.getNome());
                                        }
                                        String ret = id + ";response;music_search;" + resposta.size() + ";";
                                        for (String s : resposta) {
                                            ret += s + ";";
                                        }
                                        sendString(socket, ret);
                                        return;
                                    }
                                }
                                String ret = id + ";response;music_search;" + resposta.size() + ";";
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
                                        sendString(socket, id + ";response;details;" + a.toString());
                                        return;
                                    }
                                }
                            case "artista":
                                for (author a : authors) {
                                    if (a.getNome().equals(msg[4])) {
                                        sendString(socket, id + ";response;details;" + a.toString());
                                        return;
                                    }
                                }
                            case "musica":
                                for (music m : musicas) {
                                    if (m.getNome().equals(msg[4])) {
                                        sendString(socket, id + ";response;details;" + m.toString());
                                        return;
                                    }
                                }
                        }
                    case "critic":
                        for (album a : albums) {
                            if (a.getNome().equals(msg[3])) {
                                a.addCritica(new critica(Integer.parseInt(msg[4]), msg[5]));
                                sendString(socket, id + ";response;ignore");
                                return;
                            }
                        }
                    case "give_editor":
                        System.out.println("giving editor\n" + msg[3] + " " + users.get(msg[3]).isOnline());
                        users.get(msg[3]).setEditor(true);
                        String notificacao = id + ";response;notification;" + msg[3] + ";Obteve privilégios de editor";
                        if (users.get(msg[3]).isOnline()) {
                            sendString(socket, notificacao);
                        } else {
                            users.get(msg[3]).addNotificacao(notificacao);
                            sendString(socket, id + ";response;ignore");
                            return;
                        }
                    case "non_editor_list":
                        ArrayList<String> l = new ArrayList<>();
                        for (String key : users.keySet()){
                            if (!users.get(key).isEditor()){
                                l.add(users.get(key).getUsername());
                            }
                        }
                        String m = id + ";response;non_editor_list;" + l.size() + ";";
                        for (String s : l){
                            m += s + ";";
                        }
                        sendString(socket, m);
                        return;
                    case "user_list":
                        for (String key : users.keySet()) {
                            System.out.println(key);
                            System.out.println(users.get(key).isEditor());
                        }
                        sendString(socket, id + ";response;ignore");
                        return;
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
                        request = requests.getFirst();
                        requests.removeFirst();
                    }
                    decodeMessage(request.split(";"));
                }
            }
        }).start();
    }

}