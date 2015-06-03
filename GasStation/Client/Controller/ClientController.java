package Controller;

import BL.ClientCar;
import Client.Client;
import Listeners.ClientListener;
import Listeners.RegisterListener;
import View.AbstractCarsView;
import View.AbstractRegisterView;

public class ClientController implements ClientListener, RegisterListener {

	private Client client;
	private AbstractCarsView carView;
	private AbstractRegisterView newCarView;

	public ClientController(Client client, AbstractCarsView carView,
			AbstractRegisterView newCarView) {
		this.client = client;
		this.client.registerListener(this);
		this.carView = carView;
		this.newCarView = newCarView;
		
	}

	@Override
	public void fireNewCar(int fuel, boolean needFuel, int pump) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCarInfo(ClientCar car) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireIlligalObject() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireMessage(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireEndOfConection() {
		// TODO Auto-generated method stub

	}

}
