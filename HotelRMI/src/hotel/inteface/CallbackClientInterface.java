package hotel.inteface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import hotel.model.RoomType;

public interface CallbackClientInterface extends Remote {
	
	void notifyRoomsAvailable(RoomType roomType, int availableCount) throws RemoteException;

}
