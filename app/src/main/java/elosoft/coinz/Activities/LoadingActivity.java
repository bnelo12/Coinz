package elosoft.coinz.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.CoinzData;
import elosoft.coinz.R;

public class LoadingActivity extends Activity {
    private TextEmitter loadingTextEmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        loadingTextEmitter = findViewById(R.id.loading_emitter);
        loadingTextEmitter.emitText();
        loadCoinzFromFireStore();
    }

    private  void loadCoinzFromFireStore() {
        loadingTextEmitter.appendText(" %n Getting User Data . . .");
        // Attempt to load the Coinz from FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("coinz")
                .document("bnelo12")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                            } else {
                                loadingTextEmitter.appendText(" %n New user detected. Creating Account . . .");
                                loadCoinzFromServer();
                            }
                        } else {

                        }
                    }
                });
    }

    private void loadCoinzFromServer() {
        loadingTextEmitter.appendText(" %n Connecting to Server . . .");
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://homepages.inf.ed.ac.uk/stg/coinz/2018/01/01/coinzmap.geojson";
        JsonObjectRequest getCoinzDataRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                (geojson) -> {
                    try {
                        CoinzData.getCoinzData().coinz = Coin.parseGeoJSON(geojson);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference coinz = db.collection("coinz");
                        coinz.document("bnelo12").set(Coin.seralizeCoinSet(
                                CoinzData.getCoinzData().coinz));
                        loadingTextEmitter.appendText(" %n Coinz Downloaded");
                        loadingTextEmitter.onComplete = new Runnable() {
                            @Override
                            public void run() {
                                final Intent transitonIntent = new Intent(LoadingActivity.this, CoinzNavigationActivity.class);
                                startActivity(transitonIntent);
                            }
                        };
                    }
                    catch (Coin.CoinzGeoJSONParseError e) {
                        Log.e("Error while parsing GeoJSON", e.getErrorMessage());
                    }
                },
                (error) -> {
                    loadingTextEmitter.appendText(" %n CONNECTION ERROR: Trying again.");
                });
        queue.add(getCoinzDataRequest);
    }

    @Override
    public void onBackPressed() {
        /* Do nothing */
    }
}
