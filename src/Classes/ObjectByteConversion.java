package Classes;

import java.io.*;

public final class ObjectByteConversion {

    public static byte[] ObjectToByteArray(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ObjectOutput out;
        byte[] byteArray = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.flush();
            byteArray = bos.toByteArray();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static Object byteArrayToObject(byte[] byteArray){
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return o;
    }
}
