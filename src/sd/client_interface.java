package sd;

import java.rmi.*;

public interface client_interface extends Remote {
    void notify_client(String str) throws java.rmi.RemoteException;

}
