package DAL;

import java.time.LocalDate;

public class Transaction {

    private int         gasStation;
    private int         pump;
    private double      amount;
    private LocalDate   timeStamp;
    private ServiceType type;

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDate timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPump() {
        return pump;
    }

    public void setPump(int pump) {
        this.pump = pump;
    }

    public int getGasStation() {
        return gasStation;
    }

    public void setGasStation(int gasStation) {
        this.gasStation = gasStation;
    }
}
