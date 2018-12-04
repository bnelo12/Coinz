package elosoft.coinz.Views;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.HashMap;

import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.Utility.Network.FireStoreAPI;
import elosoft.coinz.R;

import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinzFromFireStore;
import static elosoft.coinz.Utility.UI.MapBoxUtility.addCoinzToMap;
import static elosoft.coinz.Utility.UI.MapBoxUtility.applyGameSettingsToMap;
import static elosoft.coinz.Utility.UI.MapBoxUtility.checkMapPermissions;
import static elosoft.coinz.Utility.UI.MapBoxUtility.findCoinzWithinDistance;

public class MapScreenView extends Fragment implements LocationEngineListener {

    private MapView mapView;
    private TextEmitter mapMessage;
    private ArrayList<Coin> coinz;
    private ArrayList<Coin> closestCoinz;
    private boolean buttonVisble = false;

    private void initMap() {
        mapView.setStyleUrl(this.getString(R.string.mapbox_style_url));
        mapView.getMapAsync((MapboxMap mapboxMap) -> {
            applyGameSettingsToMap(mapboxMap);
            if (checkMapPermissions(getContext())){
                mapMessage.appendText(" %n Please enable location tracking in application settings.");
            } else {
                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                // There is an explicit check above for permission. Warning here not smart enough to
                // accept check in external function.
                locationComponent.activateLocationComponent(getContext());
                locationComponent.setLocationComponentEnabled(true);
                asyncFetchMapIcons(mapboxMap);
            }
        });
    }

    private void asyncFetchMapIcons(MapboxMap mapboxMap) {
        FireStoreAPI.getInstance().getUserCoinz("bnelo12", task -> {
            coinz = deserializeCoinzFromFireStore(
                    (ArrayList<HashMap<String, Object>>) FireStoreAPI.getTaskResult(task)
                    .get("values")
            );
            addCoinzToMap(mapboxMap, coinz, getContext());
            mapMessage.setVisibility(View.INVISIBLE);
            mapView.setVisibility(View.VISIBLE);
            mapboxMap.getLocationComponent().getLocationEngine().addLocationEngineListener(this);
        });
    }

    public void handleAbleToCollectCoinz(ArrayList<Coin> collectableCoinz) {
        if (closestCoinz == null) {
            Log.d("MapScreenView", "[handleAbleToCollectCoinz] collectableCoinz was null");
        }
        else {
            Log.d("MapScreenView", "[handleAbleToCollectCoinz] can collect coinz");
            if (buttonVisble == false) {
                Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_up);
                Button getCoinzButton = getView().findViewById(R.id.collect_coinz_button);
                getCoinzButton.setVisibility(View.VISIBLE);
                getCoinzButton.startAnimation(slide_up);
                buttonVisble = true;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location Changed", "Changedlocation");
        if (location == null) {
            Log.d("MapScreenView", "[onLocationChanged] location was null");
        }
        else if (this.coinz == null) {
            Log.d("MapScreenView", "[onLocationChanged] coinz was not idealized");
        }
        else {
            closestCoinz = findCoinzWithinDistance(location, this.coinz, 25);
            if (closestCoinz.size() > 0) {
                handleAbleToCollectCoinz(closestCoinz);
            }
            else if (buttonVisble == true) {
                Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_down);
                getView().findViewById(R.id.collect_coinz_button).startAnimation(slide_down);
                buttonVisble = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
        mapMessage = view.findViewById(R.id.opening_map_emitter);
        mapMessage.emitText();
        mapView.onCreate(savedInstanceState);
        initMap();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onConnected() {

    }
}
