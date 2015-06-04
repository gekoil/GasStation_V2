package Listeners;

import java.time.LocalDate;

import DAL.Transaction;

public interface UIStatisticsListener {
	void getStatistics();
	void closeGasStation();
	Transaction getHistory(LocalDate firstDate, LocalDate lastDate, boolean byPump);
}
