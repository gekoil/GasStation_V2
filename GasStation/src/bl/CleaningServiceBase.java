package bl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class CleaningServiceBase implements Runnable {

	protected boolean isBusy;
	protected boolean fClosed;
	
	protected BlockingQueue<Car> cars;
	protected static final int WATING_QUEUE_LEN = 1;
	protected static final int WATING_QUEUE_TIMEOUT = 10; // ms
	
	protected Object theBusyMutex;
	protected CleaningDoneIF doneListener;
	
	public CleaningServiceBase() {
		this.fClosed = false;
		this.isBusy = false;
		
		theBusyMutex = new Object();
		
		// One car at a time
		cars = new ArrayBlockingQueue<Car>(WATING_QUEUE_LEN);
	}
	
	public boolean isBusy() {
		
		boolean busy;
		
		synchronized (theBusyMutex) {
			busy = this.isBusy;
		}
		
		return busy;
	}
	
	public void setBusy(boolean busy) {
		
		synchronized (theBusyMutex) {
			this.isBusy = busy;
		}
		
	}
	
	public void cleaning(Car car, CleaningDoneIF doneListener) {
		
		setBusy(true);
		
		// Get the car for cleaning
		this.doneListener = doneListener;
		
		try {
			cars.put(car);
		}
		catch(InterruptedException e) {
			
		}
	}
	
	public void closeService() {
		this.fClosed = true;
	}
	
	
	protected abstract void serveCar(Car theCar);
	
}
