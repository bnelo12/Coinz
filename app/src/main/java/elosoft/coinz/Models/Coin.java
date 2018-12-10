package elosoft.coinz.Models;

import com.mapbox.mapboxsdk.geometry.LatLng;

import static elosoft.coinz.Utility.Serialize.DeserializeCoin.stringToType;

public class Coin {

    public enum Type {
        PENY, DOLR, SHIL, QUID, GOLD
    }

    public String id;
    public double value;
    public Type type;
    public LatLng position;

    public Coin(String id, String type, double latitude, double longitude, double value) {
        this.id         = id;
        this.type       = stringToType(type);
        this.position   = new LatLng(latitude, longitude);
        this.value      = value;
    }
}
