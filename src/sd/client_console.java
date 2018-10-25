package sd;

import Classes.*;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class client_console extends UnicastRemoteObject implements client_interface {
    private static boolean exit_state;
    private static String user_name;
    private static rmi_interface_client client_console;

    client_console() throws RemoteException {
        super();
    }

    public void notify_client(String str) throws RemoteException {
        System.out.println(str);
    }

    private static int read_int() {
        Scanner scan = new Scanner(System.in);
        try {
            return Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String args[]) throws RemoteException {
        try {

            client_console = (rmi_interface_client) LocateRegistry.getRegistry(6789).lookup("192.1");

            System.out.printf("\nConnected to server\n");

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                exit_state = false;
                Scanner scan = new Scanner(System.in);
                String input;
                boolean logged = false;
                boolean editor = false;
                while (true) {
                    System.out.flush();
                    try {
                        if (!logged) {
                            System.out.println("0 -> -sair");
                            System.out.println("1 -> -logar");
                            System.out.println("2 -> -registar utilizadores");
                        } else {
                            System.out.println("0 -> -sair e desligar cliente");
                            System.out.println("1 -> -desconectar-se");
                            System.out.println("2 -> -procurar musicas e detalhes");//a partir d dados paciais, para mostrar so os detalhes da musica
                            System.out.println("3 -> -procurar albuns e detalhes");//same
                            System.out.println("4 -> -procurar artistas e detalhes");//ssame
                            System.out.println("5 -> -escrever critica num album");
                            if (editor) {
                                System.out.println("6 -> -promove user");
                                System.out.println("7 -> editar info de musicas");
                                System.out.println("8 -> editar info de albuns");
                                System.out.println("9 -> editar info de artistas");
                            }
                            System.out.println("10 -> baixar ou publicar musica");
                            System.out.println("11 -> partilhar musica com outro cliente");
                        }
                        input = scan.nextLine();
                        switch (input) {
                            case "-2": {
                                System.out.println("online users:");
                                System.out.println(client_console.get_online_clients());
                                System.out.println("is editor:");
                                System.out.println(editor);
                                break;
                            }
                            case "-1": {
                                try {
                                    client_console.sendMsg(user_name, "test notification");
                                } catch (Exception e) {
                                    System.out.println("Exception in main: " + e);
                                }

                                break;
                            }
                            case "0": {
                                exit_state = true;
                                System.exit(1);
                                return;
                            }

                            case "1": {//login--------------
                                if (!logged) {
                                    String username, password = "";
                                    System.out.println("Nome de Utilizador:");
                                    username = scan.nextLine();
                                    user_name = username;
                                    System.out.println("Palavra Chave:");
                                    password = scan.nextLine();
                                    int temp = client_console.login(username, password);
                                    if (temp == 0) {
                                        logged = false;
                                        System.out.println("Nao entrou!");
                                    } else {
                                        //System.out.println("subscribed");
                                        client_console c = new client_console();
                                        //System.out.println("cp");
                                        client_console.subscribe(user_name, c);
                                        //System.out.println("sent subscription to server");
                                        logged = true;
                                        System.out.println("Entrou!");
                                        if (temp % 10 == 1) {
                                            editor = true;
                                        }
                                    }
                                } else {//logout--------------------
                                    client_console.unsubscribe(user_name);
                                    logged = false;
                                    editor = false;
                                }
                                break;
                            }
                            case "2": {//registar-----------------
                                if (!logged) {
                                    user newuser = new user();

                                    System.out.println("Num de cc:");
                                    int t = read_int();
                                    if (t == -1) {
                                        System.out.println("input errado");
                                        break;
                                    }
                                    newuser.setNum_cc(t);
                                    System.out.println("Nome:");
                                    newuser.setNome(scan.nextLine());
                                    if (newuser.getNome().equals("")) break;
                                    System.out.println("UserName:");
                                    newuser.setUsername(scan.nextLine());
                                    if (newuser.getUsername().equals("")) break;
                                    System.out.println("Password:");
                                    newuser.setPassword(scan.nextLine());
                                    if (newuser.getPassword().equals("")) break;
                                    System.out.println("idade:");
                                    t = read_int();
                                    if (t == -1) {
                                        System.out.println("input errado");
                                        break;
                                    }
                                    newuser.setIdade(t);

                                    System.out.println("numero de telefone:");
                                    t = read_int();
                                    if (t == -1) {
                                        System.out.println("input errado");
                                        break;
                                    }
                                    newuser.setPhone_num(t);
                                    System.out.println("Endereço:");
                                    newuser.setAddress(scan.nextLine());

                                    boolean status = false;
                                    String str = newuser.pacote_String();
                                    try {
                                        //System.out.println("trying to comunicate with rmi");
                                        str = "request;register;" + str;
                                        status = client_console.send_all_return_bool(str);

                                    } catch (RemoteException ex) {
                                        Logger.getLogger(client_console.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    if (!status) {
                                        System.out.println("Erro no registo da nova unidade, por favor repita o processo");
                                        break;
                                    }
                                    System.out.println("Registo com sucesso!");


                                } else {//procurar musicas-----------------
                                    System.out.println("Procurar musicas por:\n 1-nome do artista\n 2-album\n 3-genero musical");
                                    int t = read_int();
                                    if (t == -1) {
                                        System.out.println("input errado");
                                        break;
                                    }
                                    String search = "";
                                    //artista/genero/album
                                    if (t == 1) search = "artista";
                                    else if (t == 2) search = "album";
                                    else if (t == 3) search = "genero";
                                    else {
                                        System.out.println("erro");
                                        break;
                                    }
                                    System.out.println("escreva aqui:");
                                    String str = new String("request;music_search;" + user_name + ";" + search + ";" + scan.nextLine());
                                    System.out.println("sent>> " + str);

                                    String arr[] = (client_console.send_one_return_str(str)).split(";");
                                    //System.out.println("cp");
                                    for (int i = 0; i < Integer.parseInt(arr[3]); i++) {
                                        System.out.println(i + "->" + arr[4 + i]);
                                    }
                                    System.out.println("escolha uma musica, para visao detalhada:");
                                    int i = read_int();
                                    if ((0 <= i) && (i < Integer.parseInt(arr[3]))) {
                                        str = client_console.send_one_return_str("request;details;musica;" + arr[4 + i]);
                                    } else {
                                        System.out.println("input errado");
                                        break;
                                    }

                                    System.out.println("Detalhes:\n" + str.split(";")[3]);
                                }
                                break;
                            }
                            case "3": {//detalhes de albuns---------------
                                if (!logged) {
                                    break;
                                }
                                System.out.println("Procurar albuns por:\n 1-nome do album\n 2-artista\n 3-nome da musica");
                                int t = read_int();
                                if (t == -1) break;

                                String search = "";
                                //artista/genero/album
                                if (t == 1) search = "album";
                                else if (t == 2) search = "artista";
                                else if (t == 3) search = "musica";
                                else {
                                    System.out.println("erro");
                                    break;
                                }
                                System.out.println("escreva aqui:");
                                String word = scan.nextLine();
                                /*request;album_search;album/artista/musica;nome(album/artista/music)
                                response;album_search;int item_count;String[item_count] nome_albuns*/
                                String str = "request;album_search;" + search + ";" + word;
                                //System.out.println("sent>> "+ str);
                                str = client_console.send_one_return_str(str);
                                //System.out.println("received>> "+ str);
                                String arr[] = (str).split(";");
                                if (Integer.parseInt(arr[3]) == 0) {
                                    System.out.println("Nao foram encontrados albuns");
                                    break;
                                }
                                for (int i = 0; i < Integer.parseInt(arr[3]); i++) {
                                    System.out.println(i + "->" + arr[4 + i]);
                                }
                                System.out.println("escolha um album, para visao detalhada:");
                                int i = read_int();
                                if ((0 <= i) && (i < Integer.parseInt(arr[3]))) {
                                    str = client_console.send_one_return_str("request;details;album;" + arr[4 + i]);
                                } else {
                                    System.out.println("input errado");
                                    break;
                                }
                                System.out.println("Detalhes:\n" + str.split(";")[3]);
                                break;
                            }
                            case "4": {//detalhes de artistas---------------
                                if (!logged) {
                                    break;
                                }
                                System.out.println("Procurar artistas por:\n 1-nome do artista\n 2-nome do album\n 3-nome da musica");
                                int t = read_int();
                                if (t == -1) break;

                                String search = "";
                                if (t == 1) search = "artista";
                                else if (t == 2) search = "album";
                                else if (t == 3) search = "musica";
                                if (search == "") break;

                                System.out.println("escreva aqui:");
                                String word = scan.nextLine();
                                /*request;artist_search;album/artista/musica;nome(album/artista/music)
                                response;artist_search;int item_count;String[item_count] nome_albuns*/
                                String str = "request;artist_search;" + search + ";" + word;
                                str = client_console.send_one_return_str(str);
                                System.out.println("sent>> " + str);
                                String arr[] = (client_console.send_one_return_str(str)).split(";");
                                if (Integer.parseInt(arr[3]) == 0) {
                                    System.out.println("Nao foram encrotados artistas");
                                    break;
                                }
                                for (int i = 0; i < Integer.parseInt(arr[3]); i++) {
                                    System.out.println(i + "->" + arr[4 + i]);
                                }
                                System.out.println("escolha um album, para visao detalhada:");
                                int i = read_int();
                                if (i == -1) {
                                    System.out.println("input errado");
                                    break;
                                }
                                if ((0 <= i) && (i < Integer.parseInt(arr[3]))) {
                                    str = client_console.send_one_return_str("request;details;artista;" + arr[4 + i]);
                                } else {
                                    System.out.println("input errado");
                                    break;
                                }
                                System.out.println("Detalhes:\n" + str.split(";")[3]);
                                break;
                            }
                            case "5": {//escrever uma critica
                                if (!logged) {
                                    break;
                                }
                                //escrever critica
                                //1-search album
                                System.out.println("Procurar albuns por:\n 1-nome do album\n 2-artista\n 3-nome da musica");
                                int t = read_int();
                                if (t == -1) break;

                                String search = "";
                                //artista/genero/album
                                if (t == 1) search = "album";
                                else if (t == 2) search = "artista";
                                else if (t == 3) search = "musica";
                                else {
                                    break;
                                }
                                System.out.println("escreva aqui:");
                                String word = scan.nextLine();
                                /*request;album_search;album/artista/musica;nome(album/artista/music)
                                response;album_search;int item_count;String[item_count] nome_albuns*/
                                String str = "request;album_search;" + search + ";" + word;
                                //System.out.println("sent>> "+ str);
                                String arr[] = (client_console.send_one_return_str(str)).split(";");
                                if (Integer.parseInt(arr[3]) == 0) {
                                    System.out.println("Nao foram encrotados albuns");
                                    break;
                                }
                                for (int i = 0; i < Integer.parseInt(arr[3]); i++) {
                                    System.out.println(i + "->" + arr[4 + i]);
                                }
                                System.out.println("escolha um album, para escrever critica:");
                                int i = read_int();
                                if (i == -1) {
                                    System.out.println("input errado");
                                    break;
                                }
                                //2-write critic
                                //request;critic;nome_album;int pontuacao; string descricao_critica
                                System.out.println("Pontuação:");
                                int points = read_int();
                                if (points == -1) {
                                    System.out.println("input errado");
                                    break;
                                }
                                System.out.println("comentario:");
                                String comentario = scan.nextLine();
                                if (comentario.length() > 300) {
                                    System.out.println("Tem calma Camoes, so 300 carateres!");
                                    break;
                                }
                                if ((0 <= i) && (i < Integer.parseInt(arr[3]))) {
                                    client_console.send_one_return_str("request;critic;" + arr[4 + i] + ";" + points + ";" + comentario + ";" + user_name);
                                } else {
                                    System.out.println("input errado");
                                    break;
                                }
                                break;
                            }
                            case "6": {//promover user to editor
                                if (!logged || !editor) break;
                                /*request;non_editor_list
                                response;non_editor_list;int n_users; String username[n_users]
                                request;give_editor;username*/
                                //list users
                                System.out.println("Lista de utilizadores nao editores:");
                                String str = client_console.send_one_return_str("request;non_editor_list");
                                String arr[] = str.split(";");
                                for (int i = 0; i < Integer.parseInt(arr[3]); i++) {
                                    System.out.println(i + "->" + arr[4 + i]);
                                }
                                System.out.println("escolha um utilizador para promover a editor");
                                int i = read_int();
                                if (i == -1) {
                                    System.out.println("input errado");
                                    break;
                                }
                                if ((0 <= i) && (i < Integer.parseInt(arr[3]))) {
                                    client_console.send_one_return_str("request;give_editor;" + arr[4 + i]);
                                    client_console.promove_user(arr[4 + i]);
                                } else {
                                    System.out.println("input errado");
                                    break;
                                }
                                break;
                            }
                            case "7": {//editar info de musicas
                                if (logged && editor) break;
                                System.out.println("editar info musicas");

                                break;
                            }
                            case "8": {//editar info de albuns
                                if (logged && editor) break;
                                System.out.println("editar info albuns");


                                break;
                            }
                            case "9": {//editar info de artisitas
                                if (logged && editor) break;
                                System.out.println("editar info artistas");


                                break;
                            }
                            case "10": {//baixar e publicar musica
                                if (logged) {
                                    Socket socket = null;
                                    try {
                                        System.out.println("caminho da pasta da musica:");
                                        String musicFilePath = scan.nextLine();
                                        //pasta com musicas

                                        System.out.println("[0] Upload\n[1] Download");

                                        int temp = read_int();
                                        if (temp != 1 && temp != 0) {
                                            System.out.println("input errado");
                                            break;
                                        }
                                        ArrayList<File> musicas;
                                        File[] files = new File(musicFilePath).listFiles();
                                        System.out.println("cp");
                                        if (files == null){
                                            musicas = new ArrayList<>();
                                        } else {
                                            musicas = new ArrayList<>(Arrays.asList(files));
                                            for (File file : musicas) {
                                                if (file.isFile()) {
                                                    System.out.println(file.getName());
                                                }
                                            }
                                        }
                                        System.out.println("cp1");
                                        socket = new Socket("127.0.0.1", client_console.get_tcp_port());
                                        System.out.println("cp2");
                                        InputStream in = socket.getInputStream();
                                        System.out.println("cp3");
                                        OutputStream out = socket.getOutputStream();
                                        String filename;

                                        switch (temp) {
                                            case 0:

                                                out.write(0);
                                                for (int i = 0; i < musicas.size(); i++) {
                                                    System.out.println("[" + i + "] " + musicas.get(i).getName());
                                                }
                                                int escolha;
                                                while (true) {
                                                    System.out.print("Escolha uma musica para upload: ");
                                                    escolha = read_int();
                                                    if (escolha == -1) {
                                                        System.out.println("wrong input");
                                                        break;
                                                    }
                                                    for (int i = 0; i<musicas.size();i++) {
                                                        if (musicas.get(i).isFile()) {
                                                            System.out.println(i+ "->"+musicas.get(i).getName());
                                                        }
                                                    }
                                                    if (escolha >= 0 && escolha < musicas.size()) ;

                                                }
                                                File f = musicas.get(escolha);
                                                String newmusicname = f.getName();

                                                System.out.println("tem banda[0] ou autor[1]?");
                                                int banda = read_int();
                                                boolean boolbanda;
                                                if(banda == 0){
                                                    System.out.println("escreva o nome da banda");
                                                    boolbanda = false;
                                                }
                                                else if(banda == 1){
                                                    boolbanda = true;
                                                    System.out.println("escreva o nome do autor");
                                                }
                                                else {
                                                    System.out.println("input errado");
                                                    break;
                                                }
                                                String bandaousutor = scan.nextLine();
                                                System.out.println("Quais sao as lyrics?");
                                                String lyrics = scan.nextLine();
                                                String pacote_String = newmusicname + ";" + boolbanda + ";" + bandaousutor + ";" + lyrics;
                                                client_console.send_all_return_str("request;uploaded_files;username;"+pacote_String);
                                                out.write(f.getName().getBytes());
                                                TCP.uploadFile(f, out);
                                                break;


                                            case 1:
                                                System.out.println("Procurar musicas por:\n 1-nome do artista\n 2-album\n 3-genero musical");
                                                int t = read_int();
                                                if (t == -1) {
                                                    System.out.println("input errado");
                                                    break;
                                                }
                                                String search = "";
                                                //artista/genero/album
                                                if (t == 1) search = "artista";
                                                else if (t == 2) search = "album";
                                                else if (t == 3) search = "genero";
                                                else {
                                                    System.out.println("erro no input");
                                                    break;
                                                }
                                                System.out.println("escreva aqui:");
                                                String str = new String("request;music_search;" + user_name + ";" + search + ";" + scan.nextLine());
                                                System.out.println("sent>> " + str);

                                                String arr[] = (client_console.send_one_return_str(str)).split(";");
                                                //System.out.println("cp");
                                                for (int i = 0; i < Integer.parseInt(arr[3]); i++) {
                                                    System.out.println(i + "->" + arr[4 + i]);
                                                }
                                                System.out.println("escolha uma musica, para baixar:");
                                                int i = read_int();
                                                if ((0 <= i) && (i < Integer.parseInt(arr[3]))) {
                                                    filename = arr[4 + i];
                                                } else {
                                                    System.out.println("input errado");
                                                    break;
                                                }

                                                out.write(1);
                                                out.write(filename.getBytes());
                                                // Se in.read == 1 encontrou ficheiro
                                                if (in.read() != 1) {
                                                    System.out.println("File " + filename + " not found");
                                                }

                                                TCP.downloadFile(musicFilePath, musicas, filename, in);
                                                break;
                                        }

                                        //pedir port tcp
                                        //abrir ligação
                                        //escolher file
                                        //upload ou download
                                    } catch (IOException e) {
                                        System.out.println(e);
                                    }
                                    if (socket != null) {
                                        try {
                                            socket.close();
                                        } catch (IOException e) {
                                            System.out.println(e);
                                        }
                                    }
                                }
                                break;
                            }
                            case "11": {//partilhar musica com outro cliente
                                if (!logged) {
                                    System.out.println("Publicar uma musica:");

                                }
                                break;
                            }
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(client_console.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("W: Interrupt received, killing server…");
                try {
                    client_console.unsubscribe(user_name);
                } catch (RemoteException ex) {
                    Logger.getLogger(client_console.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        while (true) {

            if (exit_state == true) {
                System.out.println("saindo");
                System.exit(1);
                return;
            }
        }
    }
}
