package sd;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;


public interface rmi_interface_client extends Remote{

    boolean test(int n) throws RemoteException;//0=false

}
