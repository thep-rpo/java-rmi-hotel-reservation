package hotel.client;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import hotel.inteface.CallbackClientInterface;
import hotel.model.RoomType;

public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {
	private static final long serialVersionUID = 1L;
	private final Object lock;

	protected CallbackClientImpl(Object lock) throws RemoteException {
		super();
		this.lock = lock;
	}
	
	@Override
	public void notifyRoomsAvailable(RoomType roomType, int availableCount) throws RemoteException {
		System.out.println("[NOTIFICATION] Rooms of type " + roomType +
				" are now available. Current count: " + availableCount);
		
		synchronized (lock) {
			lock.notify();
		}
		
		try {
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {}
		
	}
	
}