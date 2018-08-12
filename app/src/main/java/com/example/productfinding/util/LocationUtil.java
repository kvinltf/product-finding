package com.example.productfinding.util;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;


public class LocationUtil extends Service implements LocationListener {
    private static final String TAG = "LocationUtil";
    static private final int TIME_INTERVAL = 1000; // minimum time between updates in milliseconds
    static private final int DISTANCE_INTERVAL = 1; // minimum distance between updates in meters
    private static final int LOCATION_CODE = 99;

    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location myLocation;
    private Location fusedLocation;
    private boolean isGPSEnable = false;
    private boolean isNetworkEnable = false;
    private double latitude, longitude;

    private Activity activity;

    private Intent intent;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1000;
    private Context context;


    public LocationUtil(Activity activity, Context context) {
        super();
        this.context = context;
        this.activity = activity;
        this.locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * @return null or Location
     */
    public Location getMyCurrentLocation() {
        Log.d(TAG, "getMyCurrentLocation: Getting Current Location.");
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getMyCurrentLocation: Requesting Permission From User.");

            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    LOCATION_CODE);
        }

        Log.d(TAG, "getMyCurrentLocation: Check is GPS or Network provider available.");
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Log.d(TAG, "getMyCurrentLocation: Both GPS and Network Provider NOT Available.");
            return null;
        } else {
            myLocation = null;
            if (isNetworkEnable)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_INTERVAL, DISTANCE_INTERVAL, this);
            else if (isGPSEnable)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_INTERVAL, DISTANCE_INTERVAL, this);

            if (locationManager != null) {
                Log.d(TAG, "getMyCurrentLocation: Using " + locationManager.NETWORK_PROVIDER);
                myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (myLocation != null) {
                    Log.d(TAG, "getMyCurrentLocation: " + myLocation.toString());
                }
            }
        }
        return myLocation;
    }

    public static float distanceToKM(float distance){
        return distance / (float) 1000;
    }
}
