/**
 * @authors: Amir Krayden
 * @date: 21/03/15 
 */
package gasstation;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class GasStationUtility {

	public static final String SystemLog = "SystemLog";
	public static final String SystemLogFileName = "SystemLog.log";
	
	private static Logger theLogger = null;
	private static FileHandler theSystemHandler = null;
	private static ObjectArrayFilter theSystemFilter = null;
	
	private static ObjectArrayFilter theCleaningFilter = null;
	
	public static void loggerInit() {
		
		if (theLogger == null) {
			theLogger = Logger.getLogger(GasStationUtility.SystemLog);
			
			try {
				theSystemHandler = new FileHandler(GasStationUtility.SystemLogFileName);
				theSystemHandler.setFormatter(new LogFormatter());
				theSystemFilter = new ObjectArrayFilter();
				theSystemHandler.setFilter(theSystemFilter);
				theLogger.setUseParentHandlers(false);
				theLogger.addHandler(theSystemHandler);
			}
			catch (SecurityException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * Get the system log
	 */
	public synchronized static Logger getSystemLog(Object theObject) {
		
		loggerInit();
		
		addFilterToSystemLog(theObject);
	
		return theLogger;
	}
	
	private static void addFilterToSystemLog(Object theObject) {
		
		theSystemFilter.addFilteredObject(theObject);
		
	}
	

	/*
	 * GetSpecificLog
	 */
	public static Logger getSpecificLog(Object object, String logName) {
		
		FileHandler theHandler;
		
		loggerInit();
		
		try {
			theHandler = new FileHandler(logName);
			theHandler.setFilter(new ObjectFilter(object));
			theHandler.setFormatter(new LogFormatter());
			theLogger.addHandler(theHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return theLogger;
	}
	
	/*
	 * Get the pump log
	 */
	public static Logger getPumpLog(Object object,int id) {
		
		return getSpecificLog(object, "Pump" + id + ".log");
	}
	
	/*
	 * Get the car log
	 */
	public static Logger getCarLog(Object object,int id) {
		
		return getSpecificLog(object, "Car" + id + ".log");
	}
	
	/*
	 * Get the cleaning service log
	 */
	public static Logger getInsideCleaningServiceLog(Object object,int id) {
		
		return getSpecificLog(object, "InsideCleaningTeam" + id + ".log");
	}
	
	/*
	 * Get the cleaning service log
	 */
	public static Logger getCleaningServiceLog(Object object) {
		
		FileHandler theHandler;
		
		loggerInit();
		
		if (theCleaningFilter == null) {
			try {
				theHandler = new FileHandler("CleaningService.log");
				theCleaningFilter = new ObjectArrayFilter();
				theHandler.setFilter(theCleaningFilter);
				theHandler.setFormatter(new LogFormatter());
				theLogger.addHandler(theHandler);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Add the object to the filtered objects array
		theCleaningFilter.addFilteredObject(object);
		
		return theLogger;
	}
}
