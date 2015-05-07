package bl;

import gasstation.GasStationUtility;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InsideClean extends CleaningServiceBase {
	
	private static int counter = 0;
	private int id;
	
	private Logger theLogger;
		
	public InsideClean() {
	
		super();
		
		// Create the team id
		id = ++counter;
		
		// get the logger
		theLogger = GasStationUtility.getInsideCleaningServiceLog(this, id);
	
		theLogger.log(Level.INFO, "In InsideClean::InsideClean()", this);
		theLogger.log(Level.INFO, "InsideCleaningTeam init ( id = " + id + " )", this);
	
	}
	
	public int getId() {
		return this.id;
	}

	
	/*
	 * Serve the inside clean
	 */
	protected void serveCar(Car theCar) {
		
		theLogger.log(Level.INFO, "In InsideClean::serveCar() - inside clean car " + theCar.getId(), this);
		
		int cleaningTime = (int)(Math.random() * 100);
		
		theLogger.log(Level.INFO, "In InsideClean::serveCar() - car  " +  theCar.getId() + 
					  " is being cleaned for " + cleaningTime + " ms", this);
		
		try {
			Thread.sleep(cleaningTime);
		}
		catch (InterruptedException e) {
			
		}
		
		theLogger.log(Level.INFO, "In InsideClean::serveCar() - inside clean car " + theCar.getId() + " finished", this);
		
		setBusy(false);
		
		this.doneListener.insideCleanIsDone(theCar);
		
	}

	@Override
	public void run() {
		
		theLogger.log(Level.INFO, "In InsideClean::run()", this);
		theLogger.log(Level.INFO, "InsideCleaningTeam (id = " + getId() + ") - started execution as a seperate thread", this);
		
		// Main waiting loop
		while (fClosed == false) {
			
			Car theCar = null;
			
			// Get the new car
			try {
				theCar = cars.poll(WATING_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
			}
			catch(InterruptedException e) {
				
			}
			
			if (theCar != null) {
				theLogger.log(Level.INFO, "AutoCleaningTeam - car " + theCar.getId() + " Arrived", this);
				serveCar(theCar);
			}
		}
		
		theLogger.log(Level.INFO, "InsideCleaningTeam (id = " + getId() + ") - finished execution as a seperate thread", this);
		
	}
}
