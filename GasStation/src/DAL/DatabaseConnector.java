package DAL;

import BL.GasStation;
import BL.Pump;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class DatabaseConnector {

	private static DatabaseConnector instance;
	private MysqlDataSource dataSource;

	private DateTimeFormatter dateFormat = DateTimeFormatter
			.ofPattern("yyyyMMdd");
	private DateTimeFormatter timeFormat = DateTimeFormatter
			.ofPattern("HHmmss");

	// database connection should be a singleton
	private DatabaseConnector() {
		defineDriver();
		setupDataSource();
	}

	public static DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}

	public void defineDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void setupDataSource() {
		dataSource = new MysqlDataSource();
		dataSource.setURL("jdbc:mysql://localhost/my_db");
		dataSource.setPort(3306);
		dataSource.setDatabaseName("my_db");
		dataSource.setUser("root");
		dataSource.setPassword("");
	}

	public boolean checkGasStation(GasStation gs) {
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet res = statement
					.executeQuery("SELECT ID FROM gas_stations WHERE ID = "
							+ gs.getId());
			if (res.first() == false) {
				int rowCount = statement
						.executeUpdate("INSERT INTO gas_stations (ID, GAS_REVENUE, CLEAN_REVENUE, CARS_WASHED, CARS_CLEANED) VALUES ("
								+ gs.getId() + ", 0, 0, 0, 0)");
				res.close();
				statement.close();
				connection.close();
				setPumps(gs.getPumps(), gs.getId());
				return rowCount > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setPumps(Pump[] pumps, int gs) {
		Connection connection;
		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			int rows = 0;
			for (int i = 0; i < pumps.length; i++)
				rows += statement.executeUpdate(String.format(
						"INSERT INTO pumps (ID, STATION_ID) VALUES (%d, %d)",
						pumps[i].getNum(), gs));
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean storeTransaction(Transaction transaction) {
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			String query;
			if (transaction.type == ServiceType.FUEL) {
				query = String
						.format("INSERT INTO transactions (STATION_ID, AMOUNT, DATE_ADDED, TIME_ADDED, SERVICE_TYPE, PUMP) VALUES (%s, %s, %s, %s,%s, %s)",
								transaction.gasStation, transaction.amount,
								dateFormat.format(transaction.timeStamp),
								timeFormat.format(transaction.timeStamp),
								transaction.type.ordinal(), transaction.pump);
			} else { // cleaning service
				query = String
						.format("INSERT INTO transactions (STATION_ID, AMOUNT, DATE_ADDED, TIME_ADDED, SERVICE_TYPE, PUMP) VALUES (%s, %s, %s, %s,%s, %s)",
								transaction.gasStation, transaction.amount,
								dateFormat.format(transaction.timeStamp),
								timeFormat.format(transaction.timeStamp),
								transaction.type.ordinal(), "Null");
			}
			int rowCount = statement.executeUpdate(query);
			statement.close();
			connection.close();
			return rowCount > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Vector<Transaction> getTransactions(LocalDateTime first,
			LocalDateTime last, int option) {
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();

			Vector<Transaction> trans = new Vector<Transaction>();
			ResultSet res;
			res = statement.executeQuery(setQuery(first, last, option));
			while (res.next()) {
				Transaction tr = new Transaction();
				tr.amount = res.getDouble("SUM");
				tr.pump = res.getInt("PUMP");
				tr.timeStamp = LocalDateTime.of(res.getDate("DATE")
						.toLocalDate(), res.getTime("TIME").toLocalTime());
				tr.type = (res.getInt("SERVICE_TYPE") == 0 ? ServiceType.FUEL
						: ServiceType.CLEANING);
				trans.add(tr);
			}
			res.close();
			statement.close();
			connection.close();
			return trans;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String setQuery(LocalDateTime first, LocalDateTime last, int option) {
		String select = "SELECT DATE_ADDED, TIME_ADDED, PUMP, SERVICE_TYPE, SUM(AMOUNT) AS 'SUM' FROM transactions ";
		switch (option) {
		case 1:
			select += "WHERE TIME_ADDED BETWEEN " + timeFormat.format(first)
					+ " AND " + timeFormat.format(last) + " AND DATE_ADDED BETWEEN "
					+ dateFormat.format(first) + " AND "
					+ dateFormat.format(last) + " GROUP BY PUMP";
			break;
		case 2:
			select += "WHERE DATE_ADDED BETWEEN " + dateFormat.format(first)
					+ " AND " + dateFormat.format(last) + " GROUP BY PUMP";
			break;
		case 3:
			select += "WHERE DATE_ADDED BETWEEN " + dateFormat.format(first)
					+ " AND " + dateFormat.format(last) + " GROUP BY DATE_ADDED";
			break;
		default:
			select = "";
		}
		return select;
	}
}
