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

import elosoft.coinz.Activities.ViewCollectedCoinzActivity;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.R;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;

public class FortressView extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fortress, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextEmitter welcomeMessage = view.findViewById(R.id.fortress_welcome_message);
        welcomeMessage.emitText();
        Context appContext = getContext().getApplicationContext();
//        TextView penyAmount = view.findViewById(R.id.fortress_peny_amount_text);
//        TextView penyAmountGold = view.findViewById(R.id.fortress_peny_amount_gold_text);
//        TextView dolrAmount = view.findViewById(R.id.fortress_dolr_amount_text);
//        TextView dolrAmountGold = view.findViewById(R.id.fortress_dolr_amount_gold_text);
//        TextView shilAmount = view.findViewById(R.id.fortress_shil_amount_text);
//        TextView shilAmountGold = view.findViewById(R.id.fortress_shil_amount_gold_text);
//        TextView quidAmount = view.findViewById(R.id.fortress_quid_amount_text);
//        TextView quidAmountGold = view.findViewById(R.id.fortress_quid_amount_gold_text);
//        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(appContext);
//        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(appContext, exchangeRate);
//        penyAmount.setText(String.format("%.5f", userCoinzData.getNumPENY()));
//        penyAmountGold.setText(String.format("%.5f GOLD", userCoinzData.getNumPENYInGOLD()));
//        dolrAmount.setText(String.format("%.5f", userCoinzData.getNumDOLR()));
//        dolrAmountGold.setText(String.format("%.5f GOLD", userCoinzData.getNumDOLRInGOLD()));
//        shilAmount.setText(String.format("%.5f", userCoinzData.getNumSHIL()));
//        shilAmountGold.setText(String.format("%.5f GOLD", userCoinzData.getNumSHILInGOLD()));
//        quidAmount.setText(String.format("%.5f", userCoinzData.getNumQUID()));
//        quidAmountGold.setText(String.format("%.5f GOLD", userCoinzData.getNumQUIDInGOLD()));
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
