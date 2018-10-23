package sd;


import Classes.*;

import javax.sound.midi.SysexMessage;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ThreadLocalRandom;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


public class rmi_server extends UnicastRemoteObject implements rmi_interface_client {
    private int test_var;
    private MulticastSocket socket;
    private ArrayList<client_interface> clientes = new ArrayList<>();
    private ArrayList<String> usernames = new ArrayList<>();

    public class SharedMessage {
        private String msg;

        SharedMessage() {
            msg = "";
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public boolean isNull() {
            return msg.equals("");
        }

    }

    private rmi_server() throws RemoteException, ParseException {
        test_var = 0;
        socket = MulticastServer.newMulticastSocket();
        new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(System.in);
                while (true) {
                    try {
                        System.out.print("Message: ");
                        // Choose one server id
                        String input = sc.nextLine();
                        ArrayList<String> ids = getServerIds();
                        String id = ids.get(ThreadLocalRandom.current().nextInt(0, ids.size()));
                        System.out.println("Sending to id: " + id);
                        MulticastServer.sendString(socket, new String(id + ";" + input));
                        String response;
                        do {
                            response = MulticastServer.receiveString(socket);
                        } while (response.contains("request"));
                        System.out.println("Received: " + response);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

            }
        }).start();

    }

    public static void main(String args[]) throws ParseException {


        try {
            rmi_server servidorRMI;
            servidorRMI = new rmi_server();
            Registry registry = LocateRegistry.createRegistry(6789);
            registry.rebind("192.1", servidorRMI);
            System.err.println("servidor rmi online ...");
        } catch (RemoteException e) {
            System.out.print("Exception in RMI Server.main: " + e);
        }


    }

    public ArrayList getServerIds() {
        MulticastServer.sendString(socket, new String("0;request;server_id"));
        ArrayList<String> ids = new ArrayList<>();
        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            System.out.println(e);
        }
        while (true) {
            String response;
            try {
                response = MulticastServer.receiveString(socket);
            } catch (IOException e) {
                return ids;
            }
            if (response.contains("response")) {
                String id = response.split(";")[0];
                System.out.println("Id recebido: " + id);
                ids.add(id);
            }
        }
    }

    public boolean test(String username) throws RemoteException {

        System.out.println("testing notify");
        try {
            int index = usernames.indexOf(username);
            System.out.println(index);
            clientes.get(usernames.indexOf(username)).notify_client("notify test to " + username);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
        return true;
    }

    @Override
    public void unsubscribe(String user) throws RemoteException{
        int index = usernames.indexOf(user);
        System.out.println("o client "+user+"desligou-se");
        if(index != -1) {
            clientes.remove(index);
            usernames.remove(index);
        }
    }
    
    @Override
    public void subscribe(String username, client_interface c) throws RemoteException {
        System.out.println("new client");
        this.clientes.add(c);
        this.usernames.add(username);
    }

    //#1
    @Override
    public boolean send_all_return_bool(String newuser) {

        final SharedMessage msg = new SharedMessage();
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("Sending to all");
                    MulticastServer.sendString(socket, "0;request;register;" + newuser);
                    System.out.println(newuser);
                    String response;
                    do {
                        response = MulticastServer.receiveString(socket);
                        //System.out.println(response);
                    } while (response.contains("request"));
                    System.out.println("Received: " + response);

                    synchronized (msg) {
                        msg.setMsg(response);
                        msg.notify();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }).start();

        synchronized (msg) {
            if (msg.isNull()) {
                try {
                    msg.wait();
                } catch (InterruptedException e) {
                    System.out.println("erro");
                }
            }
            String str = msg.getMsg();
            System.out.println(str);
            if (str.contains("true")) {

                return true;
            } else {
                return false;
            }
        }
        //return true;
    }

    @Override
    public int login(String username, String password) throws RemoteException {
        System.out.println("LOGIN");
        SharedMessage msg = new SharedMessage();
        new Thread(new Runnable() {
            public void run() {
                try {
                    ArrayList<String> ids = getServerIds();
                    String id = ids.get(ThreadLocalRandom.current().nextInt(0, ids.size()));
                    System.out.println("Sending to id: " + id);
                    MulticastServer.sendString(socket, new String(id + ";request;login;" + username + ";" + password));
                    String response;
                    do {
                        response = MulticastServer.receiveString(socket);
                        //System.out.println(response);
                    } while (response.contains("request"));
                    System.out.println("Received: " + response);

                    synchronized (msg) {
                        msg.setMsg(response);
                        msg.notify();
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();

        synchronized (msg) {
            if (msg.isNull()) {
                try {
                    msg.wait();
                } catch (InterruptedException e) {
                    System.out.println("erro");
                }
            }
            String str = msg.getMsg();
            String[] temp = str.split(";");
            if (temp[3].equals("false")) {
                return 0;
            } else {
                if (temp[4].equals("true")) return 11;
                else return 10;

            }

        }
    }


    public String send_all_return_str(String str) {

        SharedMessage msg = new SharedMessage();
        new Thread(new Runnable() {
            public void run() {
                try {
                    ArrayList<String> ids = getServerIds();
                    String id = ids.get(ThreadLocalRandom.current().nextInt(0, ids.size()));
                    System.out.println("Sending to id: " + id);

                    MulticastServer.sendString(socket, "request;" + str);
                    String response;
                    do {
                        response = MulticastServer.receiveString(socket);
                        System.out.println(response);
                    } while (!response.contains("rmi"));
                    System.out.println("Received: " + response);

                    synchronized (msg) {
                        msg.setMsg(response);
                        msg.notify();
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();

        synchronized (msg) {
            if (msg.isNull()) {
                try {
                    msg.wait();
                } catch (InterruptedException e) {
                    System.out.println("erro");
                }
            }
            return msg.getMsg();
        }

    }

}
