package Listeners;

import BL.Car;

public interface CarsEventListener {
	void getFueled(Car c);
	void getWashed(Car c);
}