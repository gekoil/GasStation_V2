/*
 * Amir Krayden 11/04/15
 */
package bl;

/*
 * This interface allows for asynchronic callback events from the cleaning services 
 */

public interface CleaningDoneIF {

	public void autoCleanIsDone(Car car);
	public void insideCleanIsDone(Car car);
	
}
