package bl;

import gasstation.GasStationUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GasStation implements Runnable {
	
	private MainFuelPool fuelPool;
	private List<Pump> pumps = new ArrayList<Pump>();
	
	private CleaningService cleanService;
	private Thread cleanServiceThread;
	
	// Pump thread pool;
	private ArrayList<Thread> threadsPumps = null;
	
	private boolean fClosed = true;
	
	private Logger theLogger;
	
	private BlockingQueue<Car> cars;
	private final int WATING_QUEUE_LEN = 10;
	private final int WATING_QUEUE_TIMEOUT = 10; //ms
	
	private ArrayList<Car> handledCars;
	
	public GasStation() {
		
		fClosed = false;
		
		// Get the system log object
		theLogger = GasStationUtility.getSystemLog(this);
		
		cars = new ArrayBlockingQueue<Car>(WATING_QUEUE_LEN);
		
		handledCars = new ArrayList<Car>();
		
		theLogger.log(Level.INFO, "GasStation init", this);
		theLogger.log(Level.INFO, "In GasStation()::GasStation()", this);
		
	}

	public void setCleaningService(int numOfTeams, int price, int secondsPerAutoClean) {
		
		theLogger.log(Level.INFO, "In GasStation()::setCleaningService()", this);
		cleanService = new CleaningService(numOfTeams, price, secondsPerAutoClean);
		
	}
	
	
	
	public void setFuelPool(int maxCapacity,int currentCapacity) {
		fuelPool = new MainFuelPool(maxCapacity, currentCapacity);
	}
	
	public MainFuelPool getFuelPool() {
		return fuelPool;
	}
	
	public void addPump(double pricePerLiter) {
		
		pumps.add(new Pump(fuelPool,pricePerLiter));
	}
	
	public Pump getPump(int id) {
		
		if(id > pumps.size() || id <= 0) {
			
			throw new IllegalArgumentException("Invalid pump id");
		}
		else {
			
			for (Pump pump : pumps) {
		         if(pump.getId() == id) {
		        	 return pump;
		         }
		    }
		}
		
		return null;
	}
	
	public int getNumOfPumps() {
		return pumps.size();
	}
	
	public void addCar(Car car) {
		
		theLogger.log(Level.INFO, "GasStation::addCar()", this);
		
		// Add the car to the list
		cars.add(car);
		
		theLogger.log(Level.INFO, "Car id :" + car.getId() + " Want Cleaning :" + car.wantsCleaning(), this);
	
		if(car.wantsFuel()) {
			theLogger.log(Level.INFO, "Num of Liters :" + car.getNumOfLiters() + " Pump :" + car.getPumpNum(), this);
		}
	}
	
	public int getCarsListSize() {
		
		return cars.size();
		
	}
	
	/* Add Fuel to main pool */
	public void addFuelToMainPool(int numOfLiters) {
		theLogger.log(Level.INFO, "In GasStation()::addFuelToMainPool()", this);
		
		try {
			fuelPool.fullGas(numOfLiters);
		}
		catch(FuelPoolException e) {
			theLogger.log(Level.INFO, "In GasStation()::addFuelToMainPool() - got fuel exception: ", this);
			theLogger.log(Level.INFO, e.getMessage(), this);
		}
	}
	
	public void closeGasStation() {
		fClosed = true;
	}
	
	/*
	 * Activate the pumps threads
	 */
	private void activatePumps() {
		
		theLogger.log(Level.INFO, "In GasStation()::activatePumps()", this);
		theLogger.log(Level.INFO, "In GasStation()::activatePumps() - creating thread pool for " + pumps.size() + " pumps", this);
		
		
		threadsPumps = new ArrayList<Thread>(pumps.size());
		
		// Create a thread for each pump
		for (int i = 0; i < pumps.size(); i++) {
			
			theLogger.log(Level.INFO, "In GasStation()::activatePumps() - creating thread for pump: " + pumps.get(i).getId(), this);
			Thread t = new Thread(pumps.get(i));
			threadsPumps.add(t);
			t.start();
			theLogger.log(Level.INFO, "In GasStation()::activatePumps() - created thread for pump: " + pumps.get(i).getId(), this);
		}
		
	}
	
	private void deactivatePumps() {
		
		theLogger.log(Level.INFO, "In GasStation()::deactivatePumps()", this);
		
		// send close command to each pump
		theLogger.log(Level.INFO, "In GasStation()::deactivatePumps() - sending close command to the pumps", this);
		for (Pump p : pumps) {
			p.closePump();
		}
		
		// Wait for the pumps threads
		try {
			for (Thread t : threadsPumps) {
				t.join();
			}
		}
		catch(InterruptedException e) {
			
		}
		
	}
	
	private void activateCleaningService() {
		theLogger.log(Level.INFO, "In GasStation()::activateCleaningService()", this);
		cleanServiceThread = new Thread(cleanService);
		
		cleanServiceThread.start();
	}
	
	private void deactivateCleaningService() {
		theLogger.log(Level.INFO, "In GasStation()::deactivateCleaningService()", this);
		
		// send close command to the cleaning service
		cleanService.closeCleaningService();
		
		// Wait for the cleaning service thread to finish
		try {
			cleanServiceThread.join();
		}
		catch (InterruptedException e) {
			
		}
	}
	
	@Override
	public void run() {
	
		// Run the logic on the different cars
		theLogger.log(Level.INFO, "In GasStation()::run()", this);
		theLogger.log(Level.INFO, "In GasStation()::run() -  Started running as a separate thread", this);
		
		// Activate the pumps and the cleaning service
		activatePumps();
		activateCleaningService();
		
		while (fClosed == false) {
		
			Car theCar = null;
			
			// Get the new car
			try {
				theCar = cars.poll(WATING_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
			}
			catch(InterruptedException e) {
				
			}
			
			if (theCar != null) {				
				
				if (theCar.wantsFuel() == true && theCar.wantsCleaning() == true) {

					if (handledCars.contains(theCar)) {
						
						
						if (theCar.isFueled() == true) {
							// Remove from the handled list and send to cleaning
							handledCars.remove(theCar);
							theLogger.log(Level.INFO, "In GasStation()::run() - car " + theCar.getId() + " was removed from handling queue", this);
							
							theLogger.log(Level.INFO, "In GasStation()::run() - sending car " + theCar.getId() + " to cleaning", this);
							
							cleanService.cleaning(theCar);
						}
						else {
							// Back to the queue
							try {
								cars.put(theCar);
							}
							catch( InterruptedException e) {
								
							}
						}
					}
					else {
						theLogger.log(Level.INFO, "In GasStation()::run() - sendind car " + theCar.getId() + " to fueling", this);
						theLogger.log(Level.INFO, "In GasStation()::run() - car " + theCar.getId() + " was added to handling queue", this);
						// Add the car to the handled cars list
						handledCars.add(theCar);
						
						// send it to fueling 
						pumps.get(theCar.getPumpNum()-1).refuel(theCar);
						
						// Place it back on the queue for later being cleaned
						try {
							cars.put(theCar);
						}
						catch( InterruptedException e) {
							
						}
					}
				}
				else if(theCar.wantsFuel() == true && theCar.wantsCleaning() == false) {
					// Fuel the car
					theLogger.log(Level.INFO, "In GasStation()::run() - sendind car " + theCar.getId() + " to fueling", this);
					pumps.get(theCar.getPumpNum()-1).refuel(theCar);
				}
				else {
					// The car wants only cleaning
					if (theCar.wantsCleaning()) {
						theLogger.log(Level.INFO, "In GasStation()::run() - sendind car " + theCar.getId() + " to cleaning", this);
						cleanService.cleaning(theCar);
					}
				}
			}
			
		}
		
		
		deactivatePumps();
		deactivateCleaningService();
		
		theLogger.log(Level.INFO, "In GasStation()::run() -  Thread is closing", this);
	}
	
	// Get current statistics
	public GasStationStatistics getStatistics() {
		
		GasStationStatistics stats = new GasStationStatistics();
		
		// Fill the statistics object
		stats.setNumOfCarsThatWasCleaned(cleanService.getNumOfCarsServed());
		stats.setCleaningRevenew(cleanService.getTotalProfit());
		
		int numOfCarsFueled = 0;
		float totalProfit = 0;
		
		// Sum the amounts of cars and liters from the pumps
		for (Pump p : pumps) {
			numOfCarsFueled += p.totalCarsFueled();
			totalProfit		+= p.totalProfit();
		}
		
		stats.setNumOfCarsThatFueled(numOfCarsFueled);
		stats.setPumpsRevenew(totalProfit);
		
		return stats;
	}
}
