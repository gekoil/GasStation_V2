package Controller;

import Annotations.DuringWash;
import BL.Car;
import BL.ClientCar;
import BL.ClientsSoketInfo;
import BL.GasStation;
import Client.Client;
import DAL.DatabaseConnector;
import DAL.Transaction;
import Listeners.*;
import Views.CarCreatorAbstractView;
import Views.MainFuelAbstractView;
import Views.StatisticsAbstractView;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class GasStationController implements MainFuelEventListener,
		UIFuelEventListener, StatisticEventListener, UIStatisticsListener,
		UICarCreatorListener, CarsEventListener {

	private static int carId_generator = 9000;
	private static int SERVER_PORT = 9090;

	private boolean serverRunning = true;
	private GasStation gs;
	private DatabaseConnector dbConnector;
	private MainFuelAbstractView fuelView;
	private StatisticsAbstractView statisticView;
	private CarCreatorAbstractView carView;

	private HashMap<String, ClientsSoketInfo> clients;

	public GasStationController(GasStation gs, MainFuelAbstractView FuelView,
			StatisticsAbstractView statisticView, CarCreatorAbstractView carView) {
		this.gs = gs;
		this.fuelView = FuelView;
		this.statisticView = statisticView;
		this.carView = carView;
		this.gs.addFuelPoolListener(this);
		this.gs.addStatisticsListener(this);
		this.gs.addCarEventListener(this);
		this.fuelView.registerListener(this);
		this.statisticView.registerListener(this);
		this.carView.registerListener(this);

		dbConnector = DatabaseConnector.getInstance();

		clients = new HashMap<>();

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
							ClientsSoketInfo clientData = new ClientsSoketInfo(client);
							clients.put(clientData.getClientAddress(), clientData);
							Object carInput;
							do {
								carInput = clientData.getInputStream().readObject();
								if(carInput instanceof ClientCar) {
									// transform to server side Car object
									ClientCar car = (ClientCar) carInput;
									Car result = createNewCar(car.getFuel(),car.isNeedWash(), car.getPump(), clientData);
	
									// respond with client car with ID
									if(result == null)
										clientData.getOutputStream().writeObject(null);
								}
							} while(carInput != null);
						} catch (IOException | ClassNotFoundException e) {
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
	public void fireTheCurrentCapacity(int liters) {
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
	public void getCurrentCapacity() {
		fuelView.updateMainFuelCapacities(gs.getMfpool().getCurrentCapacity());

	}

	@Override
	public Car createNewCar(int liters, boolean wash, int pump, @Nullable ClientsSoketInfo owner) {
		if(liters > gs.getMfpool().getMaxCapacity()) {
			carView.updateErrorMessege("The amount fuel requested is to high!");
			return null;
		}

		if(liters < 0)
			liters = 0;

		Car c = new Car(carId_generator++, wash, liters, pump, gs);
		if(owner != null) {
			c.setOwner(owner);
			try {
				owner.getOutputStream().writeObject(c.toClientCar());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		try {
			gs.enterGasStation(c);
			carView.updateConfirmMessege("You Successfully create new car.\n" + "ID: " + c.getID());
			return c;
		} catch (Exception e) {
			carView.updateErrorMessege(e.getMessage());
			return null;
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

	@Override
	public void getFueled(Car c, Transaction t) {
		if(c.getOwner() != null) {
			ClientsSoketInfo carSocket = c.getOwner();
			if(!carSocket.getSocket().isClosed()) {
				try {
					ClientCar clCar = c.toClientCar();
					clCar.setStatus("Fueled");
					carSocket.getOutputStream().writeObject(clCar);
					//dbConnector.storeTransaction(t);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void getWashed(Car c, Transaction t) {
		if(c.getOwner() != null) {
			ClientsSoketInfo carSocket = c.getOwner();
			if(!carSocket.getSocket().isClosed()) {
				try {
					ClientCar clCar = c.toClientCar();
					if(t == null) {
						List<Method> methods = getMethodsAnnotatedWith(Car.class, DuringWash.class);
						int index = (int) ((Math.random() * 10) % methods.size());
						clCar.setStatus(methods.get(index).getName());
					} else
						clCar.setStatus("Fueled");
					carSocket.getOutputStream().writeObject(clCar);
					//dbConnector.storeTransaction(t);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
		final List<Method> methods = new ArrayList<>();
		final List<Method> allMethods = new ArrayList<>(Arrays.asList(type.getDeclaredMethods()));
		for (final Method method : allMethods) {
			if (method.isAnnotationPresent(annotation)) {
				methods.add(method);
			}
		}
		return methods;
	}

	@Override
	public Transaction getHistory(LocalDate firstDate, LocalDate lastDate, boolean byPump) {
		Vector<?> trans = dbConnector.getTransactions(firstDate, lastDate, byPump);
		return null;
	}

}
