
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class DFSZSERVER {

	public static void main(String[] args) throws RemoteException {
		DFSZinterface dfs=new ZKSevice();
		//implement a specific sleep sort interface
		DFSZinterface skeleton = (DFSZinterface) UnicastRemoteObject.exportObject(dfs, 0);
		//Transfer it to remote serve interface
		Registry registry = LocateRegistry.createRegistry(8000);
		//register a service and register it on 8000 port
        registry.rebind("DFS", skeleton);
        //register a service and name it "SleepSort".
        System.err.println("DFSServer ready");
	}

}
