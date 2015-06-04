package DAL;


import java.io.Serializable;
import java.time.LocalDate;

// a POJO for storing transactions in the database
public class Transaction implements Serializable {

    public int         gasStation;
    public int         pump;
    public double      amount;
    public LocalDate   timeStamp;
    public ServiceType type;
}
