package sd;


import Classes.*;

import javax.sound.midi.SysexMessage;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
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
        });

    }

    public static void main(String args[]) throws ParseException {


        try {
            rmi_server servidorRMI;
            servidorRMI = new rmi_server();
            Registry registry = LocateRegistry.createRegistry(6789);
            registry.bind("192.1", servidorRMI);
            System.err.println("servidor rmi online ...");
        } catch (RemoteException e) {
            System.out.println("Exception in RMI Server.main: " + e);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            main(args);
        } catch (AlreadyBoundException e) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            main(args);
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
        System.out.println("notification sent");
        try {
            int index = usernames.indexOf(username);
            System.out.println(index);
            clientes.get(usernames.indexOf(username)).notify_client(msg);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
    }

    @Override
    public void unsubscribe(String user) throws RemoteException {
        int index = usernames.indexOf(user);
        System.out.println("o client " + user + " desligou-se");
        send_all("request;logout;" + user);
        if (index != -1) {
            clientes.remove(index);
            usernames.remove(index);
        }
    }

    @Override
    public void subscribe(String username, client_interface c, boolean editor) throws RemoteException {
        System.out.println("new client");
        this.clientes.add(c);
        this.usernames.add(username);
        String[] notificacao = send_one_return_str("request;notification;" + username).split(";");
        for (String n : notificacao)
            System.out.println(n);
        if (notificacao.length >= 5) {
            sendMsg(username, notificacao[4]);
        }
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

    public int get_tcp_port() {
        String str = send_one_return_str("request;tcp_port");
        return Integer.parseInt(str.split(";")[3]);
    }

    @Override
    public int login(String username, String password) throws RemoteException {
        System.out.println("LOGIN");
        SharedMessage msg = new SharedMessage();
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("Sending to all");
                    System.out.println("sent: 0;request;login;" + username + ";" + password);
                    MulticastServer.sendString(socket, new String("0;request;login;" + username + ";" + password));
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
            if (temp.length == 3) return 0;
            if (temp[3].equals("false")) {
                return 0;
            } else {
                if (temp[4].equals("true")) {
                    return 11;
                } else {
                    return 10;
                }

            }

        }
    }

    @Override
    public String get_online_clients() {
        String str = "";
        for (int i = 0; i < usernames.size(); i++) {
            str += usernames.get(i) + ";";
        }
        return str;
    }

    public void send_all(String str) {
        System.out.println("SEND ALL");
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("Sending to all");
                    MulticastServer.sendString(socket, "0;" + str);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();

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

                    MulticastServer.sendString(socket, "0;" + str);
                    String response;
                    do {
                        response = MulticastServer.receiveString(socket);
                        System.out.println(response);
                        String arr[] = response.split(";");
                    } while (response.contains("request"));
                    System.out.println("Received: " + response);

                    synchronized (msg) {
                        msg.setMsg(response);
                        msg.notify();
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
                System.out.println("exit");
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

    public String send_one_return_str(String str) {
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

    public void promove_user(String username) {
        //se estiver online recebe notificação
        System.out.println("promove_user");
        try {
            if (usernames.contains(username)) {
                String notify = send_one_return_str("request;notification;" + username);
                String arr[] = notify.split(";");
                sendMsg(username, arr[4]);
                System.out.println("o " + username + " foi notificado");
            } else {
                System.out.println("o " + username + " nao foi notificado, porque nao esta online");
            }
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
    }

    public void notify(String msg) {
        for (int i = 0; i < clientes.size(); i++) {
            String[] notify = send_all_return_str("request;notification;" + usernames.get(i)).split(";");
            try {
                if (notify.length == 5) {
                    sendMsg(usernames.get(i), notify[4]);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
