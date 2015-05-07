/**
* @authors: Amir Krayden
* @date: 21/03/15 
*/
package dal;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.GasStation;

public class GasStationDAL {

	private Logger theLogger;
	
	private static final String ConfigXmlFileName = "GasStationsConfig.xml";
	
	
	public GasStationDAL() {
		
		// Get the system log object
		theLogger = GasStationUtility.getSystemLog(this);
				
		theLogger.log(Level.INFO, "GasStationDAL init", this);
		theLogger.log(Level.INFO, "In GasStationDAL::GasStationDAL()", this);
		
	}
	
	public void init() {
		
		theLogger.log(Level.INFO, "In GasStationDAL::init()", this);
		
		
	}
	
	public GasStation loadGasStationFromConfiguration() {
	
		GasStation theGasStation = null;
		
		theLogger.log(Level.INFO, "In GasStationDAL::loadConfiguration()", this);
		
		// Load configuration from XML
		try {
			theGasStation = (new ConfigDOMParser()).ParseXMLFile(ConfigXmlFileName);
			
		}
		catch(Exception exp) {
			exp.printStackTrace();
		}
		
		return theGasStation;
	}
	
}
