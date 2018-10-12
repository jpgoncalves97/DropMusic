package sd;

import Classes.user;

import java.net.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;

import static java.net.NetworkInterface.getNetworkInterfaces;

class MulticastServer extends Thread {

    protected static String MULTICAST_ADDRESS = "224.0.224.0";
    protected static int PORT = 4321;
    protected long id;

    public MulticastServer() {
        super();
        id = System.currentTimeMillis();
        System.out.println("Server id# " + id);
        newConnection();
    }

    public static void main(String[] args) {
        new MulticastServer();
    }

    public static void newConnection(){
        try {
            MulticastSocket socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            new MulticastReceiverThread(socket).start();
            new MulticastSenderThread(socket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class MulticastReceiverThread extends Thread {

    private MulticastSocket socket;

    MulticastReceiverThread(MulticastSocket socket) {
        this.socket = socket;
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String[] msg = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(";");
                for (String s : msg) {
                    System.out.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getHash(){
        return this.hashCode();
    }
}

class MulticastSenderThread extends Thread {

    private MulticastSocket socket;

    MulticastSenderThread(MulticastSocket socket) {
        this.socket = socket;
    }

    public void run() {
        Scanner keyboardScanner = new Scanner(System.in);
        InetAddress group;
        try {
            group = InetAddress.getByName(MulticastServer.MULTICAST_ADDRESS);
            while (true) {
                try {
                    String readKeyboard = keyboardScanner.nextLine();
                    byte[] buffer = readKeyboard.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MulticastServer.PORT);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public int getHash(){
        return this.hashCode();
    }
}

