/**
 * @authors: Amir Krayden
 * @date: 21/03/15 
 */
package gasstation;

import java.util.logging.Level;
import java.util.logging.Logger;

import ui.ConsoleUI;
import bl.BlProxy;


public class GasStaionMain {

	protected static GasStaionMain theGasStation = null; 

	private Logger theLogger;
	
	/* 
	 * Main function
	 */
	public static void main(String[] args) {
		
		GasStaionMain.getObject().run();
		
	}
	
	
	/* 
	 * Singletone get function
	 */
	public static GasStaionMain getObject() {
		
		if (theGasStation == null) {
			theGasStation = new GasStaionMain();
		}
		
		return theGasStation;
	}
	
	/*
	 * Inner constructor
	 */
	protected GasStaionMain() {
		
		// Get the system log object
		theLogger = GasStationUtility.getSystemLog(this);
		
		theLogger.log(Level.INFO, "System init", this);
		theLogger.log(Level.INFO, "In GasStationMain::GasStationMain()", this);
		
	}
	
	/*
	 * UI & BL changeover logic
	 */
	private void run() {
		
		theLogger.log(Level.INFO, "In GasStationMain::run()", this);
		
		theLogger.log(Level.INFO, "In GasStationMain::run() - init the BlProxy", this);
		
		// Init the BLProxy
		Thread blThread = BlProxy.getBlProxy().runThread();
	
		System.out.println("System Started");
		System.out.println("Logic threads started");
		
		// Init and start the UI
		theLogger.log(Level.INFO, "In GasStationMain::run() - init the UI", this);
		System.out.println("UI threads started");
		
		ConsoleUI ui = new ConsoleUI();
		Thread uiThread = ui.init();
		
		try {
			blThread.join();
			uiThread.join();
		}
		catch(InterruptedException e) {
			
		}
		
		theLogger.log(Level.INFO, "In GasStationMain::run() - all threads finished", this);
		
		System.out.println("System Done");
		
	}
	
}
