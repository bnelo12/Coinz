package elosoft.coinz.Models;

import java.util.HashMap;

public class UserCoinzData {

    private double numPENY;
    private double numSHIL;
    private double numDOLR;
    private double numQUID;
    private ExchangeRate exchangeRate;

    public UserCoinzData(HashMap<String, Coin> collectedCoinz, ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
        numPENY = 0;
        numSHIL = 0;
        numDOLR = 0;
        numQUID = 0;
        for (Coin c : collectedCoinz.values()) {
            switch (c.type) {
                case DOLR: numDOLR += c.value; break;
                case QUID: numQUID += c.value; break;
                case SHIL: numSHIL += c.value; break;
                case PENY: numPENY += c.value; break;
                default: break;
            }
        }
    }

    public UserCoinzData(double numPENY, double numSHIL, double numDOLR,
                         double numQUID, ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
        this.numPENY = numPENY;
        this.numSHIL = numSHIL;
        this.numDOLR = numDOLR;
        this.numQUID = numQUID;
    }

    public double getNumPENY() {
        return numPENY;
    }

    public double getNumSHIL() {
        return numSHIL;
    }

    public double getNumDOLR() {
        return numDOLR;
    }

    public double getNumQUID() {
        return numQUID;
    }

    public double getNumPENYInGOLD() {
        return numPENY*exchangeRate.ExchangeRatePENY;
    }

    public double getNumSHILInGOLD() {
        return numSHIL*exchangeRate.ExchangeRateSHIL;
    }

    public double getNumDOLRInGOLD() {
        return numDOLR*exchangeRate.ExchangeRateDOLR;
    }

    public double getNumQUIDInGOLD() {
        return numQUID*exchangeRate.ExchangeRateQUID;
    }
}
