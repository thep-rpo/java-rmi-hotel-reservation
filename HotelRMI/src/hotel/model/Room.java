package hotel.model;

import java.io.Serializable;

public class Room implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private RoomType roomType;
	private int quantity;
	
	public Room() {}
	
	public Room(RoomType roomType, int quantity) {
		this.roomType = roomType;
		this.quantity = quantity;
	}
	
	public RoomType getRoomType() {
		return roomType;
	}
	
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
