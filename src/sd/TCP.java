package sd;

import java.io.*;
import java.util.ArrayList;

public class TCP {

    public static void downloadFile(String path, ArrayList<File> musicas, String filename, InputStream in) throws IOException {
        System.out.println("Downloading");
        File f = new File(path + "/" + filename);
        int fileno = 1;
        while (f.exists()){
            f.renameTo(new File(path + "/ (" + fileno + ")" + filename ));
            fileno++;
        }
        OutputStream fout = new FileOutputStream(f, true);
        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1) {
            fout.write(buffer);
        }
        fout.close();
        in.close();

        musicas.add(f);
    }

    public static void uploadFile(File file, OutputStream out) throws IOException{
        System.out.println("Uploading");
        FileInputStream fin = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        while (fin.read(buffer) != -1) {
            out.write(buffer);
        }
        out.close();
        fin.close();
    }
}
