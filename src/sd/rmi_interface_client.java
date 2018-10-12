package sd;


import Classes.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;


public interface rmi_interface_client extends Remote {

    boolean test(int n) throws RemoteException;//0=false

    //#1
    boolean regUser(int permissions, String username, String password, String nome, int phone_num, String address, String num_cc) throws RemoteException;

    //#2 gerir artistas, albuns e musicas

    //#3 & 4
    ArrayList<music> showallsongs() throws RemoteException;
    ArrayList<music> showsongsbygenre(String genero) throws RemoteException;
    ArrayList<music> showsongsbyalbum(album album) throws RemoteException;
    ArrayList<author> showallauthors() throws RemoteException;
    ArrayList<album> showallalbuns() throws RemoteException;
    ArrayList<String> showallgenres() throws RemoteException;

    //#5
    boolean writecomment(album album, user user, int points, String comment) throws RemoteException;

    //#6
    boolean giverights(user user) throws RemoteException;

    //#7 & #8 NOTIFICATIONS and #9 delay notifications

    //#10/#12 upload/download files to/from multicast servers

    //#11 share song to user
    boolean partilha(music musica, user user) throws RemoteException;

}
