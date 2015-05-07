package bl;

import gasstation.GasStationUtility;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFuelPool {
	private static final int ZERO = 0;
	private static final double MIN_TARGET = 0.2;
	private List<FillingMainFuelPool_Observer> fillingFuelObservers = new ArrayList<FillingMainFuelPool_Observer>();
	private List<MinTarget_Observer> minTargetObservers = new ArrayList<MinTarget_Observer>();
	private int maxCapacity;
	
	private int currentCapacity;
	private static Object theCapacityMutex = new Object();
	
	private Logger theLogger;
	
	public MainFuelPool(int maxCapacity,int currentCapacity) {
		
		this.maxCapacity = maxCapacity;
		this.currentCapacity = currentCapacity;
		
		// Get the system log object
		theLogger = GasStationUtility.getSystemLog(this);
				
		theLogger.log(Level.INFO, "MainFuelPool init", this);
		theLogger.log(Level.INFO, "In MainFuelPool()::MainFuelPool()", this);
		theLogger.log(Level.INFO, "In MainFuelPool()::MainFuelPool() - current capacity: " + getCurrentCapacity(), this);
		theLogger.log(Level.INFO, "In MainFuelPool()::MainFuelPool() - max capacity: " + getMaxCapacity(), this);
	}
	
	public synchronized int getCurrentCapacity() {
		return currentCapacity;
	}
	
	public synchronized int getMaxCapacity() {
		return maxCapacity;
	}
	
	public void fullGas(int quantity) throws FuelPoolException {
		
		Boolean fMaxCapacity = false;
		
		theLogger.log(Level.INFO, "In MainFuelPool()::fullGas() - Liters: " + quantity, this);
		
		synchronized (theCapacityMutex) {
			
			if(currentCapacity + quantity > maxCapacity) {
				fMaxCapacity = true;
			}
			else {
		
				theLogger.log(Level.INFO, "In MainFuelPool()::fullGas() - Notifying observers that the pool is being fueled", this);
				notifyAllObserversFuelingStarted();
				
				// Sleep for some time
				// Random fueling time factores by the number of liters needed
				int fuelingTime = (int)(Math.random() * quantity);
				
				theLogger.log(Level.INFO, "In MainFuelPool::fullGas() - pool is fueling for " + fuelingTime + " ms", this);
				
				try {
					Thread.sleep(fuelingTime);
				}
				catch (InterruptedException e) {
					
				}
				
				theLogger.log(Level.INFO, "In MainFuelPool::fullGas() - pool is fueled", this);
				
				currentCapacity += quantity;
				
				theLogger.log(Level.INFO, "In MainFuelPool()::fullGas() - Notifying observers that the pool is fueled", this);
				notifyAllObserversFuelingFinished();
			}
		
		}
		
		if (fMaxCapacity == true) {
			throw new  FuelPoolException("The current capacity plus filling quantity can not be great then max capacity.");
		}
		
	}
	
	public void getGas(int quantity) throws FuelPoolException {
		
		boolean fThrowException = false;
		FuelPoolException exp = null;
		
		synchronized (theCapacityMutex) {
			
		
			if(currentCapacity == ZERO) {
				fThrowException = true;
				exp =  new FuelPoolException("Fuel pool is empty");
			}
			else if(currentCapacity - quantity < ZERO) {
				fThrowException = true;
				exp =  new FuelPoolException("There is not enough fuel in the pool");
			}
			else {
				
				currentCapacity -= quantity;
				if(((float)currentCapacity / maxCapacity) < MIN_TARGET)
				{
					theLogger.log(Level.INFO, "In MainFuelPool::getGas() - send exception", this);
					theLogger.log(Level.INFO, "In MainFuelPool::getGas() - current: " + currentCapacity + " max: " + maxCapacity, this);
					notifyAllMinTargetObservers();
				}
			}
		
		}
		
		if (fThrowException == true) {
			throw exp;
		}
	}
	
	public void attach(FillingMainFuelPool_Observer observer){
		  theLogger.log(Level.INFO, "In MainFuelPool::attach() - attched new fueling observer", this);
	      fillingFuelObservers.add(observer);		
	}
	
	public void attach(MinTarget_Observer observer){
	      minTargetObservers.add(observer);		
	}
	
	// Notify all observers that main pool fueling started
	private void notifyAllObserversFuelingStarted(){
		theLogger.log(Level.INFO, "In MainFuelPool::notifyAllObserversFuelingStarted()", this);
	    for (FillingMainFuelPool_Observer observer : fillingFuelObservers) {
	         observer.updateMainPumpStartedFueling();
	    }
	}
	
	// Notify all observers that main pool fueling started
	private void notifyAllObserversFuelingFinished(){
		theLogger.log(Level.INFO, "In MainFuelPool::notifyAllObserversFuelingFinished()", this);
	    for (FillingMainFuelPool_Observer observer : fillingFuelObservers) {
	         observer.updateMainPumpFinishedFueling();
	    }
	}
	
	private void notifyAllMinTargetObservers(){
	    for (MinTarget_Observer observer : minTargetObservers) {
	         observer.update();
	    }
	} 	
}
