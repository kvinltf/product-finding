package com.example.productfinding;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.productfinding.model.Catalog;
import com.example.productfinding.model.CatalogHolder;
import com.example.productfinding.model.Shop;
import com.example.productfinding.util.LocationUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private List<Catalog> mCatalogList = CatalogHolder.getCatalogListHolder();
    private Location mCurrentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mCurrentLocation = new LocationUtil(this, this).getMyCurrentLocation();

        Log.d(TAG, "onCreate: CatalogHolder:: " + mCatalogList.size());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map Ready");
        mMap = googleMap;
        setMyCurrentLocationEnabled(true);

        //Move the camera to the user's location and zoom in!
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 12.0f));

        mMap.clear();

        List<MarkerOptions> markerOptionsList = new ArrayList<>();

        List<Shop> uniqueShopList = new ArrayList<>();
        for (int i = 0; i < mCatalogList.size(); i++) {
            if (uniqueShopList.isEmpty()) {
                uniqueShopList.add(mCatalogList.get(i).getShop());
            } else {
                boolean _isSameFound = false;
                for (int j = 0; j < uniqueShopList.size(); j++) {
                    if (uniqueShopList.get(j).getName().equalsIgnoreCase(mCatalogList.get(i).getShop().getName())) {
                        _isSameFound = true;
                        break;
                    }
                }
                if (!_isSameFound) uniqueShopList.add(mCatalogList.get(i).getShop());
            }
        }

        for (int i = 0; i < uniqueShopList.size(); i++) {
            LatLng latLng = uniqueShopList.get(i).getLatLng();

            String _shopName = uniqueShopList.get(i).getName();
            Location _shopLocation = new Location("");
            _shopLocation.setLatitude(uniqueShopList.get(i).getLatitude());
            _shopLocation.setLongitude(uniqueShopList.get(i).getLongitude());
            Float distance = LocationUtil.distanceToKM(mCurrentLocation.distanceTo(_shopLocation));

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(_shopName).snippet(String.format("%.2fKM away",distance));


            mMap.addMarker(markerOptions);
        }
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
}
