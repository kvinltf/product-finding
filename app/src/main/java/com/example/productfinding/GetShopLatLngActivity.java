package com.example.productfinding;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productfinding.util.LocationUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GetShopLatLngActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapLongClickListener,
        OnMapReadyCallback {

    private static final String TAG = "GetShopLatLngActivity";
    private GoogleMap mMap;
    private Marker mShopMaker;
    private double mLat;
    private double mLng;
    private Location mCurrentLocation;

    private EditText mSearchET;
    private ImageView mSearchIV;
    private Button mNextBtn;
    private TextView mNameTV, mLatLngTV;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_shop_lat_lng);
        mCurrentLocation = new LocationUtil(this, this).getMyCurrentLocation();

        mSearchET = findViewById(R.id.get_shop_et_search);
        mSearchIV = findViewById(R.id.get_shop_iv_search);
        mNextBtn = findViewById(R.id.get_shop_btn_next);
        mNameTV = findViewById(R.id.get_shop_tv_name);
        mLatLngTV = findViewById(R.id.get_shop_tv_latlng);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);

        geocoder = new Geocoder(this);


        mSearchIV.setOnClickListener(v -> {
            String name = mSearchET.getText().toString();
            try {
                List<Address> addressList = geocoder.getFromLocationName(name, 1);

                for (Address address : addressList) {
                    Log.d(TAG, "onCreate: ADDRESSLIST::" + address.toString());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mLat = marker.getPosition().latitude;
        mLng = marker.getPosition().longitude;
        marker.setTitle("");
        mNameTV.setText("");
        marker.setSnippet("");
        mLatLngTV.setText(new LatLng(mLat, mLng).toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 13f));

        mMap.setOnMarkerClickListener(this::onMarkerClick);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this::onMapLongClick);

        mMap.setOnPoiClickListener(pointOfInterest -> {
            mMap.clear();
            Toast.makeText(GetShopLatLngActivity.this, pointOfInterest.placeId, Toast.LENGTH_SHORT).show();
            mShopMaker = mMap.addMarker(new MarkerOptions().position(pointOfInterest.latLng).draggable(true).title(pointOfInterest.name));
            mShopMaker.setSnippet("LatLng: " + pointOfInterest.latLng.toString());

            float currentZoom = mMap.getCameraPosition().zoom;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pointOfInterest.latLng, currentZoom > 14f ? currentZoom : 14f));
            mNameTV.setText(pointOfInterest.name);
            mLatLngTV.setText(pointOfInterest.latLng.toString());
        });
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        Toast.makeText(this, latLng.toString() + "\n" + mMap.getCameraPosition().zoom, Toast.LENGTH_SHORT).show();
        mShopMaker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        float currentZoom = mMap.getCameraPosition().zoom;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, currentZoom > 14f ? currentZoom : 14f));
        mNameTV.setText("");
        mLatLngTV.setText(latLng.toString());

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            Log.d(TAG, "onMapLongClick: ADDRESS::"+addressList.get(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
