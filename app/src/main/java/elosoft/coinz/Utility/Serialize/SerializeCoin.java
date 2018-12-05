package elosoft.coinz.Utility.Serialize;

import java.util.ArrayList;
import java.util.HashMap;

import elosoft.coinz.Models.Coin;

public class SerializeCoin {

    public static HashMap<String, Object> serializeCoinForFirestore(Coin c) {
        HashMap<String, Object> coinData = new HashMap();
        coinData.put("id", c.id);
        coinData.put("value", c.value);
        coinData.put("type", c.type.name());
        coinData.put("longitude", c.position.getLongitude());
        coinData.put("latitude", c.position.getLatitude());
        return coinData;
    }

    public static HashMap<String, Object> seralizeCoinzForFirestore(HashMap<String, Coin> coinz) {
        HashMap<String, Object> coinzData = new HashMap();
        for (Coin c : coinz.values()) {
            coinzData.put(c.id, serializeCoinForFirestore(c));
        }
        return coinzData;
    }

}
