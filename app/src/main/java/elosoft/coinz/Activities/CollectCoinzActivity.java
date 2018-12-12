package elosoft.coinz.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;

import elosoft.coinz.Components.CoinzViewAdapter;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.DrawableCoin;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.R;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;

import static elosoft.coinz.Utility.LocalStorage.LocalStorageAPI.readUserCoinzData;
import static elosoft.coinz.Utility.LocalStorage.LocalStorageAPI.updateNumCoinzCollected;
import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinz;

public class CollectCoinzActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_coinz);
        TextEmitter te = findViewById(R.id.collect_coinz_emitter);
        initExitButton();
        te.emitText();
        int numberOfCoinzCollected = getIntent().getIntExtra("NUMBER_OF_COINZ", 0);
        HashMap<String, Coin> coinz = deserializeCoinz((HashMap<String, Object>)getIntent().getSerializableExtra("COINZ"));
        String textToAdd = "My lord, we continue to increase our riches . . . %n You have just collected " + numberOfCoinzCollected + " coinz! %n %n Tap to return . . .";
        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(getApplicationContext());
        updateNumCoinzCollected(getApplicationContext(), userCoinzData.getCoinzCollectedToday() + numberOfCoinzCollected);
        te.appendText(textToAdd);
        te.onComplete = () -> te.setOnClickListener(v -> finish());
        ArrayList<DrawableCoin> drawableCoins = new ArrayList<>();
        for (Coin c : new ArrayList<Coin>(coinz.values())) {
            drawableCoins.add(new DrawableCoin(c, false));
        }
        GridView gridView = (GridView)findViewById(R.id.coinz_grid);
        CoinzViewAdapter coinzAdapter = new CoinzViewAdapter(this, drawableCoins);
        gridView.setAdapter(coinzAdapter);
    }

    private void initExitButton() {
        ImageButton exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(v -> finish());
    }
}
