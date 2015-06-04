package Listeners;

import BL.Car;

public interface CarsEventListener {
	void GetFueled(Car c);
	void GetWashed(Car c);
}