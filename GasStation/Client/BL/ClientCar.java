package BL;

import java.io.Serializable;

public class ClientCar implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static int counter = 0;
	private int id;
	private boolean needWash;
	private int fuel;
	private String status;
	private int pump;
	
	public ClientCar(int fuel, boolean wash, int pump) {
		this.fuel = fuel;
		this.needWash = wash;
		this.pump = pump;
		this.id = counter++;
		this.status = "";
	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		ClientCar.counter = counter;
	}

	public boolean isNeedWash() {
		return needWash;
	}

	public void setNeedWash(boolean needWash) {
		this.needWash = needWash;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
