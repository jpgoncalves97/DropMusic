package sd;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Scanner;

class MulticastClient extends Thread {

    public static void main(String[] args) {
        MulticastClient client = new MulticastClient();
        client.start();
    }

    public void run() {
        while (true) {
            try {
                Socket socket = new Socket("127.0.0.1", MulticastServer.TCP_PORT);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                Scanner sc = new Scanner(System.in);
                String text;
                while (true) {
                    System.out.print("Nome do ficheiro: ");
                    text = sc.nextLine();
                    out.write(text.getBytes());
                    // Se in.read == 1 encontrou ficheiro
                    if (in.read() == 1){
                        break;
                    }
                    System.out.println("File " + text + " not found");
                }
                OutputStream fout = new FileOutputStream(text, true);
                byte[] buffer = new byte[1024];
                while (in.read(buffer) != -1) {
                    fout.write(buffer);
                }
                socket.close();

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}