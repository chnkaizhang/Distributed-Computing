
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZKSevice implements DFSZinterface{
	
	public int id=1;
	public CONFIG config=new CONFIG();
	BaseZookeeper zookeeper = new BaseZookeeper();
	
	public ZKSevice() {
		
	}
	
	public void initialized(int clientID, String IP,int port,String IP1,String IP2,String IP3, int port1,int port2,int port3) throws Exception {
		System.out.print("Server "+id+": Connecting client"+clientID+"\n");
		if(config.IPlist.isEmpty()) {
			updateConfig(IP1,IP2,IP3,port1,port2,port3);
			if(IP.equals(IP1)&&port==port1) {
				try {
					Registry regiS2S1 = LocateRegistry.getRegistry(IP2,port2);
					DFSZinterface stub = (DFSZinterface) regiS2S1.lookup("DFS");
					stub.updateConfig(IP1,IP2,IP3,port1,port2,port3);		
				} catch (RemoteException e) {
					System.out.print("Server 2 cannot be connected!");
				} catch (NotBoundException e1) {
					System.out.print("\n");
				}
				try {
					Registry regiS2S2 = LocateRegistry.getRegistry(IP3,port3);
					DFSZinterface stub2 = (DFSZinterface) regiS2S2.lookup("DFS");
					stub2.updateConfig(IP1,IP2,IP3,port1,port2,port3);	
				} catch (RemoteException e2) {
					System.out.print("Server 3 cannot be connected!");
				} catch (NotBoundException e3) {
					System.out.print("\n");
				}
				
			}else if(IP.equals(IP2)&&port==port2) {
				try {
					Registry regiS2S1 = LocateRegistry.getRegistry(IP1,port1);
					DFSZinterface stub = (DFSZinterface) regiS2S1.lookup("DFS");
					stub.updateConfig(IP1,IP2,IP3,port1,port2,port3);	
					// Registry regiS2S2 = LocateRegistry.getRegistry(IP3,port3);
					// DFSinterface stub2 = (DFSinterface) regiS2S2.lookup("DFS");
					// stub2.updateConfig(IP1,IP2,IP3,port1,port2,port3);	
				} catch (RemoteException e) {
					System.out.print("Server 1 cannot be connected!");
				} catch (NotBoundException e) {
					System.out.print("\n");
				}

				try {
					Registry regiS2S2 = LocateRegistry.getRegistry(IP3,port3);
					DFSZinterface stub2 = (DFSZinterface) regiS2S2.lookup("DFS");
					stub2.updateConfig(IP1,IP2,IP3,port1,port2,port3);	
				} catch (RemoteException e) {
					System.out.print("Server 3 cannot be connected!");
				} catch (NotBoundException e) {
					System.out.print("\n");
				}
			}else if(IP.equals(IP3)&&port==port3){
				try {
					Registry regiS2S1 = LocateRegistry.getRegistry(IP1,port1);
					DFSZinterface stub = (DFSZinterface) regiS2S1.lookup("DFS");
					stub.updateConfig(IP1,IP2,IP3,port1,port2,port3);		
				} catch (RemoteException e) {
					System.out.print("Server 1 cannot be connected!");
				} catch (NotBoundException e) {
					System.out.print("\n");
				}
				try {
					Registry regiS2S2 = LocateRegistry.getRegistry(IP2,port2);
					DFSZinterface stub2 = (DFSZinterface) regiS2S2.lookup("DFS");
					stub2.updateConfig(IP1,IP2,IP3,port1,port2,port3);	
				} catch (RemoteException e) {
					System.out.print("abc");
					System.out.print("Server 2 cannot be connected!");
				} catch (NotBoundException e) {
					System.out.print("\n");
				}
			}
		}
	}
	
	public void updateConfig(String IP1,String IP2,String IP3,int port1, int port2, int port3) throws Exception {
		this.config=new CONFIG(IP1,IP2,IP3,port1,port2,port3);
		System.out.print("Server "+id+" finished initialization!"+"\n");
		zookeeper.connectZookeeper("localhost");
	}
	
	public String createS2S(String fileName) throws RemoteException, NotBoundException {
		String res=create(fileName);
		int idnext=id+1;
		if(idnext>3) {
			idnext=idnext%3;
		}
		int portnext=8000+id%3;
		String IPnext=config.IPlist.get(id%3);
		updateHashtable(fileName, id, idnext,true);
		for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
			List<Integer> servers=entry.getValue();
			System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
		}
		
		Registry regiS2S = LocateRegistry.getRegistry(IPnext,portnext);
		DFSZinterface stub = (DFSZinterface) regiS2S.lookup("DFS");
		stub.create(fileName);
		stub.updateHashtable(fileName,id,idnext,true);
		String IPthird=config.IPlist.get((id+1)%3);
		int portthird=config.portlist.get((id+1)%3);
		Registry regiS2S2 = LocateRegistry.getRegistry(IPthird, portthird);
		DFSZinterface stub2 = (DFSZinterface) regiS2S2.lookup("DFS");
		stub2.updateHashtable(fileName, id, idnext,true);
		return "File created successfully.";
	}
	
	public void updateHashtable(String fileName, int id1, int id2,boolean index) throws RemoteException{
		if(index==true) {
			List<Integer> ids=new ArrayList<>();
			ids.add(id1);
			ids.add(id2);
			this.config.map.put(fileName, ids);
		}else {
			this.config.map.remove(fileName);
		}
		
	}

	public String create(String fileName) throws RemoteException{
		try {
			// znode path
			String path = "/"+fileName; // Assign path to znode
			// data in byte array
			String data = ""; // Declare data
			zookeeper.createNode(path,data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "File created successfully.";
	}
	
	public String writeS2S(String fileName,int size) throws RemoteException, NotBoundException, KeeperException, InterruptedException {
		String content=write(fileName,size);
		for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
			List<Integer> servers=entry.getValue();
			System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
		}
		int idnext=(id+1)%3;
		int portnext=8000+id%3;
		String IPnext=config.IPlist.get(id%3);
		Registry regiS2S = LocateRegistry.getRegistry(IPnext,portnext);
		DFSZinterface stub = (DFSZinterface) regiS2S.lookup("DFS");
		stub.writeCopy(fileName,content);
		return "Successfully wrote to the file.";
	}
	
	public void writeCopy(String fileName,String content) throws RemoteException, KeeperException, InterruptedException{
		
			// znode path
			String path = "/"+fileName; // Assign path to znode
			// data in byte array
			String data = content; // Declare data
			//zookeeper.connectZookeeper("localhost");
			zookeeper.setData(path, data);
		
	}
	
	public String write(String fileName, int size) throws RemoteException{
		String content=getRandomString(size);
		try {
			// znode path
			String path = "/"+fileName; // Assign path to znode
			// data in byte array
			String data = content; // Declare data
			zookeeper.connectZookeeper("localhost");
			zookeeper.setData(path, data);
			return content;
		} catch (Exception e) {
			return "Failed wrote to the file.";
		}
	}
	
	public String read(String fileName) {
		String path = "/"+fileName;
		try {
			//zookeeper.connectZookeeper("localhost");
			String res=zookeeper.getData(path);
			return res;
		} catch (KeeperException e) {
			List<Integer> existServer=config.map.get(fileName);
			String IP=config.IPlist.get(existServer.get(0)-1);
			int port=config.portlist.get(existServer.get(0)-1);
			String IP2=config.IPlist.get(existServer.get(0));
			int port2=config.portlist.get(existServer.get(0));
			Registry regiS2S;
			try {
				regiS2S = LocateRegistry.getRegistry(IP,port);
				DFSZinterface stub = (DFSZinterface) regiS2S.lookup("DFS");
				String str=stub.read(fileName);
				for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
					List<Integer> servers=entry.getValue();
					System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
				}
				return str;
			} catch (RemoteException e2) {
				try {
					regiS2S = LocateRegistry.getRegistry(IP2,port2);
					DFSZinterface stub = (DFSZinterface) regiS2S.lookup("DFS");
					String str=stub.read(fileName);
					for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
						List<Integer> servers=entry.getValue();
						System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
					}
					return str;
				} catch (RemoteException e1) {
					return "error";
				} catch (NotBoundException e3) {
					return "error";
				}
			} catch (NotBoundException e4) {
				return "error";
			}
		} catch (InterruptedException e) {
			return "error";
		} 
	}
	
	public String deleteS2S(String fileName) throws RemoteException, KeeperException, InterruptedException{
		String path="/"+fileName;
		Stat stat=zookeeper.znode_exists(path);
		if(stat!=null) {
			delete(fileName);
			List<Integer> existServer=config.map.get(fileName);
			if(existServer.get(0)==id) {
				String IP=config.IPlist.get(id%3);
				int port=config.portlist.get(id%3);
				Registry regiS2S = LocateRegistry.getRegistry(IP,port);
				try {
					DFSZinterface stub = (DFSZinterface) regiS2S.lookup("DFS");
					stub.delete(fileName);
					stub.updateHashtable(fileName, 0, 0, false);
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
				Registry regiS2S2 = LocateRegistry.getRegistry(config.IPlist.get((id+1)%3),config.portlist.get((id+1)%3));
				try {
					DFSZinterface stub = (DFSZinterface) regiS2S2.lookup("DFS");
					stub.updateHashtable(fileName, 0, 0, false);
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}else {
				String IP=config.IPlist.get((id+1)%3);
				int port=config.portlist.get((id+1)%3);
				Registry regiS2S = LocateRegistry.getRegistry(IP,port);
				try {
					DFSZinterface stub = (DFSZinterface) regiS2S.lookup("DFS");
					stub.delete(fileName);
					stub.updateHashtable(fileName, 0, 0, false);
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
				Registry regiS2S2 = LocateRegistry.getRegistry(config.IPlist.get(id%3),config.portlist.get(id%3));
				try {
					DFSZinterface stub = (DFSZinterface) regiS2S2.lookup("DFS");
					stub.updateHashtable(fileName, 0, 0, false);
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}
			updateHashtable(fileName, 0, 0, false);
			
			for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
				List<Integer> servers=entry.getValue();
				System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
			}
		}else {
			String IP=config.IPlist.get(id%3);
			int port=config.portlist.get(id%3);
			String IP2=config.IPlist.get((id+1)%3);
			int port2=config.portlist.get((id+1)%3);
			try {
				Registry regiS2S = LocateRegistry.getRegistry(IP,port);
				DFSZinterface stub = (DFSZinterface) regiS2S.lookup("DFS");
				stub.delete(fileName);
				stub.updateHashtable(fileName, 0, 0, false);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			try {
				Registry regiS2S = LocateRegistry.getRegistry(IP2,port2);
				DFSZinterface stub = (DFSZinterface) regiS2S.lookup("DFS");
				stub.delete(fileName);
				stub.updateHashtable(fileName, 0, 0, false);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}
		return "Successfully delete!";
	}
	
	public void delete(String fileName) throws RemoteException, InterruptedException, KeeperException{
		String path="/"+fileName;
		zookeeper.deleteNode(path);
	}
	
	public static String getRandomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
}

class BaseZookeeper implements Watcher{
	
	private ZooKeeper zookeeper;
	private static final int SESSION_TIME_OUT = 2000;
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			System.out.println("Watch received event");
			countDownLatch.countDown();
		}
	}
	
	public void connectZookeeper(String host) throws Exception{
		//connect zookeeper server
		zookeeper = new ZooKeeper(host, SESSION_TIME_OUT, this);
		countDownLatch.await();
		System.out.println("zookeeper connection success");
	}

	public String createNode(String path,String data) throws Exception{
		//create znode
		return this.zookeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	public Stat znode_exists(String path) throws KeeperException,InterruptedException {
		return this.zookeeper.exists(path, true);
	}
	
	public String getData(String path) throws KeeperException, InterruptedException{
		//read from zookeeper
		byte[] data = zookeeper.getData(path, false, null);
		if (data == null) {
			return "";
		}
		return new String(data);
	}
	
	public Stat setData(String path,String data) throws KeeperException, InterruptedException{
		//write from zookeeper
		Stat stat = zookeeper.setData(path, data.getBytes(), zookeeper.exists(path,true).getVersion());
		return stat;
	}
	
	public void deleteNode(String path) throws InterruptedException, KeeperException{
		//delete znode
		zookeeper.delete(path, zookeeper.exists(path,true).getVersion());
	}
	
	public void closeConnection() throws InterruptedException{
		if (zookeeper != null) {
			zookeeper.close();
		}
	}
}