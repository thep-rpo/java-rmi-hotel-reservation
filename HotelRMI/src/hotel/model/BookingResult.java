package hotel.model;

import java.io.Serializable;

public class BookingResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean success;
	private boolean partialPossible;
	private int availableCount;
	private double totalCost;
	private String message;
	
	public BookingResult(boolean success, boolean partialPossible, int availableCount, double totalCost, String message) {
		this.success = success;
		this.partialPossible = partialPossible;
		this.availableCount = availableCount;
		this.totalCost = totalCost;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isPartialPossible() {
		return partialPossible;
	}

	public void setPartialPossible(boolean partialPossible) {
		this.partialPossible = partialPossible;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
