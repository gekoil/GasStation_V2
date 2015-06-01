package Listeners;

public interface MainFuelAbstractView {
	void registerListener(UIFuelEventListener lis);
	void updateMainFuelCapacities(int liters);
	void updateTheMainFuelIsLow(int liters);
	void updateTheMainFuelIsFull();
	void updateFinishedFillingMainFuel(int liters);
	void updateFillingTheMainFuel();
}
