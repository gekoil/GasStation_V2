package Controller;

import BL.Car;
import BL.GasStation;
import Listeners.MainFuelEventListener;
import Listeners.StatisticEventListener;
import Listeners.UICarCreatorListener;
import Listeners.UIFuelEventListener;
import Listeners.UIStatisticsListener;
import Views.MainFuelAbstractView;
import Views.StatisticsAbstractView;

public class GasStationController implements MainFuelEventListener,
		UIFuelEventListener, StatisticEventListener, UIStatisticsListener,
		UICarCreatorListener {

	private static int carId_generator = 9000;
	private GasStation gs;
	private MainFuelAbstractView fuelView;
	private StatisticsAbstractView statisticView;

	public GasStationController(GasStation gs, MainFuelAbstractView Fuelview,
			StatisticsAbstractView statisticView) {
		this.gs = gs;
		this.fuelView = Fuelview;
		this.statisticView = statisticView;
		this.gs.addFuelPoolListener(this);
		this.fuelView.registerListener(this);
		this.statisticView.registerListener(this);
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
	public void createNewCar(int liters, boolean wash) {
		int pumpNum = gs.getPumps().length;
		pumpNum = (int)(Math.random()*pumpNum);
		Car c = new Car(carId_generator++, wash, liters, pumpNum, gs);
	}

}
