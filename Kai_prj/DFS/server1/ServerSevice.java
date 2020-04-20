
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

public class ServerSevice implements DFSinterface{
	public int id=1;
	public CONFIG config=new CONFIG();
	public ServerSevice() {
		
	}
	
	public void initialized(int clientID, String IP,int port,String IP1,String IP2,String IP3, int port1,int port2,int port3) {
		System.out.print("Server "+id+": Connecting client"+clientID+"\n");
		if(config.IPlist.isEmpty()) {//if this server has been initialized, doesn't need do that again
			updateConfig(IP1,IP2,IP3,port1,port2,port3);
			if(IP.equals(IP1)&&port==port1) {//inform other two servers to initialize
				try {
					Registry regiS2S1 = LocateRegistry.getRegistry(IP2,port2);
					DFSinterface stub = (DFSinterface) regiS2S1.lookup("DFS");
					stub.updateConfig(IP1,IP2,IP3,port1,port2,port3);		
				} catch (RemoteException e) {
					System.out.print("Server 2 cannot be connected!");
				} catch (NotBoundException e1) {
					System.out.print("\n");
				}
				try {
					Registry regiS2S2 = LocateRegistry.getRegistry(IP3,port3);
					DFSinterface stub2 = (DFSinterface) regiS2S2.lookup("DFS");
					stub2.updateConfig(IP1,IP2,IP3,port1,port2,port3);	
				} catch (RemoteException e2) {
					System.out.print("Server 3 cannot be connected!");
				} catch (NotBoundException e3) {
					System.out.print("\n");
				}
				
			}else if(IP.equals(IP2)&&port==port2) {
				try {
					Registry regiS2S1 = LocateRegistry.getRegistry(IP1,port1);
					DFSinterface stub = (DFSinterface) regiS2S1.lookup("DFS");
					stub.updateConfig(IP1,IP2,IP3,port1,port2,port3);	
				} catch (RemoteException e) {
					System.out.print("Server 1 cannot be connected!");
				} catch (NotBoundException e) {
					System.out.print("\n");
				}

				try {
					Registry regiS2S2 = LocateRegistry.getRegistry(IP3,port3);
					DFSinterface stub2 = (DFSinterface) regiS2S2.lookup("DFS");
					stub2.updateConfig(IP1,IP2,IP3,port1,port2,port3);	
				} catch (RemoteException e) {
					System.out.print("Server 3 cannot be connected!");
				} catch (NotBoundException e) {
					System.out.print("\n");
				}
			}else if(IP.equals(IP3)&&port==port3){
				try {
					Registry regiS2S1 = LocateRegistry.getRegistry(IP1,port1);
					DFSinterface stub = (DFSinterface) regiS2S1.lookup("DFS");
					stub.updateConfig(IP1,IP2,IP3,port1,port2,port3);		
				} catch (RemoteException e) {
					System.out.print("Server 1 cannot be connected!");
				} catch (NotBoundException e) {
					System.out.print("\n");
				}
				try {
					Registry regiS2S2 = LocateRegistry.getRegistry(IP2,port2);
					DFSinterface stub2 = (DFSinterface) regiS2S2.lookup("DFS");
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
	
	public void updateConfig(String IP1,String IP2,String IP3,int port1, int port2, int port3) {
	//update config file, to store IP and port of all servers
		this.config=new CONFIG(IP1,IP2,IP3,port1,port2,port3);
		System.out.print("Server "+id+" finished initialization!"+"\n");
	}
	
	public String createS2S(String fileName) throws RemoteException, NotBoundException {
		//create file
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
		DFSinterface stub = (DFSinterface) regiS2S.lookup("DFS");
		stub.create(fileName);
		stub.updateHashtable(fileName,id,idnext,true);
		String IPthird=config.IPlist.get((id+1)%3);
		int portthird=config.portlist.get((id+1)%3);
		Registry regiS2S2 = LocateRegistry.getRegistry(IPthird, portthird);
		DFSinterface stub2 = (DFSinterface) regiS2S2.lookup("DFS");
		stub2.updateHashtable(fileName, id, idnext,true);
		return "File created successfully.";
	}
	
	public void updateHashtable(String fileName, int id1, int id2,boolean index) throws RemoteException{
		//update hashtable to store files' location
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
		//create a file
		File writer = new File(fileName);
		try {
			writer.createNewFile();
		} catch (IOException e) {
			System.out.print("This file has been created!");
		}
		return "File created successfully.";
	}
	
	public String writeS2S(String fileName,int size) throws RemoteException, NotBoundException {
		//write stringd to a file
		String content=write(fileName,size);
		for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
			List<Integer> servers=entry.getValue();
			System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
		}
		int idnext=(id+1)%3;
		int portnext=8000+id%3;
		String IPnext=config.IPlist.get(id%3);
		Registry regiS2S = LocateRegistry.getRegistry(IPnext,portnext);
		DFSinterface stub = (DFSinterface) regiS2S.lookup("DFS");
		stub.writeCopy(fileName,content);
		return "Successfully wrote to the file.";
	}
	
	public void writeCopy(String fileName,String content) throws RemoteException{
		FileWriter wr;
		try {
			wr = new FileWriter(fileName,true);
			wr.write(content);
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String write(String fileName, int size) throws RemoteException{
		String content=getRandomString(size);
		BufferedReader br;
		try {
			FileWriter wr = new FileWriter(fileName,true);
			wr.write(content);
		    wr.close();
		    br = new BufferedReader(new FileReader(fileName));
		    String str = br.readLine();
		    System.out.print("The total number of bytes of this file is "+str.length()+"\n");
		    return content;
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed wrote to the file.";
		}
	}
	
	public String read(String fileName) {
		//read from a file
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String str;
			try {
				str = br.readLine();
				for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
					List<Integer> servers=entry.getValue();
					System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
				}
				return str;
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
			
		} catch (FileNotFoundException e1) {
			List<Integer> existServer=config.map.get(fileName);
			String IP=config.IPlist.get(existServer.get(0)-1);
			int port=config.portlist.get(existServer.get(0)-1);
			String IP2=config.IPlist.get(existServer.get(0));
			int port2=config.portlist.get(existServer.get(0));
			Registry regiS2S;
			try {
				regiS2S = LocateRegistry.getRegistry(IP,port);
				DFSinterface stub = (DFSinterface) regiS2S.lookup("DFS");
				String str=stub.read(fileName);
				for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
					List<Integer> servers=entry.getValue();
					System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
				}
				return str;
			} catch (RemoteException e2) {
				try {
					regiS2S = LocateRegistry.getRegistry(IP2,port2);
					DFSinterface stub = (DFSinterface) regiS2S.lookup("DFS");
					String str=stub.read(fileName);
					for (Map.Entry<String, List<Integer>> entry : config.map.entrySet()) {
						List<Integer> servers=entry.getValue();
						System.out.println(entry.getKey() + ": " + "server "+servers.get(0)+" server "+servers.get(1));
					}
					return str;
				} catch (RemoteException e) {
					return "error";
				} catch (NotBoundException e) {
					return "error";
				}
			} catch (NotBoundException e) {
				return "error";
			}
			
		}
		//return fileName;
		
	}
	
	
	public String deleteS2S(String fileName) throws RemoteException{
		//delete a file
		File file = new File(fileName);
		if(file.exists()) {
			delete(fileName);
			List<Integer> existServer=config.map.get(fileName);
			if(existServer.get(0)==id) {
				String IP=config.IPlist.get(id%3);
				int port=config.portlist.get(id%3);
				Registry regiS2S = LocateRegistry.getRegistry(IP,port);
				try {
					DFSinterface stub = (DFSinterface) regiS2S.lookup("DFS");
					stub.delete(fileName);
					stub.updateHashtable(fileName, 0, 0, false);
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
				Registry regiS2S2 = LocateRegistry.getRegistry(config.IPlist.get((id+1)%3),config.portlist.get((id+1)%3));
				try {
					DFSinterface stub = (DFSinterface) regiS2S2.lookup("DFS");
					stub.updateHashtable(fileName, 0, 0, false);
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}else {
				String IP=config.IPlist.get((id+1)%3);
				int port=config.portlist.get((id+1)%3);
				Registry regiS2S = LocateRegistry.getRegistry(IP,port);
				try {
					DFSinterface stub = (DFSinterface) regiS2S.lookup("DFS");
					stub.delete(fileName);
					stub.updateHashtable(fileName, 0, 0, false);
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
				Registry regiS2S2 = LocateRegistry.getRegistry(config.IPlist.get(id%3),config.portlist.get(id%3));
				try {
					DFSinterface stub = (DFSinterface) regiS2S2.lookup("DFS");
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
				DFSinterface stub = (DFSinterface) regiS2S.lookup("DFS");
				stub.delete(fileName);
				stub.updateHashtable(fileName, 0, 0, false);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			try {
				Registry regiS2S = LocateRegistry.getRegistry(IP2,port2);
				DFSinterface stub = (DFSinterface) regiS2S.lookup("DFS");
				stub.delete(fileName);
				stub.updateHashtable(fileName, 0, 0, false);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}
		
		return "Successfully delete!";
	}
	
	public void delete(String fileName) throws RemoteException{
		File file = new File(fileName);
		file.delete();
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
