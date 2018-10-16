package sd;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class MulticastClient extends Thread {

    private ArrayList<File> musicas;
    private String musicFilePath;

    MulticastClient(){
        musicFilePath = "C:/Users/j/Desktop/musica_cliente";
        File[] temp = new File(musicFilePath).listFiles();
        System.out.println("Musicas");
        if (temp == null){
            musicas = new ArrayList<>();
        } else {
            musicas = new ArrayList<>(Arrays.asList(temp));
            for (File file : musicas) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            }
        }
    }

    public void listMusic(){
        for (File file : musicas) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }

    public static void main(String[] args) {
        MulticastClient client = new MulticastClient();
        client.start();
    }

    public void run() {
        while (true) {
            Socket socket = null;
            try {
                socket = new Socket("127.0.0.1", MulticastServer.TCP_PORT);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                Scanner sc = new Scanner(System.in);
                loop:
                while (true) {
                    System.out.println("[0] Upload\n[1] Download");

                    int temp = sc.nextInt();
                    sc.nextLine();
                    String filename;
                    switch (temp) {
                        case 0:
                            out.write(0);
                            for (int i = 0; i < musicas.size(); i++){
                                System.out.println("[" + i + "] " + musicas.get(i).getName());
                            }
                            int escolha;
                            while (true) {
                                System.out.print("Escolha uma musica para upload: ");

                                escolha = sc.nextInt();
                                if (escolha >= 0 && escolha < musicas.size()) break;
                            }
                            File f = musicas.get(escolha);
                            out.write(f.getName().getBytes());
                            TCP.uploadFile(f, out);
                            break loop;
                        case 1:
                            out.write(1);
                            while (true) {
                                System.out.print("Nome do ficheiro: ");
                                filename = sc.nextLine();
                                out.write(filename.getBytes());
                                // Se in.read == 1 encontrou ficheiro
                                if (in.read() == 1) {
                                    break;
                                }
                                System.out.println("File " + filename + " not found");
                            }
                            TCP.downloadFile(musicFilePath, musicas, filename, in);
                            listMusic();
                            break loop;
                        default:
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e){
                    System.out.println(e);
                }
            }
        }
    }
}