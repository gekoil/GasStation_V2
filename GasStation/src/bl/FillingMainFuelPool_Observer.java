package bl;

public abstract class FillingMainFuelPool_Observer {
	protected MainFuelPool subject;
	
	public abstract void updateMainPumpStartedFueling();
	public abstract void updateMainPumpFinishedFueling();
}
