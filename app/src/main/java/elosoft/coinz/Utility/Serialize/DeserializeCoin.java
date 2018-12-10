package elosoft.coinz.Utility.Serialize;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import elosoft.coinz.Models.Coin;

public class DeserializeCoin {

    public static Coin.Type stringToType(String type) {
        switch (type) {
            case "PENY":
                return Coin.Type.PENY;
            case "DOLR":
                return Coin.Type.DOLR;
            case "SHIL":
                return Coin.Type.SHIL;
            case "QUID":
                return Coin.Type.QUID;
            default:
                return Coin.Type.GOLD;
        }
    }

    public static class CoinzGeoJSONParseError extends Exception {
        String errorMessage;
        CoinzGeoJSONParseError(String error) {
            errorMessage = error;
        }
        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static HashMap<String, Coin> deserializeCoinzFromGeoJSON(JSONObject geojson)
            throws CoinzGeoJSONParseError {
        HashMap<String, Coin> coinz = new HashMap<>();
        try {
            JSONArray features = geojson.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject coinGeoJSON = features.getJSONObject(i);
                Coin coin = deserializeCoinFromGeoJSON(coinGeoJSON);
                coinz.put(coin.id, coin);
            }
        } catch (JSONException e) {
            throw new CoinzGeoJSONParseError(e.getMessage());
        }
        return coinz;
    }

    private static Coin deserializeCoinFromGeoJSON(JSONObject geoJSONFeature)
            throws CoinzGeoJSONParseError {
        try {
            JSONObject geometry = geoJSONFeature.getJSONObject("geometry");
            JSONObject properties = geoJSONFeature.getJSONObject("properties");
            return new Coin(
                    parseId(properties),
                    parseType(properties),
                    parsePosition(geometry).getLatitude(),
                    parsePosition(geometry).getLongitude(),
                    parseValue(properties)
            );
        } catch (JSONException e) {
            throw new CoinzGeoJSONParseError(e.getMessage());
        }
    }

    private static Coin deserializeCoinFromFireStore(HashMap<String, Object> coin) {
        return new Coin((String)coin.get("id"), (String)coin.get("type"),
                (double) coin.get("latitude"), (double)coin.get("longitude"),
                (double) coin.get("value"));
    }

    public static Coin deserializeCoin(HashMap<String, Object> coin) {
        return new Coin((String)coin.get("id"), (String)coin.get("type"),
                (double) coin.get("latitude"), (double)coin.get("longitude"),
                (double) coin.get("value"));
    }

    public static HashMap<String, Coin> deserializeCoinzFromFireStore(
            HashMap<String, Object> coinz) {
        HashMap<String, Coin> coinzOut = new HashMap<>();
        for (Object coinData : coinz.values()) {
            Coin coinToAdd = deserializeCoinFromFireStore((HashMap<String, Object>) coinData);
            coinzOut.put(coinToAdd.id, coinToAdd);
        }
        return coinzOut;
    }

    public static HashMap<String, Coin> deserializeCoinz(
            HashMap<String, Object> coinz) {
        return deserializeCoinzFromFireStore(coinz);
    }

    private static double parseValue(JSONObject properties) throws JSONException {
        return properties.getDouble("value");
    }

    private static LatLng parsePosition(JSONObject geometry) throws JSONException {
        return new LatLng(
                geometry.getJSONArray("coordinates").getDouble(1),
                geometry.getJSONArray("coordinates").getDouble(0)
        );

    }

    private static String parseId(JSONObject properties) throws JSONException {
        return properties.getString("id");
    }

    private static String parseType(JSONObject properties) throws JSONException {
        return properties.getString("currency");
    }
}
