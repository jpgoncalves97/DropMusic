package sd;


import Classes.*;

import java.io.*;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
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
                /*Scanner sc = new Scanner(System.in);
                InetAddress group;
                try {
                    group = InetAddress.getByName(MulticastServer.MULTICAST_ADDRESS);
                    while (true) {
                        System.out.println("[1] Registar ");
                        System.out.println("Escolha uma opção: ");
                        String m;
                        try {
                            byte[] buffer;
                            synchronized (msg) {
                                if (msg.isNull()) {
                                    try {
                                        msg.wait();
                                    } catch (InterruptedException e) {
                                        System.out.println("Thread interrompida: " + e);
                                    }
                                }
                                m = id + ";" + msg.getMsg();
                                buffer = m.getBytes();
                            }
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MulticastServer.PORT);
                            socket.send(packet);
                            System.out.println("Sending response: " + m);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }*/

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
