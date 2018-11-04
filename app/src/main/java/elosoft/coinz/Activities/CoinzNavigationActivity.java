package elosoft.coinz.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import elosoft.coinz.R;
import elosoft.coinz.Views.BankView;
import elosoft.coinz.Views.FortressView;
import elosoft.coinz.Views.MapScreenView;
import elosoft.coinz.Views.TradingFloorView;

public class CoinzNavigationActivity extends FragmentActivity {

    private Fragment currentFragment = null;
    private FortressView fortessView = new FortressView();
    private MapScreenView mapScreenView = new MapScreenView();
    private BankView bankView = new BankView();
    private TradingFloorView tradingFloorView = new TradingFloorView();

    private enum SubViewType {
        FORTRESS, TRADING_FLOOR, BANK, MAP, SETTINGS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        loadView(SubViewType.FORTRESS);
        init();
    }

    private void loadView(SubViewType subView) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (subView == SubViewType.FORTRESS) {
            currentFragment = fortessView;
        }
        else if (subView == SubViewType.TRADING_FLOOR) {
            currentFragment = tradingFloorView;
        }
        else if (subView == SubViewType.BANK) {
            currentFragment = bankView;
        }
        else if (subView == SubViewType.MAP) {
            currentFragment = mapScreenView;
        }

        fragmentTransaction.replace(R.id.current_page, currentFragment);
        fragmentTransaction.commit();
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
        ImageButton mapButton = findViewById(R.id.nav_bar_icon_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadView(SubViewType.MAP);
            }
        });
    }
}
