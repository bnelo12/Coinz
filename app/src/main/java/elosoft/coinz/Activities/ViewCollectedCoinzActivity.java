package elosoft.coinz.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import elosoft.coinz.Components.CoinzViewAdapter;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.DrawableCoin;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.R;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;

import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinzFromFireStore;

public class ViewCollectedCoinzActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collected_coinz);
        TextEmitter te = findViewById(R.id.collect_coinz_emitter);
        initExitButton();
        te.emitText();
        String currentUser = LocalStorageAPI.getLoggedInUserName(getApplicationContext());
        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(getApplicationContext());
        FireStoreAPI.getInstance().getUserCollectedCoinz(currentUser, task -> {
            HashMap<String, Object> coinzData = (HashMap<String, Object>) FireStoreAPI
                    .getTaskResult(task);
            ArrayList<Coin> coinz = new ArrayList<>(deserializeCoinzFromFireStore(coinzData).values());
            ArrayList<DrawableCoin> drawableCoins = new ArrayList<>();
            for (Coin c : coinz) {
                drawableCoins.add(new DrawableCoin(c, false));
            }
            GridView gridView = findViewById(R.id.coinz_grid);
            CoinzViewAdapter coinzAdapter = new CoinzViewAdapter(ViewCollectedCoinzActivity.this, drawableCoins);
            gridView.setAdapter(coinzAdapter);
            if (coinz.size() == 0) {
                te.appendText("My lord, you haven't collected any coinz yet. Visit the map to find more coinz.");
            } else if ((int)userCoinzData.getCoinzCollectedToday() == 50) {
                te.appendText("Congratulations on collecting all of today's coinz!");
            }
            else {
                te.appendText("Well done my lord, we are starting to collect a bounty of coinz!");
                te.appendText(String.format(" %%n There are still %d coinz to collect today.", 50-(int)userCoinzData.getCoinzCollectedToday()));
            }
        });
    }

    private void initExitButton() {
        ImageButton exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(v -> finish());
    }


}
