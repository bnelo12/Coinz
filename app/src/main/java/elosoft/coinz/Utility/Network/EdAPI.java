package elosoft.coinz.Utility.Network;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Date;

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

    public void getCoinzGeoJSON(
            Context appContext, Listener<JSONObject> onSuccessListener,
            Response.ErrorListener onErrorListener) {
        String url = "http://homepages.inf.ed.ac.uk/stg/coinz/2018/01/01/coinzmap.geojson";
        JsonObjectRequest getCoinzDataRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, onSuccessListener, onErrorListener);
        Volley.newRequestQueue(appContext).add(getCoinzDataRequest);
    }
}
