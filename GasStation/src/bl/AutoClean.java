package bl;

import gasstation.GasStationUtility;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoClean extends CleaningServiceBase {
	private int secondsPerAutoClean;
	private Logger theLogger = null;	
	
	public AutoClean(int secondsPerAutoClean) {
		
		super();
		this.secondsPerAutoClean = secondsPerAutoClean;
		
		theLogger = GasStationUtility.getCleaningServiceLog(this);
		
		theLogger.log(Level.INFO, "In AutoClean::AutoClean()", this);
		theLogger.log(Level.INFO, "AutoCleaningTeam init", this);
	}
	
	
	protected void serveCar(Car theCar) {
		
		theLogger.log(Level.INFO, "In AutoClean::serveCar()", this);
		
		theLogger.log(Level.INFO, "In AutoClean::serveCar() - Auto clean car " + theCar.getId(), this);
		
		
		// Clean the car by sleeping for a fixed time
		try {
			Thread.sleep(secondsPerAutoClean * 1000);
		}
		catch (InterruptedException e) {
			
		}
		
		theLogger.log(Level.INFO, "In AutoClean::serveCar() - Auto clean car " + theCar.getId() + " finished", this);
		
		setBusy(false);
		
		this.doneListener.autoCleanIsDone(theCar);
	}
	
	// Thread entry point
	@Override
	public void run() {
		
		theLogger.log(Level.INFO, "In AutoClean::run()", this);
		theLogger.log(Level.INFO, "AutoCleaningTeam - started execution as a seperate thread", this);
		
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
		
		theLogger.log(Level.INFO, "AutoCleaningTeam - finished execution as a seperate thread", this);
	}
}
