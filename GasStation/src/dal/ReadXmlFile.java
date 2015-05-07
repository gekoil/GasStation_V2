package dal;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bl.GasStation;
import bl.Car;

public class ReadXmlFile {
	
	private GasStation gasStation;
	
	public ReadXmlFile(String filePath) {
		
		try {
			
			gasStation = new GasStation();
			
			File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			readMainFuelPoolData(doc);
			readPumpData(doc);
			readCleaningServiceData(doc);
			readCarsData(doc);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
	}
	
    public GasStation getGasStation()
    {
    	return gasStation;
    }
    
    private void readPumpData(Document doc){
    	int numOfPumps = 0;
    	double pricePerLiter = 0;
    	
    	NodeList nList = doc.getElementsByTagName("GasStation");
    	Node nNode = nList.item(0);
    	
    	numOfPumps = Integer.parseInt(getAttribute(nNode, "numOfPumps"));
    	pricePerLiter = Double.parseDouble(getAttribute(nNode, "pricePerLiter"));
    	
    	// Add Pumps to Gas Station
    	for(int i=0;i<numOfPumps;i++){
    		gasStation.addPump(pricePerLiter);
    	}
    }
    
    private void readMainFuelPoolData(Document doc) {
    	int maxCapacity = 0;
    	int currentCapacity = 0;
    	
    	NodeList nList = doc.getElementsByTagName("MainFuelPool");
    	Node nNode = nList.item(0);
    	
    	maxCapacity = Integer.parseInt(getAttribute(nNode, "maxCapacity"));
    	currentCapacity = Integer.parseInt(getAttribute(nNode, "currentCapacity"));
    	
    	gasStation.setFuelPool(maxCapacity, currentCapacity);
    }
    
    
    private void readCleaningServiceData(Document doc) {
    	
    	int numOfTeams = 0;
    	int price = 0;
    	int secondsPerAutoClean = 0;
    	
    	NodeList nList = doc.getElementsByTagName("CleaningService");
    	Node nNode = nList.item(0);
    	
    	numOfTeams = Integer.parseInt(getAttribute(nNode, "numOfTeams"));
    	price = Integer.parseInt(getAttribute(nNode, "price"));
    	secondsPerAutoClean = Integer.parseInt(getAttribute(nNode, "secondsPerAutoClean"));
    	
    	gasStation.setCleaningService(numOfTeams, price, secondsPerAutoClean);
    }
    
    private void readCarsData(Document doc) {
    	
    	int id;
    	boolean wantCleaning = false;
    	int numOfLiters=0;
    	int pumpNum=0;
    	
    	NodeList nList = doc.getElementsByTagName("Car");
    
    	for(int i=0;i<nList.getLength();i++)
    	{
    		Node nNode = nList.item(i);
    		id = Integer.parseInt(getAttribute(nNode, "id"));
    		
    		if(getAttribute(nNode, "wantCleaning") !=null) {
    			
    			wantCleaning = Boolean.parseBoolean(getAttribute(nNode, "wantCleaning"));
    		}
    		
    		if(nNode.hasChildNodes()) { // wants fuel
    			
    			NodeList childList = nNode.getChildNodes();
    			Node childNode = childList.item(1);
    			
    			numOfLiters = Integer.parseInt(getAttribute(childNode, "numOfLiters"));
    			pumpNum =  Integer.parseInt(getAttribute(childNode, "pumpNum"));
    		}
    		
    		if(wantCleaning && pumpNum == 0) {
    			
    			gasStation.addCar(new Car(id, wantCleaning));
        	}
    		else if (!wantCleaning && pumpNum !=0) {
    			
    			gasStation.addCar(new Car(id, numOfLiters, pumpNum));
    		}
    		else {
    			gasStation.addCar(new Car(id, numOfLiters, pumpNum, wantCleaning));
    		}
    		
    		wantCleaning = false;
    		pumpNum = 0;
    	}
    	
    }
    
    private String getAttribute(Node nNode,String name) {
    	
    	// get a map containing the attributes of this node
    	NamedNodeMap attributes = nNode.getAttributes();
    	
    	// get the number of nodes in this map
    	int numAttrs = attributes.getLength();
    	
    	for (int i = 0; i < numAttrs; i++) {
    		
    		Attr attr = (Attr) attributes.item(i);
    		String attrName = attr.getNodeName();
    		String attrValue = attr.getNodeValue();
            
    		if(attrName == name) {
    			return attrValue;
    		}
    	}
    	
    	return null;
    }
}
