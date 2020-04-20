
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;

public class DFSCLIENT {
	
	public static int id=2;
	
	public static int select(String IP1, String IP2, String IP3, int port1, int port2, int port3) {
		boolean server=true;
		Random random=new Random();
		String IP="";
		int port=0;
		int index=-1;
		while(server==true) {
			index=random.nextInt(3);
			if(index==0) {
				IP=IP1;
				port=port1;
			}else if(index==1){
				IP=IP2;
				port=port2;
			}else {
				IP=IP3;
				port=port3;
			}
			try {
				Registry registry = LocateRegistry.getRegistry(IP,port);
				DFSinterface stub = (DFSinterface) registry.lookup("DFS");
				server=false;
			} catch (RemoteException e) {
				server=true;
			} catch (NotBoundException e) {
				System.out.print("This server cannot be connected");
			}
		}
		return index;
	}
	
	public static void main(String[] args) throws RemoteException, NotBoundException{
		
		String info1=args[0];
		String IP1=info1.substring(0,info1.indexOf(":"));
		String host1=info1.substring(info1.indexOf(":")+1);
		int port1=Integer.parseInt(host1);
		
		String info2=args[1];
		String IP2=info2.substring(0,info2.indexOf(":"));
		String host2=info2.substring(info2.indexOf(":")+1);
		int port2=Integer.parseInt(host2);
		
		String info3=args[2];
		String IP3=info3.substring(0,info3.indexOf(":"));
		String host3=info3.substring(info3.indexOf(":")+1);
		int port3=Integer.parseInt(host3);
		String IP="";
		int port=0;
		int index=select(IP1,IP2,IP3,port1,port2,port3);//choose an available server
		if(index==0) {
			IP=IP1;
			port=port1;
		}else if(index==1){
			IP=IP2;
			port=port2;
		}else {
			IP=IP3;
			port=port3;
		}
		boolean start=true;
		Registry registry = LocateRegistry.getRegistry(IP,port);
        DFSinterface stub = (DFSinterface) registry.lookup("DFS");
        stub.initialized(id, IP,port,IP1,IP2,IP3,port1,port2,port3);
        while(start==true) {
        	System.out.println("Please input your command:");
			Scanner scanner = new Scanner(System.in);
			String command = scanner.nextLine();
			if(command.substring(0,1).equals("c")) {
				String fileName=command.substring(7);
				String res=stub.createS2S(fileName);
				System.out.print(res);
				System.out.print("\n");
			}else if(command.substring(0,1).equals("w")) {
				String fileName=command.substring(6,command.indexOf(" ",6));
				String sizes=command.substring(command.indexOf(" ",6)+1);
				int size=Integer.parseInt(sizes);
				String res=stub.writeS2S(fileName, size);
				System.out.print(res);
				System.out.print("\n");
			}else if(command.substring(0,1).equals("r")) {
				String fileName=command.substring(5);
				String res=stub.read(fileName);
				System.out.print(res);
				System.out.print("\n");
			}else if(command.substring(0,1).equals("d")) {
				String fileName=command.substring(7);
				String res=stub.deleteS2S(fileName);
				System.out.print(res);
				System.out.print("\n");
			}else if(command.substring(0,1).equals("e")){
				System.out.println("Finished"); 
				start=false;
			}else {
				System.out.println("No such command, please input correct one!"); 
			}
        }
	}
	
}
