package bl;

import gasstation.GasStationUtility;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CleaningService implements Runnable, CleaningDoneIF {

	private AutoClean autoClean;
	private ArrayList<InsideClean> insideCleanArr;
	private ArrayList<Thread> insideCleanThreads = null;
	
	private int numOfCarsServed;
	private Object numOfCarsServedMutex;
	
	private int numOfInsideTeams;
	private int price;
	
	private BlockingQueue<Car>cars;
	private static final int WATING_QUEUE_LEN = 10;
	private static final int WATING_QUEUE_TIMEOUT = 10; // MS
	
	private Queue<Car> autoCleaningWaitingQueue;
	
	private Queue<Car> insideCleaningWaitingQueue;
	private Object insideCleaningWaitingQueueMutex;
	
	private Logger theLogger;
	private boolean fClosed;
	
	private Thread autoCleanThread;
	
	public CleaningService(int numOfTeams, int price, int secondsPerAutoClean) {
		
		insideCleanArr = new ArrayList<InsideClean>();
		this.numOfInsideTeams = numOfTeams;
		this.price = price;
		
		this.numOfCarsServed = 0;
		numOfCarsServedMutex = new Object();
		
		cars = new ArrayBlockingQueue<Car>(WATING_QUEUE_LEN);
		
		autoCleaningWaitingQueue		= new LinkedBlockingQueue<Car>();
		insideCleaningWaitingQueue 		= new LinkedBlockingQueue<Car>();
		insideCleaningWaitingQueueMutex	= new Object();
		
		fClosed = false;
		
		// get the logger
		theLogger = GasStationUtility.getCleaningServiceLog(this);
		
		theLogger.log(Level.INFO, "In CleaningService::CleaningService()", this);
		theLogger.log(Level.INFO, "CleaningService init", this);
		
		createAutoCleaningTeam(secondsPerAutoClean);
		createInsideCleaningTeams(numOfTeams);
	}
	
	public void createAutoCleaningTeam(int secondsPerAutoClean) {
		
		theLogger.log(Level.INFO, "In CleaningService::createAutoCleaningTeam()", this);
		autoClean = new AutoClean(secondsPerAutoClean);
		
	}
	
	public void createInsideCleaningTeams(int numOfTeams) {
		
		theLogger.log(Level.INFO, "In CleaningService::createAutoCleaningTeam()", this);
		
		numOfInsideTeams = numOfTeams;
		for (int i = 0; i < numOfInsideTeams; i++) {
			insideCleanArr.add(new InsideClean());
		}
		
		theLogger.log(Level.INFO, "In CleaningService::createAutoCleaningTeam() - created " + numOfTeams
					 + " inside cleaning teams", this);
	}
	
	public void cleaning(Car car) {
		// Send the car to the auto clean and then to the inside cleaning
		theLogger.log(Level.INFO, "In CleaningService::cleaning()", this);
		theLogger.log(Level.INFO, "In CleaningService::cleaning() - car " + car.getId() + " wants to enter", this);
		
		try {
			cars.put(car);
		}
		catch (InterruptedException e) {
			
		}
		
		theLogger.log(Level.INFO, "In CleaningService::cleaning() - car " + car.getId() + " is in queue", this);
		
	}

	public void closeCleaningService() {
		
		theLogger.log(Level.INFO, "In CleaningService::closeCleaningService()", this);
		fClosed = true;
	}
	
	/*
	 * start threads
	 */
	private void startServiceThreads() {
		
		theLogger.log(Level.INFO, "In CleaningService::startServiceThreads()", this);
		
		// Create the auto clean threads
		theLogger.log(Level.INFO, "In CleaningService::startServiceThreads() - starting auto clean thread", this);
		autoCleanThread = new Thread(autoClean);
		autoCleanThread.start();
		
		// Create inside teams threads
		insideCleanThreads = new ArrayList<Thread>();
		
		for (InsideClean i : insideCleanArr) {
			theLogger.log(Level.INFO, "In CleaningService()::startServiceThreads() - creating thread for inside team: " + i.getId(), this);
			Thread t = new Thread(i);
			insideCleanThreads.add(t);
			t.start();
			theLogger.log(Level.INFO, "In CleaningService()::startServiceThreads() - created thread for inside team: " + i.getId(), this);
		}
	}
	
	/*
	 * start threads
	 */
	private void stopServiceThreads() {
		
		theLogger.log(Level.INFO, "In CleaningService::stopServiceThreads()", this);
		
		// Send the close command to the auto cleaning team
		autoClean.closeService();
		
		// Wait for the thread to finish
		try {
			autoCleanThread.join();
		}
		catch (InterruptedException e) {
			
		}
		
		// Send close commands to the inside cleaning teams
		for(InsideClean i : insideCleanArr) {
			i.closeService();
		}

		// Wait for the inside cleaning teams threads
		try {
			for (Thread t : insideCleanThreads) {
				t.join();
			}
		}
		catch(InterruptedException e) {
			
		}
		
		theLogger.log(Level.INFO, "In CleaningService::stopServiceThreads() - service threads finished", this);
	}
	
	// Get the next car
	private Car getNextCar() {
		
		Car nextCar = null;
		
		// Try to get the next car from the queue or wait for it
		try {
			nextCar = cars.poll(WATING_QUEUE_TIMEOUT, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			
		}
		
		if (nextCar != null)
			theLogger.log(Level.INFO, "In CleaningService::getNextCar() - car " + nextCar.getId() + " is ready to be served", this);
		
		return nextCar;
	}
	
	private void serveAutoClean(Car nextCar) {
		
	
		if (autoClean.isBusy() == false) {
			// Send the car to auto clean
			theLogger.log(Level.INFO, "In CleaningService::run() - sending car " + nextCar.getId()
					 + " to auto cleaning", this);
			autoClean.cleaning(nextCar,this);
		}
		else {
			theLogger.log(Level.INFO, "In CleaningService::run() - sending car " + nextCar.getId()
					 + " to auto cleaning queue", this);
			autoCleaningWaitingQueue.add(nextCar);
		}
		
	}
	
	// Get the first team ready for job
	private InsideClean getFirstReadyInsideTeam() {
		
		InsideClean first = null;
		
		for (InsideClean i : insideCleanArr) {
			if (i.isBusy() == false) {
				first = i;
			}
		}
		
		return first;
		
	}
	
	// serve inside cleaning
	private void serveInsideClean() {
		
		boolean fTeamsFull = false;
		
		// Check whether there are cars waiting for inside cleaning
		synchronized (insideCleaningWaitingQueueMutex) {
			
			while (insideCleaningWaitingQueue.size() > 0 && fTeamsFull == false) {
				
				theLogger.log(Level.INFO, "In CleaningService::serveInsideClean() - there are " + 
						insideCleaningWaitingQueue.size() + " cars wating for inside cleaning", this);
				
				InsideClean team = getFirstReadyInsideTeam();
				
				if (team != null) {
					
					Car car = insideCleaningWaitingQueue.poll();
					
					theLogger.log(Level.INFO, "In CleaningService::serveInsideClean() - sending car " + 
								  car.getId() + " to team " + team.getId(), this);
					
					team.cleaning(car, this);
				}
				else {
					fTeamsFull = true;
					theLogger.log(Level.INFO, "In CleaningService::serveInsideClean() - all inside cleaning teams are busy", this);
				}
				
			}
			
		}
	}
	
	@Override
	public void autoCleanIsDone(Car car) {
		
		// Handle the auto clean is done event by adding antoher to the auto clean service
		theLogger.log(Level.INFO, "In CleaningService::autoCleanIsDone()", this);
		theLogger.log(Level.INFO, "In CleaningService::autoCleanIsDone() - car " + car.getId() + 
					  " finished the auto clean", this);

		//Send the car to inside cleaning
		insideCleaningWaitingQueue.add(car);
		
		theLogger.log(Level.INFO, "In CleaningService::run() - car " + car.getId()
				 + " is added to inside cleaning queue", this);
		
		// Get the next car if exists
		Car next = autoCleaningWaitingQueue.poll();
		
		if (next != null) {
			
			theLogger.log(Level.INFO, "In CleaningService::run() - sending car " + next.getId()
					+ " to auto cleaning", this);
			
			autoClean.cleaning(next, this);
		}
	}
	
	@Override
	public void insideCleanIsDone(Car car) {

		// Handle the inside clean is done event by marking the car cleaning as finished
		theLogger.log(Level.INFO, "In CleaningService::insideCleanIsDone()", this);
		theLogger.log(Level.INFO, "In CleaningService::insideCleanIsDone() - car " + car.getId() + 
					  " finished the inside clean", this);
		
		synchronized (numOfCarsServedMutex) {
			numOfCarsServed++;
		}
		
		// Mark as finished
		car.finishCleaning();
		
		// Allow more cars to be handled
		serveInsideClean();
	}
	
	// Cleaning service thread entry point
	@Override
	public void run() {
		
		theLogger.log(Level.INFO, "In CleaningService::run()", this);
		theLogger.log(Level.INFO, "In CleaningService::run() - started running as a seperate thread", this);
		
		// Start service threads
		startServiceThreads();
		
		// Main loop
		while (fClosed == false) {
			
			// Give inside clean to the cars that are waiting for it
			serveInsideClean();
			
			Car nextCar = getNextCar();
			
			// Serve the next car if exits
			if (nextCar != null) {
				serveAutoClean(nextCar);
			}
		}
		
		// Stop service threads
		stopServiceThreads();
		
		theLogger.log(Level.INFO, "In CleaningService::run() - Cleaning service is now closed", this);
	}
	
	// Current number of cars served
	public int getNumOfCarsServed() {
		int num;
		
		synchronized (numOfCarsServedMutex) {
			num = this.numOfCarsServed;
		}
		
		return num;
	}
	
	// Total profit calculation
	public float getTotalProfit() {
		return (getNumOfCarsServed() * price);
	}
}
