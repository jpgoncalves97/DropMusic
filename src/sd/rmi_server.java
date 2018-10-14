package sd;

import java.util.HashMap;
import java.util.Map;
import Classes.*;

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
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;


public class rmi_server extends UnicastRemoteObject implements rmi_interface_client{

    static ArrayList<rmi_interface_client> clientes_online;

    private rmi_server() throws RemoteException{
        super();
    }

    public void online(user user, rmi_interface_client c) throws RemoteException {
        System.out.println("Subscribing " + user.getNome());
        System.out.print("> ");
        clientes_online.add(c);
    }

    public static void main(String args[]) throws ParseException{


        try{
            rmi_server servidorRMI;
            servidorRMI = new rmi_server();
            Registry registry = LocateRegistry.createRegistry(6789);
            registry.rebind("192.1", servidorRMI);
            System.err.println("servidor rmi online ...");
        } catch(RemoteException e) {
            System.out.print("Exception in RMI Server.main: "+e);
        }
    }

    public boolean test(int n) throws RemoteException{
        return n != 0;
    }
    //#1
    @Override
    public boolean regUser(user user) {
        Map<String,String> reguser = new HashMap<String,String>();
        reguser.put("type",new String("regUser"));
        reguser.put("num_cc",Integer.toString(user.getNum_cc()));
        reguser.put("nome",user.getNome());
        reguser.put("username",user.getUsername());
        reguser.put("password",user.getPassword());
        reguser.put("idade",Integer.toString(user.getIdade()));
        reguser.put("num_phone",Integer.toString(user.getPhone_num()));
        reguser.put("address",user.getAddress());
        String temp = (user.isEditor() != false) ? "true" : "false";
        reguser.put("editor",temp);
        //enviar o reguser por multicast


        return true;
    }
    public user login(String username,String password){
        return null;
    }
    public boolean usertest(int cc){
        return false;
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
    public ArrayList<music> showsongsbyalbum(album album){
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
