/**
 * @authors: Amir Krayden
 * @date: 21/03/15 
 */
package dal;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.GasStation;

public class DalProxy {

	private static DalProxy theDalProxy = null;
	private static Logger theLogger;
	
	private GasStationDAL theDAL; 
	
	protected DalProxy() {
		
		// Get the system log object
		theLogger = GasStationUtility.getSystemLog(this);
		
		theLogger.log(Level.INFO, "DALProxy init", this);
		theLogger.log(Level.INFO, "In DalProxy()::DalProxy()", this);
		
	}
	
	public static DalProxy getDalProxy() {
			
		if (theDalProxy == null) {
			theDalProxy = new DalProxy();
			
			theLogger.log(Level.INFO, "Created DALProxy object", theDalProxy);
		}
		
		theLogger.log(Level.INFO, "In DalProxy::getDalProxy()", theDalProxy);
		return theDalProxy;
	}
	
	public void init() {
		theLogger.log(Level.INFO, "In DalProxy::init()", theDalProxy);
		
		// Create the actual DAL object
		theDAL = new GasStationDAL();
		theDAL.init();
	}
	
	public GasStation getGasStationFromConfiguration() {
	
		theLogger.log(Level.INFO, "In DalProxy::getGasStationFromConfiguration()", theDalProxy);
		
		return theDAL.loadGasStationFromConfiguration();
	}
	
}
