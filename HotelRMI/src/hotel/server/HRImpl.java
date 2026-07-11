package hotel.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import hotel.inteface.CallbackClientInterface;
import hotel.inteface.HRInterface;
import hotel.model.Booking;
import hotel.model.BookingResult;
import hotel.model.RoomType;

public class HRImpl extends UnicastRemoteObject implements HRInterface {
	private static final long serialVersionUID = 1L;
	
	private final Map<RoomType, Integer> availableRooms = new ConcurrentHashMap<>();
	private final List<Booking> allBookings = new CopyOnWriteArrayList<>();
	private final Map<RoomType, List<CallbackClientInterface>> waitlists = new ConcurrentHashMap<>();
	
	public HRImpl() throws RemoteException {
		availableRooms.put(RoomType.A, 60);
		availableRooms.put(RoomType.B, 50);
		availableRooms.put(RoomType.C, 40);
		availableRooms.put(RoomType.D, 30);
		availableRooms.put(RoomType.E, 20);
	
		for (RoomType roomType : RoomType.values()) {
			waitlists.put(roomType, new CopyOnWriteArrayList<>());
		}
	}
	
	@Override
	public synchronized String listAvailableRooms() throws RemoteException {
		StringBuilder stringBuilder = new StringBuilder();
		for(RoomType roomType : RoomType.values()) {
			int count = availableRooms.getOrDefault(roomType, 0);
			stringBuilder.append(count).append(" rooms of type ").append(roomType)
				.append(" - price: ").append((int) roomType.getPrice()).append(" per night\n");
		}
		
		return stringBuilder.toString();
	}
	
	@Override
	public synchronized BookingResult bookRooms(RoomType roomType, int requested, String customerName) 
			throws RemoteException {
		
		int available = availableRooms.getOrDefault(roomType, 0);
		
		boolean success = false;
		boolean partialPossible = false;
		int availableCount = 0;
		double totalCost = 0.0;
		String message = new String();
		
		if (requested <= 0) {
			message = "Invalid number of rooms.";
			return new BookingResult(success, partialPossible, availableCount, totalCost, message);
		}
		
		if (requested <= available) {
			totalCost = requested * roomType.getPrice();
			Booking booking = new Booking(customerName, roomType, requested);
			allBookings.add(booking);
			availableRooms.put(roomType, available - requested);
			
			success = true;
			
			return new BookingResult(success, partialPossible, available, totalCost,
					"Booking successful, total cost: " + totalCost);
		} else if (available > 0) {
			double partialCost = available * roomType.getPrice();
			
			partialPossible = true;
			
			return new BookingResult(success, partialPossible, available, partialCost,
					"Partial booking, rooms available: " + available + " partial cost: " + partialCost);
		} else {
			return new BookingResult(success, partialPossible, availableCount, totalCost,
					"No rooms of type " + roomType + " available");
		}
	}

	@Override
	public Booking confirmPartialBooking(RoomType roomType, int actualQuantity, String customerName)
			throws RemoteException {
		int available = availableRooms.getOrDefault(roomType, 0);
		
		if (actualQuantity <= 0 || actualQuantity > available) {
			throw new RemoteException("Cannot confirm partial booking: invalid quantity");
		}
		
		Booking booking = new Booking(customerName, roomType, actualQuantity);
		allBookings.add(booking);
		availableRooms.put(roomType, available - actualQuantity);
		return booking;
	}

	@Override
	public String cancelBooking(RoomType roomType, int number, String customerName) throws RemoteException {
		List<Booking> userBookings = allBookings.stream()
				.filter(b -> b.getCustomerName().equalsIgnoreCase(customerName) && b.getRoomType() == roomType)
				.collect(Collectors.toList());
		
		int totalUserHas = userBookings.stream().mapToInt(Booking::getQuantity).sum();
		
		if (totalUserHas < number) {
			return "Failure: User does not have " + number + " rooms of type " + roomType + " to cancel ";
		}
		
		int toRemove = number;
		for (Booking b : userBookings) {
			if (toRemove <= 0) break;
			if (b.getQuantity() <= toRemove) {
				toRemove -= b.getQuantity();
				allBookings.remove(b);
			} else {
				b.setQuantity(b.getQuantity() - toRemove);
				toRemove = 0;
			}
		}
		
		int newAvailable = availableRooms.get(roomType) + number;
		availableRooms.put(roomType, newAvailable);
		notifyWaitlist(roomType, newAvailable);
		
		return "Cancellation successful. Your remaining bookings: " +
			allBookings.stream().filter(b -> b.getCustomerName().equalsIgnoreCase(customerName)).collect(Collectors.toList());
	}

	@Override
	public List<Booking> getAllBookings() throws RemoteException {
		return new ArrayList<>(allBookings);
	}

	@Override
	public void registerForNotification(RoomType roomType, CallbackClientInterface client) throws RemoteException {
		if (!waitlists.get(roomType).contains(client)) {
			waitlists.get(roomType).add(client);
		}
		
	}

	@Override
	public void unregisterForNotification(RoomType roomType, CallbackClientInterface client) throws RemoteException {
		waitlists.get(roomType).remove(client);
	}
	
	private void notifyWaitlist(RoomType roomType, int count) {
		List<CallbackClientInterface> clients = waitlists.get(roomType);
		for(CallbackClientInterface client : clients) {
			try {
				client.notifyRoomsAvailable(roomType, count);
			} catch(RemoteException e) {
				clients.remove(client);
			}
		}
	}
}
