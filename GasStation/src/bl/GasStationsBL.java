/**
* @authors: Amir Krayden
* @date: 21/03/15 
*/
package bl;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import dal.DalProxy;

public class GasStationsBL extends GasStationBlBase implements Runnable {

	private DalProxy theDalProxy;
	private Logger theLogger;
	
	// The actual gas station object
	private GasStation theGasStation;
	
	public GasStationsBL() {
		
		// Get the system log object
		theLogger = GasStationUtility.getSystemLog(this);
				
		theLogger.log(Level.INFO, "GasStationsBL init", this);
		theLogger.log(Level.INFO, "In GasStationsBL()::GasStationsBL()", this);
		
	}
	
	public void init() {
		
		theLogger.log(Level.INFO, "In GasStationsBL()::init()", this);
		theLogger.log(Level.INFO, "GasStationBL created DAL proxy object", this);
		
		// Get the DAL proxy object
		theDalProxy = DalProxy.getDalProxy();
		
		// Init the DAL proxy
		theDalProxy.init();
		
		// Get the GasStation from configuration
		theLogger.log(Level.INFO, "GasStationBL getting GasStation object from configuration file", this);
		theGasStation = theDalProxy.getGasStationFromConfiguration();
		theLogger.log(Level.INFO, "GasStationBL the GasStation object was created", this);
		
		theLogger.log(Level.INFO, "GasStationBL: there are " + theGasStation.getCarsListSize() + " cars in the station", this);
		theLogger.log(Level.INFO, "GasStationBL: there are " + theGasStation.getNumOfPumps() + " pumps in the station", this);
	}
	
	
	/* User actions from Proxy */
	
	/* Add car */
	public void addCar(int id, boolean fAddToPump, int pumpNum, int numOfLiters, boolean addToCleaning) {
		theLogger.log(Level.INFO, "In GasStationsBL()::addCar()", this);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got car addition request", this);
		
		Car car;
		
		if (fAddToPump == true) {
			// Fuel and maybe cleaning
			car = new Car(id, numOfLiters, pumpNum, addToCleaning);
		}
		else {
			// No fuel
			car = new Car(id, addToCleaning);
		}
		
		theGasStation.addCar(car);
		
	}
	
	/* Add Fuel to main pool */
	public void addFuelToMainPool(int numOfLiters) {
		theLogger.log(Level.INFO, "In GasStationsBL()::addFuelToMainPool()", this);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got fuel addition request", this);
		
		theGasStation.addFuelToMainPool(numOfLiters);
	}
	
	/* Get the number of pumps */
	public int getNumOfPumps() {
		theLogger.log(Level.INFO, "In GasStationsBL()::getNumOfPumps()", this);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got num of pumps request", this);
		
		return theGasStation.getNumOfPumps();
		
	}
	
	/* Get the main fuel pool */
	public MainFuelPool getMainFuelPool() {
		theLogger.log(Level.INFO, "In GasStationsBL()::getMainFuelPool()", this);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got main fuel pool request", this);
		return theGasStation.getFuelPool();
	}
	
	/* Get gas station statistics */
	public GasStationStatistics getGasStationStatistics() {
		theLogger.log(Level.INFO, "In GasStationsBL()::getGasStationStatistics()", this);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got statistics request", this);
		
		return theGasStation.getStatistics();
	}
	
	
	/* close the gas station */
	public void closeGasStation() {
		theLogger.log(Level.INFO, "In GasStationsBL()::closeGasStation()", this);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got gas station closing request", this);
		
		theGasStation.closeGasStation();
	}
	
	// Runnable run method override
	public void run() {
		
		theLogger.log(Level.INFO, "In GasStationsBL::run()", this);
		theLogger.log(Level.INFO, "GasStationBL began execution as seprate thread (id = " + Thread.currentThread().getId()
					  + ")", this);
		
		// Run the gas station
		Thread stationThread = new Thread(theGasStation);
		
		theLogger.log(Level.INFO, "In GasStationBL::run() - executing the GasStation object in a seperate thread ", this);
		
		// Message loop to get commands
		stationThread.start();
		
		try {
			stationThread.join();
		}
		catch(InterruptedException e) {
			
		}
		
		theLogger.log(Level.INFO, "In GasStationBL::run() -  Gas station thread finished executing", this);
	}
	
	
}
