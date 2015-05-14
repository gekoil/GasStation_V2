
import BL.Car;
import BL.CleaningService;
import BL.GasStation;
import BL.MainFuelPool;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.Scanner;

public class Program {
	// these fields are for the menu
	private final static int MENU_SIZE = 4;
	private static Scanner scan = new Scanner(System.in);
	// these are for generating new car IDs/pumpNums for the dynamically new added cars
	private static int carId_generator = 9000; 
	private static int pumpNumToChooseForTheNewCar = 1;

	public static void main(String[] args) {
		// XML DOM-parsed values
		try {
			// reading data from the XML file
			File inputFile = new File("input.txt");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList root = doc.getChildNodes();
			GasStation gs = getGasStationXML(root);
			Node gasStationNode = getNode("GasStation", root);
			Car[] cars = getCarsXML(gasStationNode, gs);

			// inserting the cars(threads) into the gas station
			for (int i = 0; i < cars.length; i++) {
				gs.enterGasStation(cars[i]);
			}
			System.out.println(cars.length
					+ " cars from the XML file entered the gas station!");
			enterMenu(gs);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	} // main
	
	private static void enterMenu(GasStation gs) {
		int option_chosen = 0;
		// The menu runs until we close the gas station
		while (!gs.isGasStationClosing()) {
			try {
				printMenu(gs);
				option_chosen = scan.nextInt();
				switch (option_chosen) {
				case (1):
					optionOne(gs);
					break;
				case (2):
					optionTwo(gs);
					break;
				case (3):
					System.out.println(gs.getStatistics());
					break;
				case (MENU_SIZE):
					gs.closeGasStation();
					break;
				default:
					System.out.println("Wrong input, please enter a number between 1 and " + MENU_SIZE);
				} // case
			} catch (Exception e) {
				System.out.println("Input is wrong!");
				scan.nextLine();
			}
		}
	} // enterMenu
	
	private static void optionOne(GasStation gs) throws InterruptedException {
		System.out.println("How many liters do you want to get? If you don't want, enter 0");
	    int numOfLiters = scan.nextInt();
	    while (numOfLiters < 0 || numOfLiters > gs.getMfpool().getMaxCapacity()) {
			Thread.sleep(500);
	    	System.out.println("Please enter number between 0 and " + gs.getMfpool().getMaxCapacity() + " (Max MainFuelPool Capacity)");
	    	numOfLiters = scan.nextInt();
	    }	    	
	    System.out.println("Do you want to enter the CleanService? Enter 1 if you want, otherwise any other key");
	    boolean wantCleaning;
	    if (scan.nextInt() == 1)
	    	wantCleaning = true;
	    else
	    	wantCleaning = false;
	    Car car = new Car(carId_generator++, wantCleaning, numOfLiters, pumpNumToChooseForTheNewCar++, gs);
	    if (pumpNumToChooseForTheNewCar > gs.getPumps().length)
	        pumpNumToChooseForTheNewCar = (pumpNumToChooseForTheNewCar % gs.getPumps().length) + 1;
	    gs.enterGasStation(car);
	}  // optionOne
	
	private static void optionTwo(GasStation gs) {
		if (!gs.isFillingMainFuelPool()) {
        	System.out.println("Filling the Main Pool with random amount of liters");
            gs.fireFillUPMainFuelPoolEvent();
        }
        else
        	System.out.println("The MainFuelPool is being filled currently, try again later!");	
	}  // optionTwo

	private static void printMenu(GasStation gs) {
		System.out.println("|================================================================================================|");
		System.out.println("| Welcome to the gas station <Best Delek> Fuel Price: " + gs.getPricePerLiter() + "$ CleanService Price: " + gs.getCs().getPrice() + "$ Please enter:|");
		System.out.println("| 1 - to add a new car to the gas station                                                        |");
		System.out.println("| 2 - to fill up the main fuel pool                                                              |");
		System.out.println("| 3 - to show statistics                                                                         |");
		System.out.println("| 4 - to close the gas station and show statistics                                               |");
		System.out.println("|================================================================================================|");
	} // printMenu
	
	// these functions are for XML data parsing
	private static Node getNode(String tagName, NodeList nodes) {
	    for (int i = 0; i < nodes.getLength(); i++) {
	        Node node = nodes.item(i);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            return node;
	        }
	    }
	    return null;
	}  // getNode
	
