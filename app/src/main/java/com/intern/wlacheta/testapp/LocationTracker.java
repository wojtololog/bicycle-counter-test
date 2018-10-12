package com.intern.wlacheta.testapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationTracker implements LocationListener {
    private final Context actualContext;

    private LocationManager locationManager;
    private Location location;

    private boolean isGPSenabled;
    private double latitude;
    private double longitude;
    private final int minIntervalTimeInMiliSeconds = 1000;
    private final int minIntervalDistanceInMeters = 1;

    @SuppressLint("MissingPermission")
    public LocationTracker(Context context) {
        actualContext = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
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

    @SuppressLint("MissingPermission")
    public void requestForLocation() {
        locationManager = (LocationManager) actualContext.getSystemService(Context.LOCATION_SERVICE);
        isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!isGPSenabled) {
            Toast.makeText(actualContext, "Ups GPS is not working", Toast.LENGTH_SHORT).show();
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minIntervalTimeInMiliSeconds, minIntervalDistanceInMeters, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        }
    }

    public double getLatitude() {
        if(location != null) {
          return latitude;
        }
        return 0;
    }

    public double getLongitude() {
        if(location != null) {
            return longitude;
        }
        return 0;
    }
}
