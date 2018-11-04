package elosoft.coinz.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;

public class BankView extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bank, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextEmitter welcomeMessage = view.findViewById(R.id.bank_welcome_message);
        welcomeMessage.emitText();
    }

}
