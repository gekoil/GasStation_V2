/**
 * @authors: Amir Krayden
 * @date: 21/03/15 
 */
package bl;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlProxy extends GasStationBlBase{
	private static BlProxy theBlProxy = null;
	private static Object theBlProxyMutex = new Object();
	private static Logger theLogger;
	
	private GasStationsBL theBL;
	private Thread theBlThread = null;
	
	protected BlProxy() {
		
		// Get the system log object
		theLogger = GasStationUtility.getSystemLog(this);
		
		theLogger.log(Level.INFO, "BLProxy init", this);
		theLogger.log(Level.INFO, "In BLProxy()::blProxy()", this);
		
	}
	
	public static BlProxy getBlProxy() {
		
		synchronized (theBlProxyMutex) {
			
			if (theBlProxy == null) {
				theBlProxy = new BlProxy();
				
				theLogger.log(Level.INFO, "Created BLProxy object", theBlProxy);
			}
			else {
				theLogger.log(Level.INFO, "BLProxy object already exists", theBlProxy);
			}
		
		}
		
		theLogger.log(Level.INFO, "In BLProxy::getBlProxy()", theBlProxy);
		return theBlProxy;
	}
	
	public Thread runThread() {
		
		if (theBlThread == null) {
			theLogger.log(Level.INFO, "In BLProxy::runThread()", theBlProxy);
			
			theLogger.log(Level.INFO, "In BLProxy:: Running the BL as a seperate thread", theBlProxy);
			
			// Create the BL actual object and run it as a seperate thread
			theBL = new GasStationsBL();
			theBL.init();
	
			theBlThread = new Thread(theBL);
			
			theBlThread.start();
		}
		
		return theBlThread;
	}
	
	/* User actions from UI */
	
	/* Add car */
	public void addCar(int id, boolean fAddToPump,  int pumpNum, int numOfLiters, boolean addToCleaning) {
		theLogger.log(Level.INFO, "In BLProxy()::addCar()", this);
		theLogger.log(Level.INFO, "In BLProxy():: Got car addition request", this);
		
		theBL.addCar(id, fAddToPump, pumpNum, numOfLiters, addToCleaning);
	}
	
	/* Add Fuel to main pool */
	public void addFuelToMainPool(int numOfLiters) {
		theLogger.log(Level.INFO, "In BLProxy()::addFuelToMainPool()", this);
		theLogger.log(Level.INFO, "In BLProxy():: Got fuel addition request", this);
		
		theBL.addFuelToMainPool(numOfLiters);
	}
	
	/* Get the number of pumps */
	public int getNumOfPumps() {
		theLogger.log(Level.INFO, "In BLProxy()::getNumOfPumps()", this);
		theLogger.log(Level.INFO, "In BLProxy():: Got num of pumps request", this);
		
		return theBL.getNumOfPumps();
	}
	
	public MainFuelPool getMainFuelPool()
	{
		theLogger.log(Level.INFO, "In BLProxy()::getMainFuelPool()", this);
		theLogger.log(Level.INFO, "In BLProxy():: Got main fuel pool request", this);
		return theBL.getMainFuelPool();
	}
	
	/* Get gas station statistics */
	public GasStationStatistics getGasStationStatistics() {
		
		theLogger.log(Level.INFO, "In BLProxy()::getGasStationStatistics()", this);
		theLogger.log(Level.INFO, "In BLProxy():: Got statistics request", this);
		
		return theBL.getGasStationStatistics();
	}
	
	
	/* close the gas station */
	public void closeGasStation() {
		theLogger.log(Level.INFO, "In BLProxy()::closeGasStation()", this);
		theLogger.log(Level.INFO, "In BLProxy():: Got gas station closing request", this);
		
		theBL.closeGasStation();
	}
	
}
