package elosoft.coinz.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.HashMap;

import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.EdAPI;
import elosoft.coinz.Utility.Serialize.DeserializeCoin;
import elosoft.coinz.Utility.Network.FireStoreAPI;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.R;
import elosoft.coinz.Utility.Serialize.DeserializeExchangeRate;
import elosoft.coinz.Utility.User.UserUtility;

import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinzFromGeoJSON;

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

    private void loadCoinzFromFireStore() {
        loadingTextEmitter.appendText(" %n Getting User Data . . .");
        // Attempt to load user data
        FireStoreAPI.getInstance().getUserData("bnelo12", new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        HashMap<String, Object> userDataSerial = (HashMap<String, Object>)FireStoreAPI.getTaskResult(task);
                        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(getApplicationContext());
//                        LocalStorageAPI.storeUserCoinzData(getApplicationContext(), userDataSerial, exchangeRate);
                        final Intent transitonIntent = new Intent(LoadingActivity.this, CoinzNavigationActivity.class);
                        startActivity(transitonIntent);
                    } else {
                        loadingTextEmitter.appendText(" %n New user detected. Creating Account . . .");
                        createUser();
                    }
                } else {
                    loadingTextEmitter.appendText(" %n Coinz couldn't connect to the server.");
                }
            }
        });
    }

    private void createUser() {
        loadingTextEmitter.appendText(" %n Connecting to Server . . .");
        EdAPI.getInstance().getCoinzGeoJSON(new Date(), getApplicationContext(),
                geoJSON -> {
                    try {
                        UserUtility.createNewUser(getApplicationContext(), geoJSON, "bnelo12");
                        loadingTextEmitter.appendText(" %n Coinz Downloaded");
                        loadingTextEmitter.onComplete = () -> {
                            final Intent transitonIntent = new Intent(LoadingActivity.this, CoinzNavigationActivity.class);
                            startActivity(transitonIntent);
                        };
                    }
                    catch (DeserializeCoin.CoinzGeoJSONParseError e) {
                        Log.e("Error while parsing GeoJSON", e.getErrorMessage());
                    }
                },
                error -> loadingTextEmitter.appendText(" %n CONNECTION ERROR: Trying again."));
    }

    @Override
    public void onBackPressed() {
        /* Do nothing */
    }
}
