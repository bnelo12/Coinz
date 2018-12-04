package elosoft.coinz.Models;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Coin {

    public static enum Type {
        PENY, DOLR, SHIL, QUID, GOLD
    }

    public static HashMap<String, Coin> parseGeoJSON(JSONObject geojson) throws CoinzGeoJSONParseError {
        HashMap<String, Coin> coinz = new HashMap();
        try {
            JSONArray features = geojson.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject coinGeoJSON = features.getJSONObject(i);
                Coin coin = new Coin(coinGeoJSON);
                coinz.put(coin.id, coin);
            }
        } catch (JSONException e) {
            throw new CoinzGeoJSONParseError(e.getMessage());
        }
        return coinz;
    }

    public static class CoinzGeoJSONParseError extends Exception {

        String errorMessage;

        public  CoinzGeoJSONParseError(String error) {
            errorMessage = error;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static HashMap<String, Object> seralize(Coin c) {
        HashMap<String, Object> coinData = new HashMap();
        coinData.put("id", c.id);
        coinData.put("value", c.value);
        coinData.put("type", c.type.name());
        coinData.put("longitude", c.position.getLongitude());
        coinData.put("lattitude", c.position.getAltitude());
        return coinData;
    }

    public static HashMap<String, Object> seralizeCoinSet(HashMap<String, Coin> coinz) {
        HashMap<String, Object> coinzData = new HashMap();
        ArrayList<HashMap<String, Object>> coinList = new ArrayList();
        for (Coin c : coinz.values()) {
            coinList.add(seralize(c));
        }
        coinzData.put("values", coinList);
        return coinzData;
    }

    public String id;
    public double value;
    public Type type;
    public LatLng position;

    public Coin(JSONObject feature) throws CoinzGeoJSONParseError {
        try {
            JSONObject geometry = feature.getJSONObject("geometry");
            JSONObject properties = feature.getJSONObject("properties");

            this.id         = this.parseId(properties);
            this.type       = this.parseType(properties);
            this.position   = this.parsePosition(geometry);
            this.value      = this.parseValue(properties);

        } catch (JSONException e) {
            throw new CoinzGeoJSONParseError(e.getMessage());
        }
    }

    private double parseValue(JSONObject properties) throws JSONException {
        return properties.getDouble("value");
    }

    private LatLng parsePosition(JSONObject geometry) throws JSONException {
        return new LatLng(
                geometry.getJSONArray("coordinates").getDouble(1),
                geometry.getJSONArray("coordinates").getDouble(0)
        );

    }

    private String parseId(JSONObject properties) throws JSONException {
        return properties.getString("id");
    }

    private Type parseType(JSONObject properties) throws JSONException, CoinzGeoJSONParseError {
        switch (properties.getString("currency")) {
            case "PENY": return Type.PENY;
            case "DOLR": return Type.DOLR;
            case "SHIL": return Type.SHIL;
            case "QUID": return Type.QUID;
            default:     throw new CoinzGeoJSONParseError("Invalid Coin Type Found!");
            }
    }
}
