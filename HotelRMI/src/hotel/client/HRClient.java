package hotel.client;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

import hotel.inteface.HRInterface;
import hotel.model.Booking;
import hotel.model.BookingResult;
import hotel.model.RoomType;

public class HRClient {
	public static void main(String[] args) {
		if (args.length < 1) {
			printUsage();
			return;
		}
		
		String command = args[0].toLowerCase();
		
		try {
			switch(command) {
				case "list":
					if(args.length < 2) { printUsage(); return; }
					handleList(args[1]);
					break;
				
				case "book":
					if(args.length < 5) { printUsage(); return; }
					handleBook(args[1], args[2], Integer.parseInt(args[3]), args[4]);
					break;
					
				case "guests":
					if(args.length < 2) { printUsage(); return; }
					handleGuests(args[1]);
					break;
				
				case "cancel":
					if(args.length < 5) { printUsage(); return; }
					handleCancel(args[1], args[2], Integer.parseInt(args[3]), args[4]);
					break;
					
				default:
					printUsage();
			}
		} catch (Exception e) {
			System.err.println("Client error: " + e.getMessage());
		}
	}
	
	private static void handleList(String host) throws Exception {
		HRInterface service = (HRInterface) Naming.lookup("rmi://"+ host + "/HotelService");
		System.out.println(service.listAvailableRooms());
	}
	
	private static void handleBook(String host, String type, int num, String name) throws Exception {
		HRInterface service = (HRInterface) Naming.lookup("rmi://"+ host + "/HotelService");
		RoomType roomType = RoomType.valueOf(type.toUpperCase());
		
		BookingResult result = service.bookRooms(roomType, num, name);
		
		if (result.isSuccess()) {
			System.out.println(result.getMessage());
		} else if (result.isPartialPossible()) {
			System.out.println(result.getMessage());
			System.out.println("Would you like to book only the available rooms? (yes/no): ");
			try (Scanner scanner = new Scanner(System.in)) {
				if(scanner.nextLine().equalsIgnoreCase("yes")) {
					service.confirmPartialBooking(roomType, result.getAvailableCount(), name);
					System.out.println("Partial booking confirmed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(result.getMessage());
			System.out.println("No rooms available. Subscribe for notifications when rooms of type " + roomType + " become available (yes/no): ");
			try (Scanner scanner = new Scanner(System.in)) {
				if (scanner.nextLine().equalsIgnoreCase("yes")) {
					Object lock = new Object();
					CallbackClientImpl callbackObject = new CallbackClientImpl(lock);
					service.registerForNotification(roomType, callbackObject);
					System.out.println("You are now registered for notifications, keep this client running");
					synchronized (lock) { lock.wait(); }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void handleGuests(String host) throws Exception {
		HRInterface service = (HRInterface) Naming.lookup("rmi://" + host + "/HotelService");
		List<Booking> bookings = service.getAllBookings();
		if (bookings.isEmpty()) {
			System.out.println("No active bookings found");
		} else {
			bookings.forEach(System.out::println);
		}
	}
	
	private static void handleCancel(String host, String type, int num, String name) throws Exception {
		HRInterface service = (HRInterface) Naming.lookup("rmi://" + host + "/HotelService");
		RoomType roomType = RoomType.valueOf(type.toUpperCase());
		String result = service.cancelBooking(roomType, num, name);
		System.out.println(result);
	}
	
	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("java HRClient list <hostname>");
		System.out.println("java HRClient book <hostname> <roomType> <number> <name>");
		System.out.println("java HRClient guests <hostname>");
		System.out.println("java HRClient cancel <hostname> <roomType> <number> <name>");
	}
}
