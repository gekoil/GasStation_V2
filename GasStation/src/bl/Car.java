package bl;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Car {
	
	private int id;
	private Boolean finishCleaning;
	private Boolean finishFuel;
	private Boolean wantCleaning;
	private Boolean wantFuel;
	private int numOfLiters;
	private int pumpNum;
	
	private Logger theLogger;
	
	public Car(int id,Boolean wantCleaning) {
		this.id = id;
		this.wantCleaning = wantCleaning;
		this.wantFuel = false;
		this.finishCleaning = false;
		this.finishFuel = false;
		
		initLog();
		theLogger.log(Level.INFO, "In Car::Car()",this);
		theLogger.log(Level.INFO, "In Car::Car() - created Car id = " + id,this);
	}
	
	public Car(int id,int numOfLiters,int pumpNum) {
		this.id = id;
		this.numOfLiters = numOfLiters;
		this.pumpNum = pumpNum;
		this.wantFuel = true;
		this.wantCleaning = false;
		this.finishCleaning = false;
		this.finishFuel = false;
		
		initLog();
		theLogger.log(Level.INFO, "In Car::Car()",this);
		theLogger.log(Level.INFO, "In Car::Car() - created Car id = " + id,this);
	}
	
	public Car(int id,int numOfLiters,int pumpNum,Boolean wantCleaning) {
		this.id = id;
		this.numOfLiters = numOfLiters;
		this.pumpNum = pumpNum;
		this.wantCleaning = wantCleaning;
		this.wantFuel = true;
		this.finishCleaning = false;
		this.finishFuel = false;
		
		initLog();
		theLogger.log(Level.INFO, "In Car::Car()",this);
		theLogger.log(Level.INFO, "In Car::Car() - created Car id = " + id,this);
	}
	
	private void initLog() {
	
		theLogger = GasStationUtility.getCarLog(this, id);
		
	}
	
	public void finishCleaning() {
		
		theLogger.log(Level.INFO, "In Car::finishCleaning()",this);
		theLogger.log(Level.INFO, "In Car::finishCleaning() - Car id = " + id,this);
		
		finishCleaning = true;
	}
	
	public void finishFuel() {
		
		theLogger.log(Level.INFO, "In Car::finishFuel()",this);
		theLogger.log(Level.INFO, "In Car::finishFuel() - Car id = " + id,this);
		
		finishFuel = true;
	}
	
	public boolean isFueled() {
		return finishFuel;
	}
	
	public boolean isCleaned() {
		return finishCleaning;
	}
	
	public int getNumOfLiters() {
		return numOfLiters;
	}
	
	public boolean wantsFuel() {
		return wantFuel;
	}
	
	public boolean wantsCleaning() {
		return wantCleaning;
	}
	
	public int getPumpNum() {
		return pumpNum;
	}
	
	public int getId() {
		return id;
	}
}
