package Client;

import BL.ClientCar;

public interface ClientListener {
	void updateCarInfo(ClientCar car);
	void fireIlligalObject();
	void fireMessage(String s);
	void fireEndOfConection();
}
