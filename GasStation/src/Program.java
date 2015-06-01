

import BL.CreateGsFromXML;
import BL.GasStation;
import Controller.GasStationController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import UI.CarCreatorPane;
import UI.FuelPane;
import UI.UIStatistics;
import Views.MainFuelAbstractView;


public class Program extends Application {
	
	private GasStation gs;
	private static FuelPane fuelPane;
	private static CarCreatorPane carPane;
	private Label headline;
	private static UIStatistics stat;
	private final String BILD_DATA = "input.xml";
	
	public static void main(String[] args) {
		launch(args);
	} // main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		CreateGsFromXML creator = new CreateGsFromXML(BILD_DATA);
		gs = creator.CreatGasStation();
		primaryStage.setScene(creatScene());
		primaryStage.setTitle("Gas Station");
		
		GasStationController fuelCtrl = new GasStationController(gs, fuelPane, stat);
		
		primaryStage.setMinHeight(500);
		primaryStage.setMinWidth(600);
		primaryStage.show();
	}
	
	public Scene creatScene() {
		BorderPane border = new BorderPane();
		Scene scene = new Scene(border);
		scene.getStylesheets().add(Program.class.getResource("/UI/Style.css").toExternalForm());
		
		fuelPane = new FuelPane();
		fuelPane.setId("mainFuelBox");
		stat = new UIStatistics();
		headline = new Label("WELCOME TO THE GAS STATION");
		carPane = new CarCreatorPane(gs.getPumps().length);
		
		border.setAlignment(headline, Pos.CENTER);
		border.setTop(headline);
		border.setBottom(fuelPane);
		border.setCenter(stat);
		border.setRight(carPane);
		return scene;
	}

	
}  // Program
