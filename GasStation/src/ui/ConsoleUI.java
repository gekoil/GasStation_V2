package ui;

import bl.BlProxy;
import bl.GasStationStatistics;
import bl.MinTarget_Observer;
import gasstation.GasStationUtility;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConsoleUI extends MinTarget_Observer implements Runnable {
	private BlProxy bl;
	private boolean exitMenu;
	private boolean minTargetMessage;
	
	private Logger theLogger;
	
	private Scanner s;
	
	public ConsoleUI() {
		bl = BlProxy.getBlProxy();
		
		this.subject = bl.getMainFuelPool();
		this.subject.attach(this);
		minTargetMessage = false;
		
		
		// Get the system log object
		theLogger = GasStationUtility.getSystemLog(this);
				
		theLogger.log(Level.INFO, "ConsoleUI init", this);
		theLogger.log(Level.INFO, "In ConsoleUI()::ConsoleUI()", this);
	}
	
	public Thread init() {
		theLogger.log(Level.INFO, "In ConsoleUI()::init()", this);
		theLogger.log(Level.INFO, "In ConsoleUI()::init() -  running  the UI as a separate thread", this);
		
		Thread t = new Thread(this);
		t.start();
		
		return t;
	}
	
	public void run() {
		
		theLogger.log(Level.INFO, "In ConsoleUI()::run()", this);
		theLogger.log(Level.INFO, "In ConsoleUI()::run() -  Started running as a separate thread", this);
		
		bl = BlProxy.getBlProxy();
		
		s = new Scanner(System.in);
		int selection;
		
		do{
			showMenu();			
			selection = s.nextInt();
			doSelection(selection);					
		}while(!exitMenu);
		
		// Send the close command to the blProxy
		bl.closeGasStation();
		
		s.close();
		
		theLogger.log(Level.INFO, "In GasStation()::run() -  Thread is closing", this);
	}
	
	
	public void showMenu() {
		System.out.println("\nChoose your action: ");
		System.out.println("Press 1 : for adding car to pump or to cleaning service \n"
				+  "Press 2 : for filling main fuel pool \n"
				+  "Press 3 : for showing gas station statistics \n"
				+  "Press 4 : for exit gas station and showing gas station statistics");		
	}
	
	public void doSelection(int selection) {
		if(selection == 1)
			addNewCarToGasStation();
		else if(selection == 2)
			fillingMainFuelPool();
		else if(selection == 3)
			showStatistics();
		else if(selection == 4){
			exitMenu = true;
			bl.closeGasStation();
			System.out.println("Closing gas station");
			showStatistics();
		}
	}
	
	public void addNewCarToGasStation() {
		
		int select;
		int id;
		int numOfPump = 0;
		int numOfLiters = 0;
		boolean fFuel = false;
		boolean fClean = false;
		
		
		id = (int)(Math.random() * (999 - 100)) + 100;
		
		System.out.println("press 1 : for adding car to pump\n"
						+  "press 2 : for adding car to cleaning service\n"
						+  "press 3 : for adding car to pump and cleaning service");
		
		select = s.nextInt();
		
		numOfLiters = (int)(Math.random()*50) + 1;
		numOfPump = (int)(Math.random()*bl.getNumOfPumps()) + 1;
		
		if(select == 1) {
			fFuel = true;
			System.out.println("car " + id + " was added to pump " + numOfPump);
		}
		if(select == 2) {
			fClean = true;
			System.out.println("car " + id + " was added to cleaning service ");
		}
		if(select == 3) {
			fFuel = true;
			fClean = true;
			System.out.println("car " + id + " was added to pump " + numOfPump + " and added to cleaning service ");
		}
		
		bl.addCar(id, fFuel, numOfPump, numOfLiters, fClean);
		
	}
	
	public void showStatistics() {
		GasStationStatistics statistics = bl.getGasStationStatistics();
		
		System.out.println("Number of cars that fueled : " + statistics.getNumOfCarsThatFueled());
		System.out.println("Number of cars that was cleaned : " + statistics.getNumOfCarsThatWasCleaned());
		System.out.println("Pumps revenue :" + statistics.getPumpsRevenew());
		System.out.println("Cleaning revenue :" + statistics.getCleaningRevenew());
	}
	
	public void fillingMainFuelPool() {
		
		int numOfLiters;
		
		System.out.println("Please enter number of liters :");
		numOfLiters = s.nextInt();
		
		bl.addFuelToMainPool(numOfLiters);
		minTargetMessage = false;
	}

	@Override
	public void update() {
		
		if(!minTargetMessage) {
			System.out.println("Gas is under 20% need to order fuel");
			minTargetMessage = true;
		}
	}
	
}
