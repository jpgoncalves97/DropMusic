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

    public void sendMsg(String username, String msg) throws RemoteException {

        System.out.println("testing notify");
        try {
            int index = usernames.indexOf(username);
            System.out.println(index);
            clientes.get(usernames.indexOf(username)).notify_client(msg);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
    }

    @Override
    public void unsubscribe(String user) throws RemoteException{
        int index = usernames.indexOf(user);
        System.out.println("o client "+user+"desligou-se");
        send_all_return_str("request;logout;"+user);
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
    public boolean send_all_return_bool(String str) {
        System.out.println("SEND ALL RETURN BOOL");
        final SharedMessage msg = new SharedMessage();
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("Sending to all");
                    MulticastServer.sendString(socket, "0;" + str);
                    System.out.println(str);
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
            String str1 = msg.getMsg();
            System.out.println(str1);
            if (str1.contains("true")) {
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

    @Override
    public String get_online_clients(){
        String str = "";
        for(int i = 0 ; i < usernames.size();i++){
            str += usernames.get(i) + ";";
        }
        return str;
    }

    public String send_all_return_str(String str) {
        System.out.println("SEND ALL RETURN STR");
        SharedMessage msg = new SharedMessage();
        new Thread(new Runnable() {
            public void run() {
                try {
                    ArrayList<String> ids = getServerIds();
                    String id = ids.get(ThreadLocalRandom.current().nextInt(0, ids.size()));
                    System.out.println("Sending to all");

                    MulticastServer.sendString(socket,  "0;" + str);
                    String response;
                    do {
                        response = MulticastServer.receiveString(socket);
                        System.out.println(response);
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

        return getString(msg);

    }

    private String getString(SharedMessage msg) {
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

    public String send_one_return_str(String str){
        System.out.println("SEND ONE RETURN STR");
        SharedMessage msg = new SharedMessage();
        new Thread(new Runnable() {
            public void run() {
                try {
                    ArrayList<String> ids = getServerIds();
                    String id = ids.get(ThreadLocalRandom.current().nextInt(0, ids.size()));
                    System.out.println("Sending to id: " + id);

                    MulticastServer.sendString(socket, id + ";" + str);
                    String response;
                    do {
                        response = MulticastServer.receiveString(socket);
                        System.out.println(response);
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

        return getString(msg);
    }

}
