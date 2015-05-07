/*
 * 
 */
package bl;

/* Gas station statistics class */
public class GasStationStatistics {

	private int 	numOfCarsThatFueled;
	private int 	numOfCarsThatWasCleaned;
	private float 	pumpsRevenew;
	private float	cleaningRevenew;
	
	
	public int getNumOfCarsThatFueled() {
		return numOfCarsThatFueled;
	}
	public void setNumOfCarsThatFueled(int numOfCarsThatFueled) {
		this.numOfCarsThatFueled = numOfCarsThatFueled;
	}
	public int getNumOfCarsThatWasCleaned() {
		return numOfCarsThatWasCleaned;
	}
	public void setNumOfCarsThatWasCleaned(int numOfCarsThatWasCleaned) {
		this.numOfCarsThatWasCleaned = numOfCarsThatWasCleaned;
	}
	public float getPumpsRevenew() {
		return pumpsRevenew;
	}
	public void setPumpsRevenew(float pumpsRevenew) {
		this.pumpsRevenew = pumpsRevenew;
	}
	public float getCleaningRevenew() {
		return cleaningRevenew;
	}
	public void setCleaningRevenew(float cleaningRevenew) {
		this.cleaningRevenew = cleaningRevenew;
	}
	
}
