package elosoft.coinz.Models;

import java.util.Date;

public class UserCoinzData {

    private Date dateLastUpdated;
    private double numGOLD;
    private double coinzCollectedToday;
    private double coinzDepositedToday;

    public UserCoinzData(double numGOLD, Date dateLastUpdated, double coinzCollectedToday,
                         double coinzDepositedToday) {
        this.numGOLD = numGOLD;
        this.dateLastUpdated = dateLastUpdated;
        this.coinzCollectedToday = coinzCollectedToday;
        this.coinzDepositedToday = coinzDepositedToday;
    }

    public double getNumGOLD() {
        return numGOLD;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public double getCoinzCollectedToday() {
        return coinzCollectedToday;
    }

    public void setCoinzCollectedToday(double coinzCollectedToday) {
        this.coinzCollectedToday = coinzCollectedToday;
    }

    public double getCoinzDepositedToday() {
        return coinzDepositedToday;
    }

    public void setCoinzDepositedToday(double coinzDepositedToday) {
        this.coinzDepositedToday = coinzDepositedToday;
    }
}
