package elosoft.coinz.Utility.Serialize;

import java.util.HashMap;

import elosoft.coinz.Models.ExchangeRate;

public class SerializeExchangeRate {
    public static HashMap<String, Object> serializeExchangeRateForFirestore(
             ExchangeRate exchangeRate){
        HashMap<String, Object> exchangeRateSerial = new HashMap<>();
        exchangeRateSerial.put("SHIL", exchangeRate.ExchangeRateSHIL);
        exchangeRateSerial.put("PENY", exchangeRate.ExchangeRatePENY);
        exchangeRateSerial.put("DOLR", exchangeRate.ExchangeRateDOLR);
        exchangeRateSerial.put("QUID", exchangeRate.ExchangeRateQUID);
        return exchangeRateSerial;
    }
}