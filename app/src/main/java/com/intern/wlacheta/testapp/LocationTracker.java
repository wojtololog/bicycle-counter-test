package com.intern.wlacheta.testapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class LocationTracker implements LocationListener {
    private final Context actualContext;
    private final Activity actualActivity;

    private LocationManager locationManager;
    private Location location;

    private boolean isGPSenabled,isRequestForLocation;
    private double latitude;
    private double longitude;
    private final int minIntervalTimeInMiliSeconds = 1000;
    private final int minIntervalDistanceInMeters = 1;

    public LocationTracker(Context context, Activity activity) {
        actualContext = context;
        actualActivity  = activity;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        setCoordinatesData(location.getLatitude(),location.getLongitude());
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
        isRequestForLocation = true;
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

    public void stopTracking() {
        if(locationManager!=null) {
            isRequestForLocation = false;
            locationManager.removeUpdates(LocationTracker.this);
        }
    }

    private void setCoordinatesData(double latitude, double longitude) {
        String latitudeToString = String.valueOf(latitude);
        String longitudeToString = String.valueOf(longitude);

        TextView latitudeData = actualActivity.findViewById(R.id.latitudeData);
        latitudeData.setText(latitudeToString);
        TextView longitudeData = actualActivity.findViewById(R.id.longitudeData);
        longitudeData.setText(longitudeToString);
    }

    public double getLatitude() {
        if(location != null) {
           latitude = location.getLatitude();
           return latitude;
        }
        return 0;
    }

    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
            return longitude;
        }
        return 0;
    }

    public boolean isRequestForLocation() {
        return isRequestForLocation;
    }
}
