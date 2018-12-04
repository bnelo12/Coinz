package elosoft.coinz.Models;

import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashMap;

public class CoinzData {
    private static volatile CoinzData instance;
    private static Object mutex = new Object();
    private boolean dataLoaded = false;
    private SharedPreferences sharedPref;

    public HashMap<String, Coin> coinz = new HashMap();

    private CoinzData() {
    }

    public static CoinzData getCoinzData() {
        CoinzData result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) instance = result = new CoinzData();
            }
        }
        return result;
    }
}
