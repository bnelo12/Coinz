package elosoft.coinz.Views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.R;

public class MapScreenView extends Fragment {

    private MapView mapView;
    private ArrayList<Coin> coinz = new ArrayList<Coin>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
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

    private void applyMapSettingsToMap(MapboxMap mapboxMap) {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(55.946233, -3.192473))
                .include(new LatLng(55.942617, -3.184319))
                .build();
        mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        mapboxMap.moveCamera(CameraUpdateFactory.bearingTo(-15));
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
        mapboxMap.getUiSettings().setRotateGesturesEnabled(false);
        mapboxMap.getUiSettings().setTiltGesturesEnabled(false);
        mapboxMap.getUiSettings().setZoomGesturesEnabled(false);
        mapboxMap.getUiSettings().setCompassEnabled(false);
        mapboxMap.getUiSettings().setAttributionEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);
    }

    private void addCoinToMap(MapboxMap mapboxMap, Coin coin) {
        int resourceId = R.drawable.icon_gold_coin;
        switch (coin.type) {
            case DOLR: resourceId = R.drawable.icon_dolr_coin; break;
            case PENY: resourceId = R.drawable.icon_peny_coin; break;
            case SHIL: resourceId = R.drawable.icon_shil_coin; break;
            case QUID: resourceId = R.drawable.icon_quid_coin; break;
            case GOLD: resourceId = R.drawable.icon_gold_coin; break;
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(coin.position)
                .icon(IconFactory.getInstance(getContext()).fromResource(resourceId));
        mapboxMap.addMarker(markerOptions);
    }

    private void asyncFetchMapIcons(MapboxMap mapboxMap) {
        // Replace with access to local storage!!
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "http://homepages.inf.ed.ac.uk/stg/coinz/2018/01/01/coinzmap.geojson";

        JsonObjectRequest getCoinzDataRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                (geojson) -> {
                    try {
                        coinz = Coin.parseGeoJSON(geojson);
                        for (Coin coin : coinz) {
                            addCoinToMap(mapboxMap, coin);
                        }
                    }
                    catch (Coin.CoinzGeoJSONParseError e) {
                        Log.e("Error while parsing GeoJSON", e.getErrorMessage());
                    }
                },
                (error) -> {

                });
        queue.add(getCoinzDataRequest);
    }

    private void initMap() {
        mapView.setStyleUrl(this.getString(R.string.mapbox_style_url));
        mapView.getMapAsync((MapboxMap mapboxMap) -> {
            applyMapSettingsToMap(mapboxMap);
            if (MapScreenView.this.getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                // ToDo Prompt user to enable location settings
            } else {
                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(MapScreenView.this.getContext());
                locationComponent.setLocationComponentEnabled(true);
                asyncFetchMapIcons(mapboxMap);
            }
        });
    }

}
