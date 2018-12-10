package elosoft.coinz.Utility.User;

import java.util.HashMap;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.DrawableCoin;
import elosoft.coinz.Models.ExchangeRate;

public class CoinzUtility {
    public static double calculateGoldFromCoinz(HashMap<String, Coin> coinz, ExchangeRate exchangeRate) {
        double goldToAdd = 0.0f;
        for (Coin c : coinz.values()) {
            switch (c.type) {
                case PENY: goldToAdd += exchangeRate.ExchangeRatePENY*c.value; break;
                case DOLR: goldToAdd += exchangeRate.ExchangeRateDOLR*c.value; break;
                case QUID: goldToAdd += exchangeRate.ExchangeRateQUID*c.value; break;
                case SHIL: goldToAdd += exchangeRate.ExchangeRateSHIL*c.value; break;
                default: break;
            }
        }
        return goldToAdd;
    }
}
