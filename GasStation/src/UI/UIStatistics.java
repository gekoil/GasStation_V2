package UI;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Vector;

import DAL.Transaction;
import Listeners.UIStatisticsListener;
import Views.StatisticsAbstractView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class UIStatistics extends FlowPane implements StatisticsAbstractView {
	
	private UIStatisticsListener listener;
	private TextArea stat;
	private Button infoBtn;
	private Button closeBtn;
	private Button historyBtn;
	private HBox dateHbx;
	private Button statByDayBtn;
	private DatePicker startDate;
	private DatePicker endDate;
	private Label fromLbl;
	private Label untilLbl;
	private CheckBox byPumpCbx;
	
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
		createHBox();
		getChildren().add(stat);
		getChildren().add(infoBtn);
		getChildren().add(closeBtn);
		getChildren().add(dateHbx);
	}
	
	private void createHBox() {
		dateHbx = new HBox();
		dateHbx.setSpacing(10);
		
		statByDayBtn = new Button("Get info by date");
		startDate = new DatePicker(LocalDate.now());
		startDate.setMaxWidth(105);
		fromLbl = new Label("From:");
		endDate = new DatePicker(LocalDate.now());
		endDate.setMaxWidth(105);
		untilLbl = new Label("Until:");
		byPumpCbx = new CheckBox("By Pump");
		statByDayBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				LocalDate start = startDate.getValue();
				LocalDate end = endDate.getValue();
				boolean pump = byPumpCbx.isSelected();
				Vector<Transaction> history = listener.getHistory(start, end, pump);
			}
		});
		
		dateHbx.getChildren().addAll(statByDayBtn, fromLbl, startDate, untilLbl, endDate, byPumpCbx);
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
