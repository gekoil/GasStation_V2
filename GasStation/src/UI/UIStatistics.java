package UI;

import java.util.LinkedList;

import Listeners.UIStatisticsListener;
import Views.StatisticsAbstractView;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;

public class UIStatistics extends FlowPane implements StatisticsAbstractView {
	
	private LinkedList<UIStatisticsListener> listeners;
	private TextArea stat;
	private Button infoBtn;
	
	public UIStatistics() {
		listeners = new LinkedList<UIStatisticsListener>();
		setOrientation(Orientation.VERTICAL);
		stat = new TextArea("Statistics");
		stat.setId("statisTxtArea");
		stat.setScrollLeft(0);
		infoBtn = new Button("Info");
		infoBtn.setOnMousePressed(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				for(UIStatisticsListener l : listeners)
					l.getStatistics();
			}
		});
		getChildren().add(stat);
		getChildren().add(infoBtn);
	}

	@Override
	public void registerListener(UIStatisticsListener lis) {
		listeners.add(lis);
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

}
