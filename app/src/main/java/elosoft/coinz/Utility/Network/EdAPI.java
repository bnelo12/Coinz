package elosoft.coinz.Utility.Network;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;

public class EdAPI {
    private static volatile EdAPI instance;
    private static final Object mutex = new Object();

    private EdAPI() {

    }

    public static EdAPI getInstance() {
        EdAPI result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) instance = result = new EdAPI();
            }
        }
        return result;
    }

    private static String getURL(Date date) {
        String url = "http://homepages.inf.ed.ac.uk/stg/coinz/";
        url += new SimpleDateFormat("yyyy/MM/dd/").format(date);
        url += "coinzmap.geojson";
        return url;
    }

    public void getCoinzGeoJSON(
            Context appContext, Listener<JSONObject> onSuccessListener,
            Response.ErrorListener onErrorListener) {
        Date date = new Date();
        LocalStorageAPI.updateLastUpdateDate(appContext, date);
        String url = getURL(date);
        JsonObjectRequest getCoinzDataRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, onSuccessListener, onErrorListener);
        Volley.newRequestQueue(appContext).add(getCoinzDataRequest);
    }
}
