/**
 * @authors: Amir Krayden
 * @date: 21/03/15 
 */
package gasstation;

import java.util.ArrayList;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ObjectArrayFilter implements Filter {

private ArrayList<Object> filtered;
	
	public ObjectArrayFilter() {
		filtered = new ArrayList<Object>();
	}
	
	// Add an object to the filtered collection
	public void addFilteredObject(Object object) {
		
		filtered.add(object);
		
	}
	
	@Override
	public boolean isLoggable(LogRecord rec) {
		if (rec.getParameters() != null) {
			Object temp = rec.getParameters()[0];
			return filtered.contains(temp);
		}
		else
			return false;
	}

}
