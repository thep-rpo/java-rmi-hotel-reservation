package hotel.model;

public enum RoomType {
	A(90), B(120), C(150), D(180), E(225);
	
	private final double price;
	
	RoomType(double price) {
		this.price = price;
	}
	
	public double getPrice() { 
		return price;
	}

}
