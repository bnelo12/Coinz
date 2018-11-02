package elosoft.coinz.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import elosoft.coinz.R;
import elosoft.coinz.Views.BankView;
import elosoft.coinz.Views.FortressView;
import elosoft.coinz.Views.TradingFloorView;

public class CoinzNavigationActivity extends Activity {

    private View currentView = null;

    private enum SubViewType {
        FORTRESS, TRADING_FLOOR, BANK, MAP, SETTINGS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        ConstraintLayout parentLayout = findViewById(R.id.current_page);
        FortressView fv = new FortressView(this);
        currentView = fv;
        fv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        parentLayout.addView(fv);

        init();
    }

    private void loadView(SubViewType subView) {
        ConstraintLayout parentLayout = findViewById(R.id.current_page);
        parentLayout.removeView(currentView);
        if (subView == SubViewType.FORTRESS) {
            currentView = new FortressView(this);
        }
        else if (subView == SubViewType.TRADING_FLOOR) {
            currentView = new TradingFloorView(this);
        }
        else if (subView == SubViewType.BANK) {
            currentView = new BankView(this);
        }
        currentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        parentLayout.addView(currentView);
    }

    private void init() {
        ImageButton tradingFloorButton = findViewById(R.id.nav_bar_trading_floor);
        tradingFloorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadView(SubViewType.TRADING_FLOOR);
            }
        });
        ImageButton fortressButton = findViewById(R.id.nav_bar_fortress);
        fortressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadView(SubViewType.FORTRESS);
            }
        });
        ImageButton bankButton = findViewById(R.id.nav_bar_bank);
        bankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadView(SubViewType.BANK);
            }
        });
    }
}
