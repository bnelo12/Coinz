package elosoft.coinz.Models;

public class ExchangeRate {

    public double ExchangeRateSHIL;
    public double ExchangeRatePENY;
    public double ExchangeRateDOLR;
    public double ExchangeRateQUID;

    public ExchangeRate(double ExchangeRateSHIL, double ExchangeRatePENY,
                        double ExchangeRateDOLR, double ExchangeRateQUID) {
        this.ExchangeRateSHIL = ExchangeRateSHIL;
        this.ExchangeRateDOLR = ExchangeRateDOLR;
        this.ExchangeRatePENY = ExchangeRatePENY;
        this.ExchangeRateQUID = ExchangeRateQUID;
    }
}
