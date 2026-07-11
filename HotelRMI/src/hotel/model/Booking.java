package hotel.model;

import java.io.Serializable;

public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String customerName;
	private RoomType roomType;
	private int quantity;
	private double totalCost;
	
	public Booking() {}
	
	public Booking(String customerName, RoomType roomType, int quantity) {
		this.customerName = customerName;
		this.roomType = roomType;
		this.quantity = quantity;
		this.totalCost = calculateTotalCost(roomType, quantity);
	}
	
	private double calculateTotalCost(RoomType roomType, int quantity) {
		return quantity * roomType.getPrice();
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
		this.totalCost = calculateTotalCost(roomType, quantity);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
		this.totalCost = calculateTotalCost(roomType, quantity);
	}

	public double getTotalCost() {
		return totalCost;
	}
	
	
	@Override
	public String toString() {
		return quantity + " room(s), type " + roomType + ", cost: " + totalCost;
	}
}
