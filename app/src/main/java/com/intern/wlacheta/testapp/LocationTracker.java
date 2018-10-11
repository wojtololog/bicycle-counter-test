package com.intern.wlacheta.testapp;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationTracker implements LocationListener {
    private LocationManager locationManager;
    private boolean isGPSenabled;
    private double latitude;
    private double longitude;

    public LocationTracker(LocationManager locationManager) {
        this.locationManager = locationManager;
        if(locationManager != null) {
            isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
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

    public LocationManager getLocationManager() {
        return locationManager;
    }
}
