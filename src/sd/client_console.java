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
    private static String input;
    private static boolean logged, editor,redirect;
    private static String musicFilePath = "C:/Users/j/Desktop/musica_cliente";


    client_console() throws RemoteException {
        super();
    }

    public void notify_client(String str) throws RemoteException {
        System.out.println(str);

    }

    public void change_to_editor() throws RemoteException {
        editor = true;
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

            logged = false;
            editor = false;


            if(args.length != 0){
                redirect = true;
                System.out.println("input = " + args[0]);
                input = args[0];
                System.out.println("logged = " + args[1]);
                if(args[1].equals("true")) logged = true;
                else logged = false;
                System.out.println("editor = " + args[2]);
                if(args[2].equals("true")) editor = true;
                else editor = false;
                System.out.println("username = " + args[3]);
                user_name = args[3];
            }


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                exit_state = false;
                Scanner scan = new Scanner(System.in);


                while (true) {
                    System.out.flush();
                    try {
                        if (!logged) {
                            System.out.println("0 -> sair");
                            System.out.println("1 -> logar");
                            System.out.println("2 -> registar utilizadores");
                        } else {
                            System.out.println("0 -> sair e desligar cliente");
                            System.out.println("1 -> desconectar-se");
                            System.out.println("2 -> procurar musicas e detalhes");//a partir d dados paciais, para mostrar so os detalhes da musica
                            System.out.println("3 -> procurar albuns e detalhes");//same
                            System.out.println("4 -> procurar artistas e detalhes");//ssame
                            System.out.println("5 -> escrever critica num album");
                            if (editor) {
                                System.out.println("6 -> -promove user");
                                System.out.println("7 -> editar info de musicas");
                                System.out.println("8 -> editar info de albuns");
                                System.out.println("9 -> editar info de artistas");
                            }
                            System.out.println("10 -> baixar ou publicar musica");
                            System.out.println("11 -> partilhar musica com outro cliente");
                            System.out.println("12 -> ver playlists");
                            System.out.println("13 -> criar playlist");
                            System.out.println("14 -> adicionar musicas a playlists");
                            System.out.println("15 -> apagar playlist");

                        }

                        if(redirect){
                            System.out.println("redirection input");
                            input = args[0];
                        }else{
                            redirect = false;
                            input = scan.nextLine();
                        }
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
                                    System.out.println("Palavra Chave:");
                                    password = scan.nextLine();
                                    if(username.equals("") || password.equals("")){
                                        System.out.println("mau username ou password");
                                        break;
                                    }
                                    user_name = username;
                                    int temp = client_console.login(username, password);
                                    if (temp == 0) {
                                        logged = false;
                                        System.out.println("Nao entrou!");
                                    } else {
                                        logged = true;
                                        String notificacoes = client_console.send_one_return_str("request;notification;"+user_name);
                                        String notify[] = notificacoes.split(";");
                                        if (notify.length >= 5)
                                            System.out.println("Notificações: \n" + notify[4]);
                                        System.out.println("Entrou!");
                                        if (temp % 10 == 1) {
                                            editor = true;
                                        }
                                        client_console c = new client_console();
                                        client_console.subscribe(user_name, c,editor);
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
                                    System.out.println("Procurar musicas por:\n 1-nome do artista\n 2-album\n 3-genero musical\n 4-nome");
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
                                    else if (t == 4) search = "nome";
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
                                if (search.equals("")) break;

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
                                if (!logged ) break;
                                if (!editor) break;
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
                                    client_console.send_all_return_str("request;give_editor;" + arr[4 + i]);
                                    client_console.promove_user(arr[4 + i]);
                                } else {
                                    System.out.println("input errado");
                                    break;
                                }
                                break;
                            }
                            case "7": {//editar info de musicas
                                if (!logged ) break;
                                if (!editor) break;
                                System.out.println("Editar info musicas");
                                System.out.println("Procurar musicas por:\n 1-nome do artista\n 2-album\n 3-genero musical\n 4-nome");
                                int t = read_int();
                                if (t == -1) {
                                    System.out.println("input errado");
                                    break;
                                }
                                String search = "";
                                //artista/genero/album/nome
                                if (t == 1) search = "artista";
                                else if (t == 2) search = "album";
                                else if (t == 3) search = "genero";
                                else if (t == 4) search = "nome";
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
                                /*request;edit;username;music;nome;letra;alteracao
                                request;edit;username;music;nome;nome;alteracao*/
                                System.out.println("Editar nome[0] ou letra[1]?");
                                String changestr;
                                int change = read_int();
                                if ((change == 0)) {
                                    changestr = "nome";
                                } else if (change == 1) {
                                    changestr = "letra";
                                }else{
                                    System.out.println("Input errado");
                                    break;
                                }
                                System.out.println("Escreva a mudanca:");
                                String newstr = scan.nextLine();
                                client_console.send_all_return_str("request;edit;"+user_name+";music;"+arr[4+i]+";"+changestr+";"+newstr);
                                break;
                            }
                            case "8": {//editar info de albuns
                                if (!logged ) break;
                                if (!editor) break;
                                System.out.println("Editar info de albuns");
                                System.out.println("Procurar o album por:\n 1-nome do album\n 2-artista\n 3-nome da musica");
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
                                System.out.println("escolha um album, para editar a informacao:");
                                int i = read_int();
                                if ((0 <= i) && (i < Integer.parseInt(arr[3]))) {
                                    str = client_console.send_one_return_str("request;details;album;" + arr[4 + i]);
                                } else {
                                    System.out.println("input errado");
                                    break;
                                }
                                /*request;edit;username;album;nome;nome;alteracao
                                request;edit;username;album;nome;genero;alteracao
                                request;edit;username;album;nome;descricao;alteracao*/
                                System.out.println("Editar nome[0], genero[1] ou descricao[2]?");
                                String changestr;
                                int change = read_int();
                                if ((change == 0)) {
                                    changestr = "nome";
                                } else if (change == 1) {
                                    changestr = "genero";
                                } else if (change == 2){
                                    changestr = "descricao";
                                }else{
                                    System.out.println("Input errado");
                                    break;
                                }
                                System.out.println("Escreva a mudanca:");
                                String newstr = scan.nextLine();
                                client_console.send_all_return_str("request;edit;"+user_name+";album;"+arr[4+i]+";"+changestr+";"+newstr);
                                client_console.notify("O utilizador "+user_name+" alterou o album "+ arr[4+i]);
                                break;
                            }
                            case "9": {//editar info de artisitas
                                if (!logged ) break;
                                if (!editor) break;
                                System.out.println("Editar info artistas");
                                System.out.println("Procurar artistas por:\n 0-nome do artista\n 1-albuns\n 2- nome da musica");
                                int t = read_int();
                                if (t == -1) break;

                                String search = "";
                                if (t == 1) search = "artista";
                                else if (t == 2) search = "album";
                                else if (t == 3) search = "musica";
                                if (search.equals("")) break;

                                System.out.println("escreva aqui:");
                                String word = scan.nextLine();
                                /*request;artist_search;album/artista/musica;nome(album/artista/music)
                                response;artist_search;int item_count;String[item_count] nome_albuns*/
                                String str = "request;artist_search;" + search + ";" + word;
                                str = client_console.send_one_return_str(str);
                                System.out.println("sent>> " + str);
                                String arr[] = (str).split(";");
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
                                /*request;edit;username;author;nome;nome;alteracao
                                request;edit;username;author;nome;descricao;alteracao*/
                                System.out.println("Editar nome[0], descricao[1]?");
                                String changestr;
                                int change = read_int();
                                if ((change == 0)) {
                                    changestr = "nome";
                                } else if (change == 1){
                                    changestr = "descricao";
                                }else{
                                    System.out.println("Input errado");
                                    break;
                                }
                                System.out.println("Escreva a mudanca:");
                                String newstr = scan.nextLine();
                                client_console.send_all_return_str("request;edit;"+user_name+";author;"+arr[4+i]+";"+changestr+";"+newstr);
                                client_console.notify("O utilizador "+user_name+" alterou o artista "+ arr[4+i]);

                                break;
                            }
                            case "10": {//baixar e publicar musica
                                if (logged) {
                                    Socket socket = null;
                                    try {
                                        System.out.println("[0] Upload\n[1] Download");

                                        int temp = read_int();
                                        if (temp != 1 && temp != 0) {
                                            System.out.println("input errado");
                                            break;
                                        }
                                        ArrayList<File> musicas;
                                        System.out.println("Escolha o diretorio da pasta de musicas:");
                                        musicFilePath = scan.nextLine();
                                        File[] files = new File(musicFilePath).listFiles();
                                        System.out.println("Musicas na pasta escolhida");
                                        if (files == null){
                                            musicas = new ArrayList<>();
                                        } else {
                                            musicas = new ArrayList<>(Arrays.asList(files));
                                        }
                                        int tcp_port = client_console.get_tcp_port();
                                        System.out.println(tcp_port);
                                        socket = new Socket("127.0.0.1", tcp_port);
                                        InputStream in = socket.getInputStream();
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
                                                    if (escolha >= 0 && escolha < musicas.size()) break;

                                                }
                                                File f = musicas.get(escolha);
                                                String newmusicname = f.getName();

                                                System.out.println("tem banda[0] ou autor[1]?");
                                                int banda = read_int();
                                                boolean boolbanda;
                                                if(banda == 0){
                                                    System.out.println("escreva o nome da banda");
                                                    boolbanda = true;
                                                }
                                                else if(banda == 1){
                                                    boolbanda = false;
                                                    System.out.println("escreva o nome do autor");
                                                }
                                                else {
                                                    System.out.println("input errado");
                                                    break;
                                                }
                                                String bandaousutor = scan.nextLine();
                                                System.out.println("Quais sao as lyrics?");
                                                String lyrics = scan.nextLine();
                                                String pacote_String = newmusicname + ";" + Boolean.toString(boolbanda) + ";" + bandaousutor + ";" + lyrics;
                                                client_console.send_all_return_str("request;upload;"+user_name+";"+pacote_String);
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
                                                filename += ".mp3";
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
                                if (logged) {
                                    System.out.println("Publicar uma musica:");
                                    System.out.println("escolher a musica");
                                    /*request;user_songs;username
                                    response;user_songs;int count; String nomes[count]*/

                                    String str = client_console.send_one_return_str("request;user_songs;"+user_name);
                                    String arr[] = str.split(";");
                                    for (int i = 0; i < Integer.parseInt(arr[3]); i++) {
                                        System.out.println(i + "->" + arr[4 + i]);
                                    }
                                    int choice = read_int();
                                    if(choice == -1 || (choice<0) || (choice>Integer.parseInt(arr[3]))){
                                        System.out.println("input errado");
                                        break;
                                    }
                                    String nome_musica = arr[4 + choice];
                                    System.out.println("partilhar a musica para todos[0] ou um utilizador especifico[1]?");
                                    int inpt = read_int();
                                    if(inpt != 0 && inpt != 1){
                                        System.out.println("input errado");
                                        break;
                                    }
                                    if(inpt == 0){
                                        client_console.send_one_return_str("request;share;"+ nome_musica+";true");
                                    }else {
                                        String[] res = client_console.send_one_return_str("request;user_list").split(";");
                                        for (int i = 0; i < Integer.parseInt(res[3]); i++) {
                                            System.out.println(i + "->" + res[4 + i]);
                                        }
                                        choice = read_int();
                                        if(choice == -1 || (choice<0) || (choice>Integer.parseInt(res[3]))){
                                            System.out.println("input errado");
                                            break;
                                        }
                                        client_console.send_all_return_str("request;share;"+ nome_musica+";false;" + res[4 + choice]);
                                    }
                                    //request;share;filename;username
                                }
                                break;
                            }
                            case "12":{
                                if (!logged) break;
                                //ver playlists
                                System.out.println("Ver playlists publicas[0] ou privadas[1]?");
                                /*request;user_playlists;username
                                response;user_playlists;username;int count;nome_playlist[count]
                                request;get_playlists;username
                                response;get_playlists;username;int count;nome_playlist[count]*/
                                int choice = read_int();
                                String str = "";
                                if (choice == 0){
                                    str = client_console.send_one_return_str("request;get_playlists;"+user_name);
                                }else if (choice == 1){
                                    str = client_console.send_one_return_str("request;user_playlists;"+user_name);
                                }else{
                                    System.out.println("input errado");
                                }

                                String arr[] = str.split(";");
                                for (int i = 0; i < Integer.parseInt(arr[4]); i++) {
                                    System.out.println(i + "->" + arr[5 + i]);
                                }
                                //request;playlist_details;nome_playlist
                                System.out.println("Escolha uma playlist para ver as musicas:");
                                int param = read_int();
                                if((0<=param) && (param<Integer.parseInt(arr[4]))){
                                    System.out.println(client_console.send_all_return_str("request;playlist_details;"+arr[5+param]).split(";")[3]);
                                }
                                break;

                            }
                            case "13":{
                                if (!logged) break;
                                //criar playlists
                                System.out.println("Criar uma playlist");
                                System.out.println("Nome da playlist:");
                                String nome = scan.nextLine();
                                System.out.println("Criar uma playlist publicas[0] ou privadas[1]?");
                                int choice = read_int();
                                if (choice == 0){
                                    client_console.send_all_return_str("request;create_playlist;"+user_name+";"+nome+";false");
                                }else if (choice == 1){
                                    client_console.send_all_return_str("request;create_playlist;"+user_name+";"+nome+";true");
                                }else{
                                    System.out.println("input errado");
                                }
                                break;

                            }
                            case "14":{
                                if (!logged) break;
                                //adicionar musicas a playlists
                                System.out.println("Adicionar musicas a uma playlist");
                                System.out.println("Escolha a playlist");
                                String str = client_console.send_one_return_str("request;user_playlists;"+user_name);
                                String arr[] = str.split(";");
                                for (int i = 0; i < Integer.parseInt(arr[4]); i++) {
                                    System.out.println(i + "->" + arr[5 + i]);
                                }
                                int choice = read_int();
                                if(choice == -1 || (choice<0) || (choice>Integer.parseInt(arr[4]))){
                                    System.out.println("input errado");
                                    break;
                                }

                                str = client_console.send_one_return_str("request;music_list;"+user_name);
                                String arr2[] = str.split(";");
                                for (int i = 0; i < Integer.parseInt(arr2[3]); i++) {
                                    System.out.println(i + "->" + arr2[4 + i]);
                                }
                                int choice2 = read_int();
                                if(choice2 == -1 || (choice2<0) || (choice2>Integer.parseInt(arr2[3]))){
                                    System.out.println("input errado");
                                    break;
                                }
                                //request;add_to_playlist;username;nome_playlist;nome_musica
                                client_console.send_all_return_str("request;add_to_playlist;"+user_name+";"+arr[5+choice]+";"+arr2[4+choice2]);

                                break;
                            }
                            case "15":{
                                if (!logged) break;
                                //apagar playlists
                                System.out.println("Apagar uma playlist");
                                System.out.println("Escolha a playlist:");
                                String str = client_console.send_one_return_str("request;user_playlists;"+user_name);
                                String arr[] = str.split(";");
                                for (int i = 0; i < Integer.parseInt(arr[4]); i++) {
                                    System.out.println(i + "->" + arr[5 + i]);
                                }
                                int choice = read_int();
                                if(choice == -1 || (choice<0) || (choice>Integer.parseInt(arr[4]))){
                                    System.out.println("input errado");
                                    break;
                                }
                                client_console.send_all_return_str("request;delete_playlist;"+user_name+";"+arr[5+choice]);
                                break;
                            }
                        }
                    } catch (RemoteException ex) {
                        try {
                            String arr[] = {input, String.valueOf(logged), String.valueOf(editor),user_name};
                            main(arr);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    if(logged)
                        System.out.println("Clique em alguma tecla para avançar...");
                        scan.nextLine();
                }
            }
        }).start();

        while (true) {

            if (exit_state == true) {
                System.out.println("saindo");
                System.exit(1);
                return;
            }
        }
    }
}
