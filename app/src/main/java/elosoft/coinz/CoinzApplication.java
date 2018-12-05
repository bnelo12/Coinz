package elosoft.coinz;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import elosoft.coinz.Utility.Network.EdAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;

public class CoinzApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));

        /* Initialize Singleton Classes */
        FireStoreAPI.getInstance();
        EdAPI.getInstance();
    }
}
