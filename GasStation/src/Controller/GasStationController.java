package Controller;

import BL.GasStation;
import Listeners.MainFuelAbstractListener;
import Listeners.MainFuelEventListener;

public class GasStationController implements MainFuelEventListener {
	
	private GasStation gs;
	private MainFuelAbstractListener view;
	
	public GasStationController(GasStation gs, MainFuelAbstractListener view) {
		this.gs = gs;
		this.view = view;
		
		this.gs.addFuelPoolListener(this);
	}

	@Override
	public void theMainFuelIsLow() {
		view.updateTheMainFuelIsLow();
	}

	@Override
	public void finishedFillTheMainFuel() {
		// TODO Auto-generated method stub
		
	}
	
}
