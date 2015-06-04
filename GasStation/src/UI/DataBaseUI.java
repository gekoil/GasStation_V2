package UI;

import BL.GasStationServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DataBaseUI extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane border = new BorderPane();
		Scene scene = new Scene(border);
		scene.getStylesheets().add(GasStationServer.class.getResource("/UI/Style.css").toExternalForm());
		stage.setScene(scene);
		stage.setMinHeight(500);
	    stage.setMinWidth(600);
	    stage.show();
	}
	
}
