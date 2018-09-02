package com.example.productfinding;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productfinding.util.LocationUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetShopLatLngActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapLongClickListener,
        OnMapReadyCallback {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "GetShopLatLngActivity";
    private GoogleMap mMap;
    private Marker mShopMaker;
    private double mLat, mLng;
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
        mLat = mLng = 0;
        mSearchET = findViewById(R.id.get_shop_et_search);
        mSearchIV = findViewById(R.id.get_shop_iv_search);
        mNextBtn = findViewById(R.id.get_shop_btn_next);
        mNameTV = findViewById(R.id.get_shop_tv_name);
        mLatLngTV = findViewById(R.id.get_shop_tv_latlng);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);

        geocoder = new Geocoder(this);


        mSearchET.setOnClickListener(v -> {
            try {
                //Filter The Result only In Malaysia
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setCountry("MY")
                        .build();

                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
        });

        mNextBtn.setOnClickListener(v -> {
            if (mLat == 0 || mLng == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("You Must Select a Place to Add Shop")
                        .setPositiveButton("OK", null).show();
                return;
            }

            Intent i = new Intent(this, AddNewShop.class);
            i.putExtra("shop_name", mNameTV.getText().toString().trim());
            i.putExtra("lat", mLat);
            i.putExtra("lng", mLng);

            startActivity(i);

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
            setLatLng(pointOfInterest.latLng);
            mLatLngTV.setText(getLatLng().toString());
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
        setLatLng(latLng);
        mLatLngTV.setText(getLatLng().toString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());

                mMap.clear();
                mShopMaker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).draggable(true));
                float currentZoom = mMap.getCameraPosition().zoom;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), currentZoom > 16f ? currentZoom : 16f));
                mNameTV.setText(place.getName());
                setLatLng(place.getLatLng());
                mLatLngTV.setText(getLatLng().toString());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void setLatLng(LatLng latLng) {
        mLat = latLng.latitude;
        mLng = latLng.longitude;
    }

    private LatLng getLatLng() {
        return new LatLng(mLat, mLng);
    }
}
