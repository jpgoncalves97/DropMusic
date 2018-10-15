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
    private static rmi_interface_client client_console;

    client_console() throws  RemoteException{
        super();
    }
    public void notify_client() throws java.rmi.RemoteException{
        System.out.println("\nAGORA É UM EDITOR!\n");
    }

    public static void main(String args[]) throws RemoteException {
        try {

            client_console = (rmi_interface_client) LocateRegistry.getRegistry(6789).lookup("192.1");

            System.out.printf("\nConnected to server\n");
            boolean response = client_console.test(1);
            System.out.println("response: " + response);

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
                user logged = new user();
                while (true) {
                    try {
                        System.out.println("0 -> sair");
                        System.out.println("1 -> registar utilizadores");
                        System.out.println("2 -> login");
                        input = scan.nextLine();
                        switch (input) {
                            case "0": {
                                exit_state = true;
                                if(logged != null){
                                    logged.setOnline_state(false);
                                }
                                break;
                            }

                            case "1": {
                                user newuser = new user();

                                System.out.println("Num de cc:");
                                int num_cc = scan.nextInt();
                                if(client_console.usertest(num_cc) == true){
                                    System.out.println("unidade ja registada!!");
                                    break;
                                }
                                newuser.setNum_cc(num_cc);

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

                                System.out.println("Endereço:");
                                newuser.setAddress(scan.nextLine());

                                System.out.println("permissoes de editor?[y/n]");
                                if (scan.nextLine().equals("y")) newuser.setEditor(true);
                                else newuser.setEditor(false);
                                boolean status = false;
                                try {
                                    status = client_console.regUser(newuser);
                                    System.out.println("Registo com sucesso!");

                                } catch (RemoteException ex) {
                                    Logger.getLogger(client_console.class.getName()).log(Level.SEVERE, null, ex);
                                }if (status == false){
                                    System.out.print("Erro no registo da nova unidade");
                                }
                                break;
                            }
                            case "2": {
                                String username, password = "";
                                System.out.println("Username:");
                                username = scan.nextLine();
                                System.out.println("Password:");
                                password = scan.nextLine();
                                logged = client_console.login(username, password);
                                if(logged != null){
                                    logged.setOnline_state(true);
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
                System.exit(1);
            }
        }
    }
}
