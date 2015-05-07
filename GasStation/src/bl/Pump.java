package bl;

import gasstation.GasStationUtility;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Pump extends FillingMainFuelPool_Observer implements Runnable {
	private static int counter = 0;
	private int id;
	private double pricePerLiter;
	
	private Logger theLogger;
	
	private BlockingQueue<Car>cars;
	private static final int WATING_QUEUE_LEN = 10;
	private static final int WATING_QUEUE_TIMEOUT = 50; //ms
	
	private Boolean fClosed;
	
	private int numOfCarServed;
	private Object numOfCarServedMutex;
	
	private int numOfLitersUsed;
	private Object numOfLitersUsedMutex;
	
	private boolean fWaitForMainPump;
	private Object fWaitForMainPumpMutex;
	
	public Pump(MainFuelPool subject,double pricePerLiter){
		
		
		this.subject = subject;
		this.pricePerLiter = pricePerLiter;
		id = ++counter;
		this.subject.attach(this);
		
		cars = new ArrayBlockingQueue<Car>(WATING_QUEUE_LEN);
		
		numOfCarServed = 0;
		numOfCarServedMutex = new Object();
		
		numOfLitersUsed = 0;
		numOfLitersUsedMutex = new Object();
		
		fClosed = false;
		
		fWaitForMainPump = false;
		fWaitForMainPumpMutex = new Object();
		
		// Get the logger object
		theLogger = GasStationUtility.getPumpLog(this,id);
		
		theLogger.log(Level.INFO, "In Pump::Pump()", this);
		theLogger.log(Level.INFO, "Pump init (id=" + id + ")", this);
		
		
	}
	
	public void refuel(Car car)
	{
		theLogger.log(Level.INFO, "In Pump::refuel()", this);
		theLogger.log(Level.INFO, "In Pump::refuel - car " + car.getId() + " wants to enter", this);
		
		try {
			cars.put(car);
		}
		catch (InterruptedException e) {
			
		}
		
		theLogger.log(Level.INFO, "In Pump::refuel - car " + car.getId() + " is in queue", this);
		
	}

	private Car getNextCar() {
		
		Car nextCar = null;
			
		
		if (fWaitForMainPump == false) { 
		
			// Try to get the next car from the queue or wait for it
			try {
				nextCar = cars.poll(WATING_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				
			}
			
			if (nextCar != null)
				theLogger.log(Level.INFO, "In Pump::getNextCar() - car " + nextCar.getId() + " is ready to be served", this);
		}
		
		
		return nextCar;
	}
	
	// Cars queue check
	private boolean carsWaiting() {
		
		return (cars.isEmpty() == false);
		
	}
	
	// Car serving
	private void serveCar(Car car) {
		
		theLogger.log(Level.INFO, "In Pump::serveCar()", this);
		theLogger.log(Level.INFO, "In Pump::serveCar() - car " + car.getId() + " is being served", this);
		theLogger.log(Level.INFO, "In Pump::serveCar() - car needs " +  car.getNumOfLiters() + " litres", this);
		
		// Fuel the car if can
		
		// Random fueling time factores by the number of liters needed
		int fuelingTime = (int)(Math.random() * 10 * car.getNumOfLiters());
		
		theLogger.log(Level.INFO, "In Pump::serveCar() - car  " +  car.getId() + 
					  " is fueling for " + fuelingTime + " ms", this);
		
		
		try {
			
			subject.getGas(car.getNumOfLiters());
			Thread.sleep(fuelingTime);
			IncreaseNumOfCarsServed();
			IncreaseNumOfLitersUsed(car.getNumOfLiters());
			
			theLogger.log(Level.INFO, "In Pump::serveCar() - car  " +  car.getId() + 
					  " is fueled", this);
			
			car.finishFuel();
		}
		catch (FuelPoolException e) {
			theLogger.log(Level.INFO,e.getMessage(),this);
			refuel(car);
		}
		catch(InterruptedException e) {
			
		}
	}
	
	@Override
	public void updateMainPumpStartedFueling() {
		// Mark the waiting flag as true
		synchronized (fWaitForMainPumpMutex) {
			theLogger.log(Level.INFO, "In Pump::updateMainPumpStartedFueling() - main pump fueling",this);
			fWaitForMainPump = true;
		}
	}
	
	@Override
	public void updateMainPumpFinishedFueling() {
		// Mark the waiting flag as false
		synchronized (fWaitForMainPumpMutex) {
			theLogger.log(Level.INFO, "In Pump::updateMainPumpFinishedFueling() - main pump finished fueling",this);
			fWaitForMainPump = false;
		}
		
	}
	
	public double getPricePerLiter() {
		return pricePerLiter;
	}
	
	public int getId() {
		return id;
	}
	
	// Get the number of cars served
	public int totalCarsFueled() {
		int num;
		
		synchronized (numOfCarServedMutex) {
			num = this.numOfCarServed;
		}
		
		return num;
	}

	public void IncreaseNumOfCarsServed() {
		
		synchronized (numOfCarServedMutex) {
			this.numOfCarServed++;
		}
		
	}
	
	
	public int totalLitersUsed() {
		int num;
		
		synchronized (numOfLitersUsedMutex) {
			num = this.numOfLitersUsed;
		}
		
		return num;
	}
	
	public void IncreaseNumOfLitersUsed(int amount) {
		
		synchronized (numOfLitersUsedMutex) {
			this.numOfLitersUsed += amount;
		}
		
	}
	
	// Get the pumps total profit
	public double totalProfit() {
		return totalLitersUsed() * pricePerLiter;
	}
	
	public void closePump() {
		
		theLogger.log(Level.INFO, "In Pump::closePump()", this);
		fClosed = true;
	}
	
	// Thread entry point
	@Override
	public void run() {
		
		theLogger.log(Level.INFO, "In Pump::run()", this);
		theLogger.log(Level.INFO, "In Pump::run() - pump number: " + this.getId() + 
					  " started running as a seperate thread", this);
		
		// Main loop
		while (fClosed == false || carsWaiting()) {
			
			Car nextCar = getNextCar();
			
			// Serve the next car if exits
			if (nextCar != null) {
				theLogger.log(Level.INFO, "In Pump::run() - serving car " + nextCar.getId(), this);
				serveCar(nextCar);
			}
		}
		
		theLogger.log(Level.INFO, "In Pump::run() - pump number: " + this.getId() + 
				  " is now closed", this);
		
	}
}
