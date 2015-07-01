package UI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Vector;

import DAL.ServiceType;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class UIStatistics extends FlowPane implements StatisticsAbstractView {

	private UIStatisticsListener listener;
	private TextArea stat;
	private Button infoBtn;
	private Button closeBtn;
	private HBox dateHbx;
	private HBox sectionHbx;
	private Button statByDayBtn;
	private DatePicker startDate;
	private DatePicker endDate;
	private Label fromLbl;
	private Label untilLbl;
	private ComboBox startHourCmb;
	private ComboBox endHourCmb;
	private CheckBox hoursCbx;
	private CheckBox datesCbx;

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
		createSectionHBox();
		createDateHBox();
		getChildren().add(stat);
		getChildren().add(infoBtn);
		getChildren().add(closeBtn);
		getChildren().add(dateHbx);
		getChildren().add(sectionHbx);
	}

	private void createDateHBox() {
		dateHbx = new HBox();
		dateHbx.setSpacing(10);
		statByDayBtn = new Button("Get info by date");
		startDate = new DatePicker(LocalDate.now());
		startDate.setMaxWidth(105);
		fromLbl = new Label("From:");
		endDate = new DatePicker(LocalDate.now());
		endDate.setMaxWidth(105);
		untilLbl = new Label("Until:");
		statByDayBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int timeS = startHourCmb.getSelectionModel().getSelectedIndex();
				int timeE = endHourCmb.getSelectionModel().getSelectedIndex();
				LocalDateTime start = startDate.getValue().atTime(timeS, 0, 0);
				LocalDateTime end = endDate.getValue().atTime(timeE, 0, 0);
				String archive ="";
				if (end.isBefore(start)) {
					archive = "Illegal date range!";
				} else {
					if (end.toLocalTime().equals(LocalTime.MIN))
						end = LocalDateTime.of(end.toLocalDate().minusDays(1),
								LocalTime.MAX);
					if (datesCbx.isSelected() && hoursCbx.isSelected())
						archive = creatReportByDateAndHour(start, end);
					else if (datesCbx.isSelected())
						archive = createReportByDates(start, end);
					else if (hoursCbx.isSelected())
						archive = createReportByHour(start, end);
					archive += createReportOfTotal(start, end);
				}
				setStatistics(archive);
			}
		});

		dateHbx.getChildren().addAll(statByDayBtn, fromLbl, startDate,
				untilLbl, endDate);
	}

	private void createSectionHBox() {
		sectionHbx = new HBox();
		datesCbx = new CheckBox("By dates");
		hoursCbx = new CheckBox("By houers");
		sectionHbx.setSpacing(10);
		final String[] hours = new String[24];
		for (int i = 0; i < 24; i++)
			hours[i] = String.format("%02d:00", i);
		startHourCmb = new ComboBox();
		startHourCmb.getItems().addAll(hours);
		startHourCmb.setValue(hours[0]);
		startHourCmb.setTooltip(new Tooltip("Start hour"));
		endHourCmb = new ComboBox();
		endHourCmb.getItems().addAll(hours);
		endHourCmb.setValue(hours[0]);
		endHourCmb.setTooltip(new Tooltip("End hour"));
		sectionHbx.getChildren().addAll(datesCbx, hoursCbx, startHourCmb,
				endHourCmb);
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

	private String creatReportByDateAndHour(LocalDateTime start,
			LocalDateTime end) {
		Vector<Transaction> history;
		StringBuilder data = new StringBuilder();
		LocalDateTime corrent = start;
		while (corrent.isBefore(end)) {
			data.append(corrent.toLocalDate() + ":\n");
			data.append(createReportByHour(corrent,
					LocalDateTime.of(corrent.toLocalDate(), end.toLocalTime())));
			corrent = corrent.plusDays(1);
		}
		data.append("\n");
		return data.toString();
	}

	private String createReportByHour(LocalDateTime start, LocalDateTime end) {
		Vector<Transaction> history;
		StringBuilder data = new StringBuilder();
		data.append("Profit by hour:\n");
		LocalDateTime correntHour = start;
		LocalDateTime endHour = LocalDateTime.of(start.toLocalDate(),
				end.toLocalTime());
		while (correntHour.isBefore(endHour)) {
			LocalTime nextH = correntHour.toLocalTime().plusHours(1);
			nextH = nextH.equals(LocalTime.MIN) ? LocalTime.MAX : nextH;
			history = listener.getHistory(correntHour,
					LocalDateTime.of(end.toLocalDate(), nextH), 1);
			if(history.size() == 0) {
				correntHour = correntHour.plusHours(1);
				continue;
			}
			data.append(correntHour.toLocalTime() + ":\n");
			for (Transaction t : history) {
				if (t.type == ServiceType.FUEL)
					data.append("	PUMP No." + t.pump + ": "
							+ String.format("%.2f", t.amount) + '\n');
				else
					data.append("	Clean: " + String.format("%.2f", t.amount)
							+ '\n');
			}
			correntHour = correntHour.plusHours(1);
		}
		data.append("\n");
		return data.toString();
	}

	private String createReportByDates(LocalDateTime start, LocalDateTime end) {
		Vector<Transaction> history;
		StringBuilder data = new StringBuilder();
		data.append("Profit by Day:\n");
		LocalDateTime corrent = start;
		while (corrent.isBefore(end)) {
			data.append(corrent.toLocalDate() + ":\n");
			history = listener.getHistory(corrent, corrent, 2);
			for (Transaction t : history) {
				if (t.type == ServiceType.FUEL)
					data.append("	PUMP No." + t.pump + ": "
							+ String.format("%.2f", t.amount) + '\n');
				else
					data.append("	Clean: " + String.format("%.2f", t.amount)
							+ '\n');
			}
			corrent = corrent.plusDays(1);
		}
		data.append("\n");
		return data.toString();
	}

	private String createReportOfTotal(LocalDateTime start, LocalDateTime end) {
		Vector<Transaction> history;
		StringBuilder data = new StringBuilder();
		history = listener.getHistory(start, end, 2);
		data.append("Total profit by pump/clean:\n");
		for (Transaction t : history) {
			if (t.type == ServiceType.FUEL)
				data.append("	PUMP No." + t.pump + ": "
						+ String.format("%.2f", t.amount) + '\n');
			else
				data.append("	Clean: " + String.format("%.2f", t.amount) + '\n');
		}
		history = listener.getHistory(start, end, 3);
		data.append("\nTotal profit:\n");
		for (Transaction t : history) {
			data.append(String.format("	%.2f", t.amount) + '\n');
		}
		return data.toString();
	}

}
