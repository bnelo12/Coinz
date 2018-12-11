package elosoft.coinz.Views;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

import elosoft.coinz.Activities.DepositCoinzActivity;
import elosoft.coinz.Activities.PendingTradeActivity;
import elosoft.coinz.Components.HighScoreListAdapter;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.R;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;

public class BankView extends Fragment {

    TextView goldAmount = null;
    UserCoinzData userCoinzData = null;
    View currentView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bank, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        currentView = view;
        TextEmitter welcomeMessage = view.findViewById(R.id.bank_welcome_message);
        welcomeMessage.emitText();

        goldAmount = view.findViewById(R.id.bank_gold_coin_amount);
        Context appContext = getContext().getApplicationContext();
        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(appContext);
        userCoinzData = LocalStorageAPI.readUserCoinzData(
                getContext().getApplicationContext(), exchangeRate);

        Button depositButton = view.findViewById(R.id.bank_deposit_button);
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionIntent = new Intent(getActivity(), DepositCoinzActivity.class);
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity());
                startActivity(transitionIntent, activityOptions.toBundle());
            }
        });

        FireStoreAPI.getInstance().getHighScores(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                HashMap<String, Object> scores = (HashMap<String, Object>)FireStoreAPI.getTaskResult(task);
                ListView listView = getView().findViewById(R.id.high_score_list);
                HighScoreListAdapter highScoreListAdapter = new HighScoreListAdapter(
                        getContext(), scores);
                listView.setAdapter(highScoreListAdapter);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Context appContext = getContext().getApplicationContext();
        TextView goldAmount = getView().findViewById(R.id.bank_gold_coin_amount);
        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(appContext);
        userCoinzData = LocalStorageAPI.readUserCoinzData(appContext, exchangeRate);
        Log.d("BankView", "[onResume] Updating Gold Amount");
        if (goldAmount != null && userCoinzData != null) {
            goldAmount.setText(String.format("%.5f GOLD", userCoinzData.getNumGOLD()));
            goldAmount.invalidate();
        }
    }

}
