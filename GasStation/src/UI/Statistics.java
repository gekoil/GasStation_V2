package UI;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;

public class Statistics extends FlowPane {
	
	public Statistics() {
		setOrientation(Orientation.VERTICAL);
		TextArea stat = new TextArea("Statistics");
		stat.setId("statisTxtArea");
		Button infoBtn = new Button("Info");
		infoBtn.setOnMousePressed(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				
			}
		});
		getChildren().add(stat);
		getChildren().add(infoBtn);
	}

}
