package elosoft.coinz.Views;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import elosoft.coinz.Activities.ViewCollectedCoinzActivity;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.R;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;

public class FortressView extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_fortress, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextEmitter welcomeMessage = view.findViewById(R.id.fortress_welcome_message);
        welcomeMessage.emitText();
        Context appContext = getContext().getApplicationContext();
        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(appContext);
        TextView collectedCoinz = view.findViewById(R.id.fortress_collected);
        TextView depositedCoinz = view.findViewById(R.id.fortress_deposited);
        collectedCoinz.setText(String.format(Locale.getDefault(), "%02d/50 Coinz collected today",
                (int)userCoinzData.getCoinzCollectedToday()));
        depositedCoinz.setText(String.format(Locale.getDefault(),
                "%02d/25 Coinz deposited today",
                (int)userCoinzData.getCoinzDepositedToday()));
        Button collectCoinzButton = view.findViewById(R.id.view_collected_coinz_button);
        collectCoinzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionIntent = new Intent(getActivity(), ViewCollectedCoinzActivity.class);
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity());
                startActivity(transitionIntent, activityOptions.toBundle());
            }
        });
    }
}
