/**
 * @authors: Amir Krayden
 * @date: 21/03/15 
 */
package dal;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.GasStation;


public class ConfigDOMParser {

	private Logger theLogger;
	
	public ConfigDOMParser() {
		// Get the system logger
		theLogger = GasStationUtility.getSystemLog(this);
	}
	
	public GasStation ParseXMLFile(String filename) throws Exception {

		theLogger.log(Level.INFO, "In ConfigDOMParser::ParseXMLFile()");
		theLogger.log(Level.INFO, "ConfigurationDOMParser: Loading XML file: " + filename);
		
		ReadXmlFile rdxml = new ReadXmlFile("GasStationsConfig.xml");
		
		GasStation gasStation = rdxml.getGasStation();
	    
	    theLogger.log(Level.INFO, "ConfigurationDOMParser: XML file loaded and parsed");
	    
	    return gasStation;
	    
	}
	 
}
