package com.example.productfinding;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.productfinding.model.Catalog;
import com.example.productfinding.util.LocationUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapPopupActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private Catalog mCatalog;
    private Location mCurrentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_map_popup_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mCurrentLocation = new LocationUtil(this, this).getMyCurrentLocation();
        mCatalog = (Catalog) getIntent().getSerializableExtra("catalog");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels*.9);
        int height = (int) (displayMetrics.heightPixels*.7);

        getWindow().setLayout(width,height);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map Ready");
        mMap = googleMap;
        //setMyCurrentLocationEnabled(true);
        setMyCurrentLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        //Move the camera to the user's location and zoom in!
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 12.0f));
        LatLng currentLocationLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        LatLng shopLocationLatLng = mCatalog.getShop().getLatLng();

        LatLngBounds latLngBounds = LatLngBounds.builder().include(shopLocationLatLng).include(currentLocationLatLng).build();
        
        String _shopName = mCatalog.getShop().getName();

        Location _shopLocation = new Location("");
        _shopLocation.setLatitude(mCatalog.getShop().getLatitude());
        _shopLocation.setLongitude(mCatalog.getShop().getLongitude());

        Float distance = LocationUtil.distanceToKM(mCurrentLocation.distanceTo(_shopLocation));

        MarkerOptions markerOptions = new MarkerOptions().position(mCatalog.getShop().getLatLng()).title(_shopName).snippet(String.format("%.2fKM away", distance));
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 135));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission: Check is Location Permission Granted");

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Location Permission")
                        .setMessage("This Application needs Location Permission to Continue")
                        .setPositiveButton("OK", (dialog, which) -> {
                            requestLocationPermission();
                        }).create().show();
            } else
                requestLocationPermission();
            return false;
        }
        return true;
    }

    private void requestLocationPermission() {
        Log.d(TAG, "requestLocationPermission: Requesting Location Permission");
        ActivityCompat.requestPermissions(getParent(),
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_LOCATION);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setMyCurrentLocationEnabled(boolean enabled) {
        Log.d(TAG, "setMyCurrentLocationEnabled: to " + enabled);
        if (checkLocationPermission())
            mMap.setMyLocationEnabled(enabled);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                setMyCurrentLocationEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Activity Destroyed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Activity Paused");

    }
}
