package Controller;

import BL.Car;
import BL.GasStation;
import DAL.DatabaseConnector;
import Listeners.*;
import Views.CarCreatorAbstractView;
import Views.MainFuelAbstractView;
import Views.StatisticsAbstractView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GasStationController implements MainFuelEventListener,
		UIFuelEventListener, StatisticEventListener, UIStatisticsListener,
		UICarCreatorListener {

	private static int carId_generator = 9000;
	private static int SERVER_PORT = 9090;

	private boolean serverRunning = true;
	private GasStation gs;
	private DatabaseConnector dbConnector;
	private MainFuelAbstractView fuelView;
	private StatisticsAbstractView statisticView;
	private CarCreatorAbstractView carView;

	public GasStationController(GasStation gs, MainFuelAbstractView FuelView,
			StatisticsAbstractView statisticView, CarCreatorAbstractView carView) {
		this.gs = gs;
		this.fuelView = FuelView;
		this.statisticView = statisticView;
		this.carView = carView;
		this.gs.addFuelPoolListener(this);
		this.gs.addStatisticsListener(this);
		this.fuelView.registerListener(this);
		this.statisticView.registerListener(this);
		this.carView.registerListener(this);
		this.dbConnector = DatabaseConnector.getInstance();
		this.dbConnector = DatabaseConnector.getInstance();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				initServer();
			}
		};
		new Thread(runnable).start();
	}

	private void initServer() {
		try {
			ServerSocket listener = new ServerSocket(SERVER_PORT);
			while (serverRunning) {
				final Socket client = listener.accept();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ObjectInputStream input  = new ObjectInputStream(client.getInputStream());
							ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
							client.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			listener.close();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public GasStation getGs() {
		return gs;
	}

	public void setGs(GasStation gs) {
		this.gs = gs;
	}

	public MainFuelAbstractView getFuelview() {
		return fuelView;
	}

	public void setFuelview(MainFuelAbstractView fuelview) {
		this.fuelView = fuelview;
	}

	public StatisticsAbstractView getStatisticView() {
		return statisticView;
	}

	public void setStatisticView(StatisticsAbstractView statisticView) {
		this.statisticView = statisticView;
	}

	@Override
	public void theMainFuelIsLow(int liters) {
		if (!gs.isFillingMainFuelPool())
			fuelView.updateTheMainFuelIsLow(liters);
	}

	@Override
	public void finishedFillTheMainFuel(int liters) {
		fuelView.updateFinishedFillingMainFuel(liters);
	}

	@Override
	public void fireFillingTheMainFuel() {
		fuelView.updateFillingTheMainFuel();
	}

	@Override
	public void fireTheMainFuelIsFull() {
		fuelView.updateTheMainFuelIsFull();

	}

	@Override
	public void refill() {
		gs.fireFillUPMainFuelPoolEvent();
	}

	@Override
	public void fireTheCorrentCapacity(int liters) {
		fuelView.updateMainFuelCapacities(liters);
	}

	@Override
	public void getStatistics() {
		statisticView.setStatistics(gs.getStatistics().toString());
	}

	@Override
	public void ShowStatistics(String info) {
		statisticView.setStatistics(info);
	}

	@Override
	public void getCorrentCapacity() {
		fuelView.updateMainFuelCapacities(gs.getMfpool().getCurrentCapacity());

	}

	@Override
	public void createNewCar(int liters, boolean wash, int pump) {
		if(liters > gs.getMfpool().getMaxCapacity()) {
			carView.updateErrorMessege("The amount fuel requested is to high!");
			return;
		}
		if(liters < 0)
			liters = 0;
		Car c = new Car(carId_generator++, wash, liters, pump, gs);
		try {
			gs.enterGasStation(c);
			carView.updateConfirmMessege("You Successfully create new car.");
		} catch (Exception e) {
			carView.updateErrorMessege(e.getMessage());
		}
	}

	@Override
	public void closeGasStation() {
		gs.closeGasStation();
		if(gs.isGasStationClosing()) {
			ShowStatistics("Start closing operation...\nPlease wait for \"end of day\" statistics.");
			statisticView.setDisable();
			fuelView.setDisable();
			carView.setDisable();
		}
		serverRunning = false;
	}

	@Override
	public void fireCantCloseWhileFilling() {
		statisticView.setStatistics("The gas station can't be closed\nwhile filling the main fuel pool.");
	}

}
