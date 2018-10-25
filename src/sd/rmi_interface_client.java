package sd;


import Classes.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public interface rmi_interface_client extends Remote {

    void sendMsg(String username, String msg) throws RemoteException;//0=false

    int login(String username, String password) throws RemoteException;

    void unsubscribe(String user) throws RemoteException;

    void subscribe(String name, client_interface client) throws RemoteException;

    boolean send_all_return_bool(String str) throws RemoteException;

    String send_all_return_str(String str) throws RemoteException;

    void promove_user(String username) throws RemoteException;

    String get_online_clients() throws RemoteException;

    String send_one_return_str(String str) throws RemoteException;

    int get_tcp_port() throws RemoteException;


}
