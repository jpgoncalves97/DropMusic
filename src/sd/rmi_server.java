package sd;


import Classes.*;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

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
    private MulticastSocket socket;

    private rmi_server() throws RemoteException, ParseException {
        test_var = 0;
        socket = MulticastServer.newMulticastSocket();
        SharedMessage msg = new SharedMessage();
        new MulticastSenderThread(socket, msg, new Runnable() {
            public void run() {
                try {
                    Scanner sc = new Scanner(System.in);
                    while (true) {

                        System.out.print("Message: ");
                        // Choose one server id
                        String input = sc.nextLine();
                        ArrayList<String> ids = getServerIds();
                        String id = ids.get(ThreadLocalRandom.current().nextInt(0, ids.size()));
                        System.out.println("Sending to id: " + id);
                        MulticastServer.sendString(socket, new String(id + ";request;echo" + input));
                        System.out.println("Received: " + MulticastServer.receiveString(socket));
                    }
                } catch (Exception e) {
                    System.out.println(e);
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
        try {
            MulticastServer.sendString(socket, new String("0;request;server_id"));
        } catch (IOException e){
            System.out.println(e);
        }

        ArrayList<String> ids = new ArrayList<>();
        try {
            socket.setSoTimeout(100);
        } catch (SocketException e) {
            System.out.println(e);
        }
        while (true) {
            String response;
            try {
                response = MulticastServer.receiveString(socket);
            } catch (IOException e){
                return ids;
            }
            if (response.contains("response")) {
                String id = response.split(";")[0];
                System.out.println("Id recebido: " + id);
                ids.add(id);
            }
        }
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
