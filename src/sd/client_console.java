package sd;

import java.io.Console;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class client_console {

    private static rmi_interface_client client_console;


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



    }



}
