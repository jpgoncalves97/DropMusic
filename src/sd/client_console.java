package sd;

import Classes.*;
import java.io.Console;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class client_console extends UnicastRemoteObject implements client_interface{
    private static boolean exit_state;
    private static String user_name;
    private static rmi_interface_client client_console;

    client_console() throws  RemoteException{
        super();
    }
    public void notify_client(String str) throws RemoteException{
        System.out.println(str);
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
                            System.out.println("0 -> sair");
                            System.out.println("1 -> logar");
                            System.out.println("2 -> registar utilizadores");
                        }
                        else{
                            System.out.println("0 -> sair");
                            System.out.println("1 -> desconectar-se");
                            System.out.println("2 -> procurar musicas");//a partir d dados paciais, para mostrar so os detalhes da musica
                            System.out.println("3 -> procurar detalhes de albuns");//same
                            System.out.println("4 -> procurar detalhes de musicas");//ssame
                            System.out.println("5 -> escrever critica num album");
                            if(editor){
                                System.out.println("6 -> promove user");
                                System.out.println("7 -> editar info de musicas");
                                System.out.println("8 -> editar info de albuns");
                                System.out.println("9 -> editar info de artistas");
                            }
                            System.out.println("10 -> baixar musica");
                            System.out.println("11 -> publicar musica");
                            System.out.println("12 -> partilhar musica com outro cliente");
                        }
                        input = scan.nextLine();
                        switch (input) {
                            case "-1":{
                                try{
                                    client_console.sendMsg(user_name, "test notification");
                                }catch (Exception e) {
                                    System.out.println("Exception in main: " + e);
                                }

                                break;
                            }
                            case "0": {
                                exit_state = true;
                                client_console.unsubscribe(user_name);
                                System.exit(1);
                                return;
                            }

                            case "1": {//login--------------
                                if(!logged){
                                    String username, password = "";
                                    System.out.println("Nome de Utilizador:");
                                    username = scan.nextLine();
                                    user_name = username;
                                    System.out.println("Palavra Chave:");
                                    password = scan.nextLine();
                                    int temp = client_console.login(username, password);
                                    if (temp == 0){
                                        logged = false;
                                        System.out.println("Nao entrou!");
                                    }
                                    else{
                                        System.out.println("subscripting");
                                        client_console c = new client_console();
                                        System.out.println("cp");
                                        client_console.subscribe(user_name, c);
                                        System.out.println("sent subscription to server");
                                        logged = true;
                                        System.out.println("Entrou!");
                                        if(temp%10 == 1){
                                            editor = true;
                                        }
                                    }
                                }else{//logout--------------------
                                    logged = false;
                                    editor = false;
                                }break;
                            }
                            case "2": {//registar-----------------
                                if(!logged) {
                                    user newuser = new user();

                                    System.out.println("Num de cc:");
                                    int num_cc = scan.nextInt();
                                    newuser.setNum_cc(num_cc);
                                    scan.nextLine();
                                    System.out.println("Nome:");
                                    newuser.setNome(scan.nextLine());

                                    System.out.println("UserName:");
                                    newuser.setUsername(scan.nextLine());

                                    System.out.println("Password:");
                                    newuser.setPassword(scan.nextLine());

                                    System.out.println("idade:");
                                    newuser.setIdade(scan.nextInt());

                                    System.out.println("numero de telefone:");
                                    newuser.setPhone_num(scan.nextInt());
                                    scan.nextLine();
                                    System.out.println("Endereço:");
                                    newuser.setAddress(scan.nextLine());
                                    System.out.println("permissoes de editor?[y/n]");
                                    if (scan.nextLine().equals("y"))
                                        newuser.setEditor(true);
                                    else newuser.setEditor(false);
                                    boolean status = false;
                                    String str = newuser.pacote_String();
                                    try {
                                        System.out.println("trying to comunicate with rmi");
                                        status = client_console.send_all_return_bool(str);
                                        System.out.println("Registo com sucesso!");

                                    } catch (RemoteException ex) {
                                        Logger.getLogger(client_console.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    if (!status)
                                        System.out.println("Erro no registo da nova unidade, por favor repita o processo");

                                }else{//procurar musicas-----------------
                                    System.out.println("Procurar por:\n 1-Nome do artista\n 2-album\n 3-genero musical");
                                    int num_cc = scan.nextInt();
                                    scan.nextLine();
                                    String search = "";
                                    //artista/genero/album
                                    if(num_cc == 1) search = "artista";
                                    else if(num_cc == 2) search = "album";
                                    else if(num_cc == 3) search = "genero";
                                    else{
                                        System.out.println("erro");
                                        break;
                                    }
                                    scan.nextLine();
                                    System.out.println("escreva aqui:");
                                    String str = new String ("request;music_search;"+user_name+";"+search+";"+scan.nextLine());
                                    System.out.println("sent>> "+ str);
                                    String arr[] = (client_console.send_all_return_str(str)).split(";");
                                    for(int i = 0; i<Integer.parseInt(arr[3]);i++){
                                        System.out.println(i+arr[3+i]);
                                    }
                                    System.out.println("clique em qualquer tecla para avançar");
                                    scan.nextLine();
                                }break;
                            }
                            case "3":{//detalhes de albuns---------------
                                if(!logged){
                                    break;
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



        while (true) {

            if(exit_state == true){
                System.out.println("saindo");
                System.exit(1);
                return;
            }
        }
    }
}
