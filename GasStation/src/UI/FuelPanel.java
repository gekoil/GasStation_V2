package UI;

import Listeners.MainFuelAbstractListener;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class FuelPanel extends Pane implements MainFuelAbstractListener {
	
	private Label needFuel;
	
	public FuelPanel() {
		new JFXPanel(); // needed to initialize JavaFX toolkit
		Label headLine = new Label("Main Fuel");
		needFuel = new Label("");
		Button refillBtn = new Button("Refill tank");
		BorderPane mainFuel = new BorderPane();
		mainFuel.setTop(headLine);
		mainFuel.setCenter(refillBtn);
		mainFuel.setBottom(needFuel);
		getChildren().add(mainFuel);
	}

	@Override
	public void theMainFuelCapacities(int liters) {
		needFuel.setText("The \"Main fuel pool\" capacity is " + liters + ".");
	}

	@Override
	public void updateTheMainFuelIsLow() {
		needFuel.setText("The Main fuel pool is low! ");
		System.out.println("here");
	}

}
