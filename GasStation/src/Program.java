


import BL.GasStation;
import Listeners.MainFuelAbstractListener;
import UI.FuelPanel;
import UI.Statistics;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Program extends Application {
	
	private GasStation gs;
	private Pane fuelPane;
	private Pane stat;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane border = new BorderPane();
		Scene scene = new Scene(border);
		scene.getStylesheets().add(Program.class
				.getResource("Style.css").toExternalForm());
		
		primaryStage.setMinHeight(500);
		primaryStage.setMinWidth(600);
		
		fuelPane = new FuelPanel();
		fuelPane.setId("mainFuelBox");
		stat = new Statistics();
		border.setTop(new Label(""));
		border.setBottom((Node) fuelPane);
		border.setCenter(stat);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public MainFuelAbstractListener getMainFuelPane() {
		return (MainFuelAbstractListener) fuelPane;
	}

}
