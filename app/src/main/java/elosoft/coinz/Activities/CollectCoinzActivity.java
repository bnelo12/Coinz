package elosoft.coinz.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

import elosoft.coinz.Components.CoinzViewAdapter;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.R;

import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinz;

public class CollectCoinzActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_coinz);
        TextEmitter te = findViewById(R.id.collect_coinz_emitter);
        te.emitText();
        int numberOfCoinzCollected = getIntent().getIntExtra("NUMBER_OF_COINZ", 0);
        HashMap<String, Coin> coinz = deserializeCoinz((HashMap<String, Object>)getIntent().getSerializableExtra("COINZ"));
        String textToAdd = "My lord, we continue to increase our riches . . . %n You have just collected " + numberOfCoinzCollected + " coinz! %n %n Tap to return . . .";
        te.appendText(textToAdd);
        te.onComplete = new Runnable() {
            @Override
            public void run() {
                te.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        };
        GridView gridView = (GridView)findViewById(R.id.coinz_grid);
        CoinzViewAdapter coinzAdapter = new CoinzViewAdapter(this, new ArrayList<Coin>(coinz.values()));
        gridView.setAdapter(coinzAdapter);
    }
}
