package hotel.inteface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import hotel.model.Booking;
import hotel.model.BookingResult;
import hotel.model.RoomType;

public interface HRInterface extends Remote {
	String listAvailableRooms() throws RemoteException;
	BookingResult bookRooms(RoomType roomType, int requested, String customerName) throws RemoteException;
	Booking confirmPartialBooking(RoomType roomType, int actualQuantity, String customerName) throws RemoteException;
	String cancelBooking(RoomType roomType, int number, String customerName) throws RemoteException;
	List<Booking> getAllBookings() throws RemoteException;
	void registerForNotification(RoomType roomType, CallbackClientInterface client) throws RemoteException;
	void unregisterForNotification(RoomType roomType, CallbackClientInterface client) throws RemoteException;
	
}
