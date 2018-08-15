package com.example.productfinding;


import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.productfinding.util.KeyboardUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "MyMapFragment";
    public static final int PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    private View mView;
    List<String> mAllCategory;

    public MyMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_map_search, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map Ready");
        mMap = googleMap;

        setMyCurrentLocationEnabled(true);

        if (checkLocationPermission()) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));


            List<MarkerOptions> markerOptionsList = new ArrayList<>();

//            MarkerOptions tescoKamparMarker = new MarkerOptions().position(new LatLng(4.3353572, 101.1538988)).title("Tesco Kampar");
//            MarkerOptions econsaveTaipingMarker = new MarkerOptions().position(new LatLng(4.8915194, 100.7309496)).title("Econsave Taiping");
//            MarkerOptions econsaveKamaprMarker = new MarkerOptions().position(new LatLng(4.327343, 101.146956)).title("Econsave Kampar");
//            MarkerOptions gaintKamparMarker = new MarkerOptions().position(new LatLng(4.342949, 101.154668)).title("Gaint Kampar");
//            MarkerOptions sevenElevenKamparMarker = new MarkerOptions().position(new LatLng(4.326428, 101.143390)).title("7-eleven Kampar");
//
//            markerOptionsList.add(tescoKamparMarker);
//            markerOptionsList.add(econsaveTaipingMarker);
//            markerOptionsList.add(econsaveKamaprMarker);
//            markerOptionsList.add(gaintKamparMarker);
//            markerOptionsList.add(sevenElevenKamparMarker);
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(4.310732, 101.151670)).title("PASARAYA ECON KAMPAR"));
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(4.327045,101.145238)).title("Paris Mini Baker"));
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(4.309903,101.153329)).title("Butik Binari"));
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(4.310185,101.152100)).title("mmCineplexes Kampar"));
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(4.073788,101.157545)).title("K. Muthalagu Chettiar Enterprise"));
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(4.595913,101.090094)).title("Ipoh Parade"));
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(4.594878,101.085817)).title("Expressway Service Tires"));
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(4.595989,101.077307)).title("Funtasy House Trick Art"));
//            markerOptionsList.add(new MarkerOptions().position(new LatLng(3.161801,101.570630)).title(" Glass & Plastic Packaging Sdn Bhd"));

            Button mSearchButton = mView.findViewById(R.id.search_iv_search_button);
            EditText mUserSearch = mView.findViewById(R.id.search_et_search_item);

            mSearchButton.setOnClickListener(v -> {
                KeyboardUtil.hideSoftKeyboard(getActivity());
                mMap.clear();
                if (mUserSearch.getText().toString() == null || mUserSearch.getText().toString().trim().isEmpty() || mUserSearch.getText().toString().trim() == "") {
                    Toast.makeText(getContext(), "Empty String", Toast.LENGTH_SHORT).show();
                } else {
                    String query = mUserSearch.getText().toString();
                    for (int i = 0; i < markerOptionsList.size(); i++) {
                        int abc = (int) ((Math.random() * 10) % 2);
                        if (abc == 0) {
                            mMap.addMarker(markerOptionsList.get(i));
                        }
                    }
                    Toast.makeText(getContext(), "Success search: " + query, Toast.LENGTH_SHORT).show();
                }


            });


            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (checkLocationPermission()) {
                        Location targetLocation = new Location(LocationManager.GPS_PROVIDER);
                        targetLocation.setLatitude(marker.getPosition().latitude);
                        targetLocation.setLongitude(marker.getPosition().longitude);
                        float distance = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false)).distanceTo(targetLocation);
                        Toast.makeText(getContext(), "Distance is: " + distance+" Meter", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }
    }


    private boolean checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission: Check is Location Permission Granted");

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getContext())
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
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_LOCATION);
    }

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
