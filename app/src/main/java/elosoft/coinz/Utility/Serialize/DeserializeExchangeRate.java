package elosoft.coinz.Utility.Serialize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import elosoft.coinz.Models.ExchangeRate;

public class DeserializeExchangeRate {
    public static ExchangeRate deserializeExchangeRateFromGeoJSON(JSONObject geoJSON)
            throws DeserializeCoin.CoinzGeoJSONParseError {
        try {
            JSONObject rates = geoJSON.getJSONObject("rates");
            return new ExchangeRate(
                    rates.getDouble("SHIL"),
                    rates.getDouble("PENY"),
                    rates.getDouble("DOLR"),
                    rates.getDouble("QUID")
            );
        } catch (JSONException e) {
            throw new DeserializeCoin.CoinzGeoJSONParseError(e.getMessage());
        }
    }
    public static ExchangeRate deserializeExchangeRateFromFirestore(
            Map<String, Object> exchangeRateSerial){
        return new ExchangeRate(
                (double)exchangeRateSerial.get("SHIL"),
                (double)exchangeRateSerial.get("PENY"),
                (double)exchangeRateSerial.get("DOLR"),
                (double)exchangeRateSerial.get("QUID")
        );
    }
}
