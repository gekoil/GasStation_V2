package View;

import BL.ClientCar;

public interface AbstractUIView {
	void updateMessage(String s);
	void carUpdate(ClientCar car);
}
