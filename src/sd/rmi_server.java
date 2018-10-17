package sd;


import Classes.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ThreadLocalRandom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class rmi_server extends UnicastRemoteObject implements rmi_interface_client {
    private int test_var;

    private rmi_server() throws RemoteException, ParseException {
        test_var = 0;
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

        MulticastSocket socket = MulticastServer.newMulticastSocket();
        SharedMessage msg = new SharedMessage();
        new MulticastSenderThread(socket, msg, new Runnable() {
            public void run() {
                try {
                    Scanner sc = new Scanner(System.in);
                    InetAddress group;
                    group = InetAddress.getByName(MulticastServer.MULTICAST_ADDRESS);
                    while (true) {

                        System.out.print("Message: ");
                        // Choose one server id
                        String input = sc.nextLine();
                        String msg = "0;request;server_id";
                        {
                            byte[] buffer = msg.getBytes();
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MulticastServer.PORT);
                            socket.send(packet);
                        }
                        socket.setSoTimeout(100);
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        ArrayList<String> ids = new ArrayList<>();
                        while (true) {
                            try {
                                socket.receive(packet);
                                if (MulticastServer.packetToString(packet).contains("response")) {
                                    System.out.println("Id recebido: " + MulticastServer.packetToString(packet).split(";")[0]);
                                    ids.add(MulticastServer.packetToString(packet).split(";")[0]);
                                }
                            } catch (SocketTimeoutException e) {
                                break;
                            }
                        }
                        System.out.println("Id list");
                        for (int i = 0; i < ids.size(); i++){
                            System.out.println(ids.get(i));
                        }
                        {
                            String id = ids.get(ThreadLocalRandom.current().nextInt(0, ids.size()));
                            System.out.println("Sending to id: " + id);
                            byte[] buffer1 = new String(id + ";request;echo" + input).getBytes();
                            DatagramPacket packet1 = new DatagramPacket(buffer1, buffer1.length, group, MulticastServer.PORT);
                            socket.send(packet1);
                            System.out.println("Sent packet");
                        }
                        byte[] buf = new byte[1024];
                        DatagramPacket p = new DatagramPacket(buf, buf.length, group, MulticastServer.PORT);
                        socket.receive(p);
                        System.out.println("Received: " + MulticastServer.packetToString(p));
                        ids = new ArrayList<>();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();
    }

    public boolean test(int n) throws RemoteException {
        return n != 0;
    }

    //#1
    public boolean usertest(int cc) {
        return true;
    }

    @Override
    public boolean regUser(user newuser) {
        return false;
    }

    @Override
    public user login(String username, String password) {
        return null;
    }

    @Override
    public void online(user user, rmi_interface_client c) {

    }

    //#3 & 4
    @Override
    public ArrayList<album> showallalbuns() {
        return null;
    }

    @Override
    public ArrayList<author> showallauthors() {
        return null;
    }

    @Override
    public ArrayList<String> showallgenres() {
        return null;
    }

    @Override
    public ArrayList<music> showallsongs() {
        return null;
    }

    @Override
    public ArrayList<music> showsongsbygenre(String genero) {
        return null;
    }

    @Override
    public ArrayList<music> showsongsbyalbum(album album) {
        return null;
    }

    //#5
    @Override
    public boolean writecomment(album album, user user, int points, String comment) {
        return false;
    }

    //#6
    @Override
    public boolean giverights(user user) {
        return false;
    }

    //#11
    @Override
    public boolean partilha(music musica, user user) {
        return false;
    }


}
