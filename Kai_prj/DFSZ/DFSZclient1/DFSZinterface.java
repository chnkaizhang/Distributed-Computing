
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.apache.zookeeper.KeeperException;

public interface DFSZinterface extends Remote{

	public String create(String fileName) throws RemoteException;
	public String write(String fileName, int size) throws RemoteException;
	public String read(String fileName) throws RemoteException;
	public void delete(String fileName) throws RemoteException, InterruptedException, KeeperException;
	public void initialized(int clientID, String IP, int port, String IP1, String IP2, String IP3, int port1, int port2, int port3) throws RemoteException, NotBoundException, Exception ;
	public void updateConfig(String iP1, String iP2, String iP3, int port1, int port2, int port3) throws RemoteException, Exception;
	public String createS2S(String fileName) throws RemoteException, NotBoundException;
	public String writeS2S(String fileName, int size) throws RemoteException, NotBoundException, KeeperException, InterruptedException;
	public void writeCopy(String fileName, String content) throws RemoteException, KeeperException, InterruptedException;
	public void updateHashtable(String fileName, int id, int idnext,boolean index) throws RemoteException;
	public String deleteS2S(String fileName) throws RemoteException, KeeperException, InterruptedException;
	
}
