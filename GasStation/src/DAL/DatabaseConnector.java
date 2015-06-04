package DAL;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Vector;

public class DatabaseConnector {

    private static DatabaseConnector instance;
    private MysqlDataSource dataSource;

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
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupDataSource() {
        dataSource = new MysqlDataSource();
        dataSource.setURL("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("my_db");
        dataSource.setUser("root");
        dataSource.setPassword("");
    }

    public boolean storeTransaction(Transaction transaction) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String query;
            if (transaction.type == ServiceType.FUEL) {
                query = String.format("INSERT INTO transactions (STATION_ID, AMOUNT, TIME_STAMP, TYPE, PUMP) VALUES (%s, %s, %s,%s, %s)",
                        transaction.gasStation, transaction.amount, transaction.timeStamp, transaction.type.ordinal(), transaction.pump);
            } else { // cleaning service
                query = String.format("INSERT INTO transactions (AMOUNT, TIME_STAMP, TYPE) VALUES (%s, %s, %s,%s)",
                        transaction.gasStation, transaction.amount, transaction.timeStamp, transaction.type.ordinal());
            }

            int rowCount = statement.executeUpdate(query);
            connection.close();
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Vector<?> getTransactions(LocalDate first, LocalDate last, boolean pump) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            Vector<Transaction> t = new Vector<>();
            
            ResultSet set = statement.executeQuery("SELECT * FROM transactions WHERE TIME_STAMP > " + first + " AND TIME_STAMP < " + last);
            while (set.next()) {
            	set.toString();
            }
            //int rowCount = statement.executeUpdate(query);
            connection.close();
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
    }
}
