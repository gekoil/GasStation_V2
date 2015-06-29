package Listeners;

import DAL.Transaction;

import java.time.LocalDate;
import java.util.Vector;

public interface UIStatisticsListener {
	void getStatistics();
	void closeGasStation();
	Vector<Transaction> getHistory(LocalDate firstDate, LocalDate lastDate, boolean byPump);
}
