package hotel.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HRServer {
	public static void main(String[] args) {
		int portNumber = 8080;
		
		if (args.length > 0) {
			try {
				portNumber = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Invalid port number, using default 8080");
			}
		}
		
		try {
			startRegistry(portNumber);
			
			HRImpl exportedObject = new HRImpl();
			
			String registryURL = "rmi://localhost:" + portNumber + "/HotelService";
			
			Naming.rebind(registryURL, exportedObject);
			
			System.out.println("Hotel reservation server is ready at port " + portNumber);
			System.out.println("Service bound at: " + registryURL);
		} catch (Exception e) {
			System.err.println("Exception in HRServer: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void startRegistry(int port) throws RemoteException {
		try {
			Registry registry = LocateRegistry.getRegistry(port);
			registry.list();
			System.out.println("RMI registry found at port " + port);
		} catch (RemoteException e) {
			System.out.println("RMI registry not found. Creating one at port " + port);
			LocateRegistry.createRegistry(port);
		}
	}
}
