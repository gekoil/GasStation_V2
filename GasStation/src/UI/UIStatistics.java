package UI;

import java.util.LinkedList;

import Listeners.UIStatisticsListener;
import Views.StatisticsAbstractView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;

public class UIStatistics extends FlowPane implements StatisticsAbstractView {
	
	private UIStatisticsListener listener;
	private TextArea stat;
	private Button infoBtn;
	private Button closeBtn;
	
	public UIStatistics() {
		setOrientation(Orientation.VERTICAL);
		setHgap(15);
		setVgap(10);
		stat = new TextArea("Statistics");
		stat.setId("statisTxtArea");
		stat.setScrollLeft(0);
		infoBtn = new Button("Info");
		infoBtn.setOnMousePressed(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				listener.getStatistics();
			}
		});
		closeBtn = new Button("Close Station");
		closeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						listener.closeGasStation();
					}
				});
			}
		});
		getChildren().add(stat);
		getChildren().add(infoBtn);
		getChildren().add(closeBtn);
	}

	@Override
	public void registerListener(UIStatisticsListener lis) {
		listener = lis;
	}

	@Override
	public void setStatistics(String info) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stat.setText(info);
			}
		});
	}

	@Override
	public void setDisable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				infoBtn.setDisable(true);
				closeBtn.setDisable(true);
			}
		});
	}

}
