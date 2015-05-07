package bl;

/*
 * Base class for Business logic classes
 */
public abstract class GasStationBlBase {

	/* Add car */
	public abstract void addCar(int id, boolean fAddToPump, int pumpNum, int numOfLiters, boolean addToCleaning);
	
	/* Add Fuel to main pool */
	public abstract void addFuelToMainPool(int numOfLiters);
	
	/* Get the number of pumps */
	public abstract int getNumOfPumps();
	
	/* Get main fuel pool */
	public abstract MainFuelPool getMainFuelPool();
	
	/* Get gas station statistics */
	public abstract GasStationStatistics getGasStationStatistics();
	
	/* close the gas station */
	public abstract void closeGasStation();
}
