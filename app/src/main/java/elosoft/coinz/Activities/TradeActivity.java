package elosoft.coinz.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import elosoft.coinz.Components.CoinzViewAdapter;
import elosoft.coinz.Components.EightBitRetroKeyBoard;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.DrawableCoin;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.Trade;
import elosoft.coinz.R;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;
import elosoft.coinz.Utility.Serialize.DeserializeCoin;
import elosoft.coinz.Utility.User.UserUtility;

import static elosoft.coinz.Utility.UI.TradeUtility.getSelectedCoin;
import static elosoft.coinz.Utility.UI.TradeUtility.slideKeyboardDown;
import static elosoft.coinz.Utility.UI.TradeUtility.slideKeyboardUp;
import static elosoft.coinz.Utility.UI.TradeUtility.tradePossible;

public class TradeActivity extends FragmentActivity {

    private ArrayList<DrawableCoin> userDrawableCoinz = null;
    private ArrayList<DrawableCoin> partnerDrawableCoinz = null;

    private boolean tradeButtonVisible = false;

    private String partnerUserName = null;
    private String user = null;

    private TextEmitter emitter = null;
    private EightBitRetroKeyBoard keyboard = null;

    private void updateTradeConfirmButton() {
        if (tradePossible(userDrawableCoinz, partnerDrawableCoinz)) {
            slideTradeConfirmButtonUp();
        }
        else slideTradeConfirmButtonDown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TradeActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_coinz);
        Log.d("TradeActivity", "Initializing view components.");
        initKeyboard();
        initEmitter();
        initExitButton();
        setUser();
        Log.d("TradeActivity", "Commencing trade.");
        commenceTrade();
    }

    private void initKeyboard() {
        keyboard = findViewById(R.id.keyboard);
        keyboard.addOnDoneKeyPressedListener(v -> {
            if (emitter.userInputMode) {
                String input = emitter.popUserInput();
                slideKeyboardDown(getApplicationContext(), keyboard);
                lookupUser(emitter, input);
            }
        });
    }

    private void initEmitter() {
        emitter = findViewById(R.id.emitter);
        emitter.addEightBitKeyBoard(keyboard);
        emitter.addOnWaitingForUserInput(() -> slideKeyboardUp(getApplicationContext(), keyboard));
        emitter.emitText();
    }

    private void initExitButton() {
        ImageButton exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(v -> finish());
    }

    private void setUser() {
        user = LocalStorageAPI.getLoggedInUserName(getApplicationContext());
    }

    private void handleNoCoinzToTrade() {
        emitter.appendText("You have no coinz to trade. Visit the map to collect coinz!");
    }

    private void commenceTrade() {
        FireStoreAPI.getInstance().getUserCollectedCoinz(user, task -> {
            HashMap<String, Object> taskResult = (HashMap<String, Object>)FireStoreAPI
                    .getTaskResult(task);
            HashMap<String, Coin> userCoinz = DeserializeCoin.deserializeCoinz(taskResult);
            if (userCoinz.size() == 0) {
                handleNoCoinzToTrade();
            } else {
                emitter.appendText("Who would you like to trade with? %n username: %i");
                ConstraintLayout otherCoinz = findViewById(R.id.fragment_user_coinz);
                GridView gridView = otherCoinz.findViewById(R.id.coinz_grid);
                ArrayList<DrawableCoin> drawableCoins = new ArrayList<>();
                for (Coin c : new ArrayList<>(userCoinz.values())) {
                    drawableCoins.add(new DrawableCoin(c, false));
                }
                userDrawableCoinz = drawableCoins;
                CoinzViewAdapter coinzAdapter = new CoinzViewAdapter(TradeActivity.this, drawableCoins);
                gridView.setAdapter(coinzAdapter);
                gridView.setOnItemClickListener((parent, view, position, id) -> {
                    drawableCoins.get(position).toggleSelected();
                    coinzAdapter.notifyDataSetChanged();
                    updateTradeConfirmButton();
                });
            }
        });
    }

    private void lookupUser(TextEmitter emitter, String user) {
        emitter.appendText(String.format("Looking up user %s . . . ", user));
        FireStoreAPI.getInstance().getUserCollectedCoinz(user, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                HashMap<String, Object> taskResult = (HashMap<String, Object>)FireStoreAPI
                        .getTaskResult(task);
                if (taskResult == null) {
                    emitter.appendText("User not found! %n username: %i");
                    return;
                }
                HashMap<String, Coin> tradeUserCoinz = DeserializeCoin.deserializeCoinz(taskResult);
                if (tradeUserCoinz.size() == 0) {
                    emitter.appendText(String.format("%s has no coinz to trade! %%n username: %%i", user));
                    return;
                }
                partnerUserName = user;
                ArrayList<DrawableCoin> drawableCoins = new ArrayList<>();
                for (Coin c : new ArrayList<>(tradeUserCoinz.values())) {
                    drawableCoins.add(new DrawableCoin(c, false));
                }
                TextView partnerLabel = findViewById(R.id.partner_coinz_label);
                partnerLabel.setText(String.format("%s's Coinz", user));
                ConstraintLayout otherCoinz = findViewById(R.id.fragment_other_user_coinz);
                GridView gridView = otherCoinz.findViewById(R.id.coinz_grid);
                CoinzViewAdapter coinzAdapter = new CoinzViewAdapter(TradeActivity.this, drawableCoins);
                gridView.setAdapter(coinzAdapter);
                partnerDrawableCoinz = drawableCoins;
                gridView.setOnItemClickListener((parent, view, position, id) -> {
                    drawableCoins.get(position).toggleSelected();
                    coinzAdapter.notifyDataSetChanged();
                    updateTradeConfirmButton();
                });
                slideFragmentLayoutUp();
                emitter.appendText("Found user. Select the coinz you want to trade.");
            }
        });
    }

    private void slideTradeConfirmButtonUp() {
        Button tradeButton = findViewById(R.id.confirm_trade_button);
        if (tradeButtonVisible) return;
        tradeButton.setVisibility(View.VISIBLE);
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        tradeButton.startAnimation(slide_up);
        tradeButton.setOnClickListener(onConfirmPressListener);
        tradeButtonVisible = true;
    }

    private void slideTradeConfirmButtonDown() {
        Button tradeButton = findViewById(R.id.confirm_trade_button);
        if (!tradeButtonVisible) return;
        tradeButton.setVisibility(View.GONE);
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        tradeButton.startAnimation(slide_down);
        tradeButton.setOnClickListener(null);
        tradeButtonVisible = false;
    }

    private void slideFragmentLayoutUp() {
        LinearLayout layout = findViewById(R.id.coinz_fragment_layout);
        layout.setVisibility(View.VISIBLE);
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        layout.startAnimation(slide_up);
    }

    private View.OnClickListener onConfirmPressListener = v -> {
        HashMap<String, Coin> userSelectedCoinz = getSelectedCoin(userDrawableCoinz);
        HashMap<String, Coin> partnerSelectedCoinz = getSelectedCoin(partnerDrawableCoinz);
        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(getApplicationContext());
        Trade trade = new Trade(userSelectedCoinz, partnerSelectedCoinz, user, partnerUserName,
                "pending", exchangeRate, UUID.randomUUID().toString());
        FireStoreAPI.getInstance().addTrade(trade);
        FireStoreAPI.getInstance().removeUserDepositedCoinz(trade.getUser(),  new ArrayList<>(userSelectedCoinz.values()));
        FireStoreAPI.getInstance().removeUserDepositedCoinz(trade.getPartner(),  new ArrayList<>(partnerSelectedCoinz.values()));;
        finish();
    };
}
