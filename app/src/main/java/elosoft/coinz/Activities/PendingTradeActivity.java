package elosoft.coinz.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import elosoft.coinz.Components.CoinzViewAdapter;
import elosoft.coinz.Components.TradesViewAdapter;
import elosoft.coinz.Models.Trade;
import elosoft.coinz.R;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;

import static elosoft.coinz.Utility.Serialize.DeserializeTrade.deserializeTradesFromFirestore;

public class PendingTradeActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_trade);
        initExitButton();


        String user = LocalStorageAPI.getLoggedInUserName(getApplicationContext());
        FireStoreAPI.getInstance().getPendingUserTrades(user, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Map<String, Object> result = FireStoreAPI.getTaskResult(task);
                ArrayList<Trade> pendingTrades = deserializeTradesFromFirestore(result);
                ConstraintLayout layout = findViewById(R.id.fragment_pending_trades);
                ListView listView = layout.findViewById(R.id.trades_list);
                TradesViewAdapter tradesViewAdapter = new TradesViewAdapter(
                        PendingTradeActivity.this, pendingTrades, "deposit gold",
                        "accepted");
                listView.setAdapter(tradesViewAdapter);
            }
        });
        FireStoreAPI.getInstance().getReceivedUserTrades(user, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Map<String, Object> result = FireStoreAPI.getTaskResult(task);
                ArrayList<Trade> pendingTrades = deserializeTradesFromFirestore(result);
                ConstraintLayout layout = findViewById(R.id.fragment_received_trades);
                ListView listView = layout.findViewById(R.id.trades_list);
                TradesViewAdapter tradesViewAdapter = new TradesViewAdapter(
                        PendingTradeActivity.this, pendingTrades, "approve",
                        "pending");
                listView.setAdapter(tradesViewAdapter);
            }
        });
    }

    private void initExitButton() {
        ImageButton exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(v -> finish());
    }
}
