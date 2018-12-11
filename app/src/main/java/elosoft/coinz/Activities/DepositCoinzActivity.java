package elosoft.coinz.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

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
import elosoft.coinz.Utility.User.UserUtility;

import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinzFromFireStore;

public class DepositCoinzActivity extends FragmentActivity {

    private HashMap<String, Coin> selectedCoins = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_coinz);
        TextEmitter te = findViewById(R.id.d_coinz_emitter_coinz_emitter);
        te.emitText();
        String currentUser = LocalStorageAPI.getLoggedInUserName(getApplicationContext());
        FireStoreAPI.getInstance().getUserCollectedCoinz(currentUser, task -> {
            HashMap<String, Object> coinzData = (HashMap<String, Object>) FireStoreAPI
                    .getTaskResult(task);
            ArrayList<Coin> coinz = new ArrayList<>(deserializeCoinzFromFireStore(coinzData).values());
            ArrayList<DrawableCoin> drawableCoins = new ArrayList<>();
            for (Coin c : coinz) {
                drawableCoins.add(new DrawableCoin(c, false));
            }
            GridView gridView = findViewById(R.id.coinz_grid);
            CoinzViewAdapter coinzAdapter = new CoinzViewAdapter(
                    DepositCoinzActivity.this, drawableCoins);
            gridView.setAdapter(coinzAdapter);
            if (coinz.size() == 0) {
                te.appendText("My lord, you haven't collected any coinz yet. Visit the map to find the coin locations.");
            } else {
                te.appendText("Sire, we can exchange our coinz for GOLD here! ");
                emitExchangeRates(te);
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    drawableCoins.get(position).toggleSelected();
                    coinzAdapter.notifyDataSetChanged();
                }
            });

            Button depositButton = findViewById(R.id.bank_deposit_button);
            depositButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    depositSelectedCoinz(drawableCoins);
                    finish();
                }
            });
        });
    }

    private void depositSelectedCoinz(ArrayList<DrawableCoin> coinz) {
        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(getApplicationContext());
        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(getApplicationContext());
        ArrayList<Coin> collectedCoinz = new ArrayList<>();
        double goldToAdd = 0.0f;
        for (DrawableCoin coin : coinz) {
            if (coin.isSelected) {
                Coin c = coin.getCoin();
                switch (c.type) {
                    case PENY:
                        goldToAdd += exchangeRate.ExchangeRatePENY*c.value;
                        break;
                    case DOLR:
                        goldToAdd += exchangeRate.ExchangeRateDOLR*c.value;
                        break;
                    case QUID:
                        goldToAdd += exchangeRate.ExchangeRateQUID*c.value;
                        break;
                    case SHIL:
                        goldToAdd += exchangeRate.ExchangeRateSHIL*c.value;
                        break;
                    default:
                        break;
                }
                collectedCoinz.add(c);
            }
        }
        Log.d("DepositCoinzActivity",
                String.format("Adding %f GOLD to user account.", goldToAdd));
        LocalStorageAPI.updateUserGOLD(getApplicationContext(), goldToAdd);
        UserUtility.removeUserCoinz(getApplicationContext(), collectedCoinz);
    }

    private void emitExchangeRates(TextEmitter emitter) {
        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(getApplicationContext());
        emitter.appendText(" %n Today's exchange rates are . . . ");
        emitter.appendText(String.format(" %%n 1 PENY -> %.2f GOLD", exchangeRate.ExchangeRatePENY));
        emitter.appendText(String.format(" %%n 1 DOLR -> %.2f GOLD", exchangeRate.ExchangeRateDOLR));
        emitter.appendText(String.format(" %%n 1 SHIL -> %.2f GOLD", exchangeRate.ExchangeRateSHIL));
        emitter.appendText(String.format(" %%n 1 QUID -> %.2f GOLD", exchangeRate.ExchangeRateQUID));
    }
}
