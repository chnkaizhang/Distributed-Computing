
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class DFSSERVER {

	public static void main(String[] args) throws RemoteException {
		DFSinterface dfs=new ServerSevice();
		//implement a specific sleep sort interface
		DFSinterface skeleton = (DFSinterface) UnicastRemoteObject.exportObject(dfs, 0);
		//Transfer it to remote serve interface
		Registry registry = LocateRegistry.createRegistry(8000);
		//register a service and register it on 8000 port
        registry.rebind("DFS", skeleton);
        //register a service and name it "SleepSort".
        System.err.println("DFSServer ready");
	}

}
