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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import elosoft.coinz.R;

public class MapScreenView extends Fragment {

    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        init();
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

    private void init() {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url ="http://homepages.inf.ed.ac.uk/stg/coinz/2018/01/01/coinzmap.geojson";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        initMap(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void initMap(JSONObject geojson) {
        mapView.setStyleUrl("mapbox://styles/bnelo12/cjo2xxw9022ju2rqq100qygz8");
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                        .include(new LatLng(55.946233, -3.192473))
                        .include(new LatLng(55.942617, -3.184319))
                        .build();
                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                mapboxMap.moveCamera(CameraUpdateFactory.bearingTo(-20));
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
                mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
                mapboxMap.getUiSettings().setRotateGesturesEnabled(false);
                mapboxMap.getUiSettings().setTiltGesturesEnabled(false);
                mapboxMap.getUiSettings().setZoomGesturesEnabled(false);
                mapboxMap.getUiSettings().setCompassEnabled(false);
                mapboxMap.getUiSettings().setAttributionEnabled(false);
                mapboxMap.getUiSettings().setLogoEnabled(false);
                if (MapScreenView.this.getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    // ToDo Prompt user to enable location settings
                } else {
                    LocationComponent locationComponent = mapboxMap.getLocationComponent();
                    locationComponent.activateLocationComponent(MapScreenView.this.getContext());
                    locationComponent.setLocationComponentEnabled(true);
                    try {
                        JSONArray features = geojson.getJSONArray("features");
                        for (int i = 0; i < features.length(); i++) {
                            JSONObject feature = features.getJSONObject(i);
                            MarkerOptions markOptions = new MarkerOptions()
                                    .position(new LatLng(
                                            feature.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1),
                                            feature.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0)))
                                    .icon(IconFactory.getInstance(MapScreenView.this.getContext()).fromResource(R.drawable.icon_gold_coin));
                            mapboxMap.addMarker(markOptions);
                        }
                    } catch (JSONException e) {
                        // Todo No coins found
                        Log.e("Coinz JSON Error", "Unable to parse JSON");
                    }
                }
            }
        });
    }

}
