package DAL;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
            if (transaction.getType() == ServiceType.FUEL) {
                query = String.format("INSERT INTO transactions (STATION_ID, AMOUNT, TIME_STAMP, TYPE, PUMP) VALUES (%s, %s, %s,%s, %s)",
                        transaction.getGasStation(), transaction.getAmount(), transaction.getTimeStamp(), transaction.getType().ordinal(), transaction.getPump());
            } else { // cleaning service
                query = String.format("INSERT INTO transactions (AMOUNT, TIME_STAMP, TYPE) VALUES (%s, %s, %s,%s)",
                        transaction.getGasStation(), transaction.getAmount(), transaction.getTimeStamp(), transaction.getType().ordinal());
            }

            int rowCount = statement.executeUpdate(query);
            connection.close();
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}