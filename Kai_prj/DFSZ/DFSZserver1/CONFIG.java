
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class CONFIG {
	
	public int port1;
	public int port2;
	public int port3;
	
	Map<String, List<Integer>> map=new HashMap<>();
	public List<String> IPlist=new ArrayList<>();
	public List<Integer> portlist=new ArrayList<>();
	
	public CONFIG(String IP1,String IP2,String IP3,int port1, int port2, int port3){
		
		IPlist.add(IP1);
		IPlist.add(IP2);
		IPlist.add(IP3);
		this.port1=port1;
		this.port2=port2;
		this.port3=port3;
		portlist.add(port1);
		portlist.add(port2);
		portlist.add(port3);
		//this.map=new HashMap<>();
		
	}
	
	public CONFIG() {
		
	}
	
	
//	public List<Integer> query(int id, String filename){
//		List<Integer> servers=map.get(filename);
//		int ID=servers.get(0);
//		
//		Registry registry = LocateRegistry.getRegistry(IP,port);
//		return null;
//		
//	}

	
}
