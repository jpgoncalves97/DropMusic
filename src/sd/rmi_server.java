package sd;


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
    private int test_var;

    private rmi_server() throws RemoteException, ParseException{
        test_var = 0;
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

}
