package elosoft.coinz.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageButton;

import elosoft.coinz.R;
import elosoft.coinz.Views.BankView;
import elosoft.coinz.Views.FortressView;
import elosoft.coinz.Views.MapScreenView;
import elosoft.coinz.Views.TradingFloorView;

public class CoinzNavigationActivity extends FragmentActivity {

    private Fragment currentFragment = null;
    private FortressView fortressView = new FortressView();
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
        switch (subView) {
            case FORTRESS: currentFragment = fortressView; break;
            case MAP: currentFragment = mapScreenView; break;
            case BANK: currentFragment = bankView; break;
            case TRADING_FLOOR: currentFragment = tradingFloorView; break;
            case SETTINGS: currentFragment = bankView; break;
        }
        fragmentTransaction.replace(R.id.current_page, currentFragment);
        fragmentTransaction.commit();
    }

    private void init() {
        ImageButton tradingFloorButton = findViewById(R.id.nav_bar_trading_floor);
        tradingFloorButton.setOnClickListener(v -> loadView(SubViewType.TRADING_FLOOR));
        ImageButton fortressButton = findViewById(R.id.nav_bar_fortress);
        fortressButton.setOnClickListener(v -> loadView(SubViewType.FORTRESS));
        ImageButton bankButton = findViewById(R.id.nav_bar_bank);
        bankButton.setOnClickListener(v -> loadView(SubViewType.BANK));
        ImageButton mapButton = findViewById(R.id.nav_bar_icon_map);
        mapButton.setOnClickListener(v -> loadView(SubViewType.MAP));
    }

    @Override
    public void onBackPressed() {
        /* Do nothing */
    }
}
