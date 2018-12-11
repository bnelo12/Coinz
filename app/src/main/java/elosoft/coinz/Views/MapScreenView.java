package elosoft.coinz.Views;

import android.app.ActivityOptions;
import android.content.Intent;
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

import java.util.HashMap;

import elosoft.coinz.Activities.CollectCoinzActivity;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;
import elosoft.coinz.R;

import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinzFromFireStore;
import static elosoft.coinz.Utility.Serialize.SerializeCoin.serializeCoinz;
import static elosoft.coinz.Utility.UI.MapBoxUtility.addCoinzToMap;
import static elosoft.coinz.Utility.UI.MapBoxUtility.applyGameSettingsToMap;
import static elosoft.coinz.Utility.UI.MapBoxUtility.checkMapPermissions;
import static elosoft.coinz.Utility.UI.MapBoxUtility.findCoinzWithinDistance;

public class MapScreenView extends Fragment implements LocationEngineListener {

    private MapView mapView;
    private TextEmitter mapMessage;
    private HashMap<String, Coin> coinz;
    private HashMap<String, Coin> closestCoinz;
    private boolean buttonVisble = false;
    private MapboxMap currentMap = null;

    private void initMap() {
        mapView.setStyleUrl(this.getString(R.string.mapbox_style_url));
        mapView.getMapAsync((MapboxMap mapboxMap) -> {
            applyGameSettingsToMap(mapboxMap);
            this.currentMap = mapboxMap;
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
        String currentUser = LocalStorageAPI.getLoggedInUserName(getActivity().getApplicationContext());
        FireStoreAPI.getInstance().getUserCollectableCoinz(currentUser, task -> {
            HashMap<String, Object> coinzData = (HashMap<String, Object>) FireStoreAPI
                    .getTaskResult(task);
            coinz = deserializeCoinzFromFireStore(coinzData);
            addCoinzToMap(mapboxMap, coinz, getContext());
            mapMessage.setVisibility(View.GONE);
            mapView.setVisibility(View.VISIBLE);
            mapboxMap.getLocationComponent().getLocationEngine().addLocationEngineListener(this);
        });
    }

    public void handleAbleToCollectCoinz(HashMap<String, Coin> collectableCoinz) {
        if (closestCoinz == null) {
            Log.d("MapScreenView", "[handleAbleToCollectCoinz] collectableCoinz was null");
        }
        else {
            Log.d("MapScreenView", "[handleAbleToCollectCoinz] can collect coinz");
            Button collecetCoinzButton = getView().findViewById(R.id.collect_coinz_button);
            slideCollectButtonUp(collecetCoinzButton);
            buttonVisble = true;
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
            } else {
                Button collecetCoinzButton = getView().findViewById(R.id.collect_coinz_button);
                slideCollectButtonDown(collecetCoinzButton);
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
        Button collectCoinzButton = view.findViewById(R.id.collect_coinz_button);
        collectCoinzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUser = LocalStorageAPI.getLoggedInUserName(getActivity().getApplicationContext());
                FireStoreAPI.getInstance().removeUserCollectibleCoinz(currentUser, closestCoinz.values());
                FireStoreAPI.getInstance().addUserCollectedCoinz(currentUser, closestCoinz.values());
                LocalStorageAPI.updateUserCoinzData(getContext().getApplicationContext(), closestCoinz.values());
                Intent transitionIntent = new Intent(getActivity(), CollectCoinzActivity.class);
                transitionIntent.putExtra("NUMBER_OF_COINZ", closestCoinz.size());
                transitionIntent.putExtra("COINZ", serializeCoinz(closestCoinz));
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity());
                startActivity(transitionIntent, activityOptions.toBundle());
            }
        });
    }

    private void slideCollectButtonUp(Button collecetCoinzButton) {
        if (!buttonVisble) {
            collecetCoinzButton.setVisibility(View.VISIBLE);
            Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_up);
            collecetCoinzButton.startAnimation(slide_up);
            buttonVisble = true;
        }
    }

    private void slideCollectButtonDown(Button collecetCoinzButton) {
        if (buttonVisble) {
            collecetCoinzButton.setVisibility(View.GONE);
            Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_down);
            collecetCoinzButton.startAnimation(slide_down);
            buttonVisble = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        Log.d("Resuming", "Resuming MapView");
        super.onResume();
        Button collecetCoinzButton = getView().findViewById(R.id.collect_coinz_button);
        slideCollectButtonDown(collecetCoinzButton);
        initMap();
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
