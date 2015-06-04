package Controller;

import javax.swing.JOptionPane;

import BL.ClientCar;
import Client.Client;
import Listeners.ClientListener;
import Listeners.ConnectionUIListener;
import Listeners.RegisterUIListener;
import View.AbstractCarsView;
import View.AbstractConnectionView;
import View.AbstractRegisterView;

public class ClientController implements ClientListener, RegisterUIListener,
		ConnectionUIListener {
	private Client client;

	private AbstractCarsView carView;
	private AbstractRegisterView newCarView;
	private AbstractConnectionView connectView;

	public ClientController(Client client, AbstractCarsView carView,
			AbstractRegisterView newCarView, AbstractConnectionView connectView) {
		this.client = client;
		this.client.registerListener(this);
		this.carView = carView;
		this.newCarView = newCarView;
		this.newCarView.registeListener(this);
		this.connectView = connectView;
		this.connectView.registerListener(this);
	}

	@Override
	public void fireNewCar(int fuel, boolean needWash, int pump) {
		ClientCar car = new ClientCar(fuel, needWash, pump);
		client.sendCar(car);
	}

	@Override
	public void updateCarInfo(ClientCar car) {
		carView.carUpdate(car);
	}

	@Override
	public void fireEndOfConection() {
		connectView.setConnectionStatus(false);
	}

	@Override
	public void setConnection(boolean onOff) {
		if(!onOff) {
			ClientCar car = new ClientCar(0, false, 0);
			car.setId(-1);
			client.sendCar(car);
		}
	}

	@Override
	public void fireIlligalObject() {
	}

}
