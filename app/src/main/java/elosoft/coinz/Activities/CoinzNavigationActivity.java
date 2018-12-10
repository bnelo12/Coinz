package elosoft.coinz.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

import elosoft.coinz.R;
import elosoft.coinz.Views.BankView;
import elosoft.coinz.Views.FortressView;
import elosoft.coinz.Views.MapScreenView;
import elosoft.coinz.Views.TradingFloorView;

public class CoinzNavigationActivity extends FragmentActivity {

    private Fragment currentFragment;
    private SubViewType currentFragmentType = SubViewType.FORTRESS;
    private FortressView fortressView = new FortressView();
    private MapScreenView mapScreenView = new MapScreenView();
    private BankView bankView = new BankView();
    private TradingFloorView tradingFloorView = new TradingFloorView();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private enum SubViewType {
        FORTRESS, TRADING_FLOOR, BANK, MAP, SETTINGS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentFragment = fortressView;
        setContentView(R.layout.home_screen);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.current_page, currentFragment);
        ft.commit();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadView(SubViewType subView) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (currentFragmentType == subView) {
            Log.d("Fragment Manager", "Fragment already visible!");
        }
        else if (subView == SubViewType.MAP) {
            ft.remove(currentFragment);
            Log.d("Fragment Manager", "Sub View Selected is Map");
            Log.d("Fragment Manager", "Removieng old fragment");

            if (!mapScreenView.isAdded()) {
                Log.d("Fragment Manager", "Map not added. Creating new instance");
                ft.add(R.id.current_page, mapScreenView);
            }
            Log.d("Fragment Manager", "Showing map.");
            ft.show(mapScreenView);
        } else {
            Log.d("Fragment Manager", "Sub view selected is not map.");
            Log.d("Fragment Manager", "Removeing current fragment.");

            if (currentFragmentType != SubViewType.MAP) ft.remove(currentFragment);

            switch (subView) {
                case FORTRESS: currentFragment = fortressView; break;
                case MAP: currentFragment = mapScreenView; break;
                case BANK: currentFragment = bankView; break;
                case TRADING_FLOOR: currentFragment = tradingFloorView; break;
                case SETTINGS: currentFragment = bankView; break;
            }

            if (!mapScreenView.isHidden()) {
                ft.hide(mapScreenView);
            }

            Log.d("Fragment Manager", "Adding and showing new fragment.");
            ft.add(R.id.current_page, currentFragment, subView.toString());
            ft.show(currentFragment);
        }

        ft.commit();

        currentFragmentType = subView;
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
