package elosoft.coinz.Models;

import java.util.Date;

public class UserCoinzData {

    private Date dateLastUpdated;
    private double numGOLD;

    public UserCoinzData(double numGOLD, Date dateLastUpdated) {
        this.numGOLD = numGOLD;
        this.dateLastUpdated = dateLastUpdated;
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
}
