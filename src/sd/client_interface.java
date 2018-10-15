package sd;

import java.rmi.Remote;

public interface client_interface extends Remote {
    void notify_client() throws java.rmi.RemoteException;

}
