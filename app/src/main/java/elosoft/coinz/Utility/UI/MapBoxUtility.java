package elosoft.coinz.Utility.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.HashMap;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.R;

public class MapBoxUtility {

    private static void addCoinToMap(MapboxMap mapboxMap, Coin coin, Context appContext) {
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
                .icon(IconFactory.getInstance(appContext).fromResource(resourceId));
        mapboxMap.addMarker(markerOptions);
    }

    public static void addCoinzToMap(MapboxMap mapboxMap,
                                     HashMap<String, Coin> coinz,
                                     Context appContext) {
        mapboxMap.clear();
        for (Coin coin : coinz.values()) {
            addCoinToMap(mapboxMap, coin, appContext);
        }
    }

    public static void applyGameSettingsToMap(MapboxMap mapboxMap) {
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

    public static boolean checkMapPermissions(Context appContext) {
        return appContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    public static void requestMapPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1
        );
    }

    public static  HashMap<String, Coin> findCoinzWithinDistance(
            Location playerLocation, HashMap<String, Coin> coinz, double minDistance) {
        HashMap<String, Coin> closetCoinz = new HashMap<>();
        for (Coin coin : coinz.values()) {
            double distanceToCoin = coin.position.distanceTo(new LatLng(playerLocation));
            if (distanceToCoin < minDistance) {
                closetCoinz.put(coin.id, coin);
            }
        }
        return closetCoinz;
    }
}