	private static String getNodeAttr(String attrName, Node node) {
	    NamedNodeMap attrs = node.getAttributes();
	    for (int i = 0; i < attrs.getLength(); i++ ) {
	        Node attr = attrs.item(i);
	        if (attr.getNodeName().equalsIgnoreCase(attrName)) {
	            return attr.getNodeValue();
	        }
	    }
	    return "";
	}  // getNodeAttr
	
	private static GasStation getGasStationXML(NodeList root) {	
		// getting the GasStation Node
		Node gasStationNode = getNode("GasStation", root);
		String numOfPumpsString = getNodeAttr("numOfPumps", gasStationNode);
		int numOfPumps = Integer.parseInt(numOfPumpsString);
		//System.out.print("numOfPumps: " + numOfPumps + "   ");
		String pricePerLiterString = getNodeAttr("pricePerLiter",
				gasStationNode);
		double pricePerLiter = Double.parseDouble(pricePerLiterString);
		//System.out.println("pricePerLiter: " + pricePerLiter);

		// getting the MainFuelPool Node
		Node mainFuelPoolNode = getNode("MainFuelPool",gasStationNode.getChildNodes());
		String maxCapacityString = getNodeAttr("maxCapacity", mainFuelPoolNode);
		int maxCapacity = Integer.parseInt(maxCapacityString);
		//System.out.print("maxCapacity: " + maxCapacity + "   ");
		String currentCapacityString = getNodeAttr("currentCapacity",
				mainFuelPoolNode);
		int currentCapacity = Integer.parseInt(currentCapacityString);
		//System.out.println("currentCapacity: " + currentCapacity);

		// getting the CleaningService Node
		Node cleaningServiceNode = getNode("CleaningService",gasStationNode.getChildNodes());
		String numOfTeamsString = getNodeAttr("numOfTeams", cleaningServiceNode);
		int numOfTeams = Integer.parseInt(numOfTeamsString);
		//System.out.print("numOfTeams: " + numOfTeams + "   ");
		String priceString = getNodeAttr("price", cleaningServiceNode);
		int price = Integer.parseInt(priceString);
		//System.out.print("price: " + price + "   ");
		String secondsPerAutoCleanString = getNodeAttr("secondsPerAutoClean", cleaningServiceNode);
		int secondsPerAutoClean = Integer.parseInt(secondsPerAutoCleanString);
		//System.out.println("secondsPerAutoClean: " + secondsPerAutoClean);

		MainFuelPool pool = new MainFuelPool(maxCapacity, currentCapacity);
		CleaningService cs = new CleaningService(numOfTeams ,price, secondsPerAutoClean);
		GasStation gs = new GasStation(numOfPumps, pricePerLiter, pool, cs);
		return gs;
	}  // getGasStationXML
	
	private static Car[] getCarsXML(Node gasStationNode, GasStation gs) {
		Car[] cars = null;
		// getting the Cars Node
		Node carsNode = getNode("Cars", gasStationNode.getChildNodes());
		Element el = (Element) carsNode;
		// getting the Cars List
		//System.out.println("Cars List:");
		NodeList carsList = el.getElementsByTagName("Car");
		cars = new Car[carsList.getLength()];
		for (int i = 0; i < carsList.getLength(); i++) {
			// getting the Car node
			Node carNode = carsList.item(i);
			String idString = getNodeAttr("id", carNode);
			int id = Integer.parseInt(idString);
			//System.out.print("Car ID: " + id + "   ");
			String wantCleaningString = getNodeAttr("wantCleaning", carNode);
			boolean wantCleaning = Boolean.parseBoolean(wantCleaningString);
			//System.out.print("wantCleaning: " + wantCleaning + "   ");

			// getting the wantsFuel Node
			Node wantsFuelNode = getNode("WantsFuel", carNode.getChildNodes());
			if (wantsFuelNode != null) {
				String numOfLitersString = getNodeAttr("numOfLiters", wantsFuelNode);
				int numOfLiters = Integer.parseInt(numOfLitersString);
				//System.out.print("numOfLiters: " + numOfLiters + "   ");
				String pumpNumString = getNodeAttr("pumpNum", wantsFuelNode);
				int pumpNum = Integer.parseInt(pumpNumString);
				//System.out.println("pumpNum: " + pumpNum);
				cars[i] = new Car(id, wantCleaning, numOfLiters, pumpNum, gs);
			} else {
				//System.out.println();
				cars[i] = new Car(id, wantCleaning, 0, -1, gs);
			}
		}  // for-loop
		return cars;
	} // getCarsXML

}  // Program
