package com.intern.wlacheta.testapp.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.location.datamanager.SpeedCalculator;
import com.intern.wlacheta.testapp.location.model.MapPoint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationTracker implements LocationListener {
    private final Context actualContext;
    private final Activity actualActivity;

    private LocationManager locationManager;
    private Location location;

    private final SpeedCalculator speedCalculator = new SpeedCalculator();
    private MapPoint mapPoint;

    private boolean isRequestForLocation;
    private final int minIntervalTimeInMiliSeconds = 3000;
    private final int minIntervalDistanceInMeters = 0;

    public LocationTracker(Context context, Activity activity) {
        actualContext = context;
        actualActivity = activity;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            this.location = location;
            mapPoint = new MapPoint(location.getLatitude(),location.getLongitude());
            setCoordinatesData(mapPoint.getLatitude(), mapPoint.getLongitude());
            setDate(this.location.getTime());
            setSpeed(this.location.getSpeed());
        }
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
       // locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,this, null); //todo wait for GPS established connection
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minIntervalTimeInMiliSeconds, minIntervalDistanceInMeters, this);
      /*  if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                setCoordinatesData(latitude, longitude);
                setDate(location.getTime());
                setSpeed(location.getSpeed());
            }
        }*/
    }

    public void stopTracking() {
        if (locationManager != null) {
            isRequestForLocation = false;
            locationManager.removeUpdates(LocationTracker.this);
        }
    }

    public void setCoordinatesData(double latitude, double longitude) {
        //set double percision by DecimalFormat
       // DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
       // String latitudeToString = String.valueOf(decimalFormat.format(latitude));
       // String longitudeToString = String.valueOf(decimalFormat.format(longitude));
        String latitudeToString = String.valueOf(latitude);
        String longitudeToString = String.valueOf(longitude);

        TextView latitudeData = actualActivity.findViewById(R.id.latitudeData);
        latitudeData.setText(latitudeToString);
        TextView longitudeData = actualActivity.findViewById(R.id.longitudeData);
        longitudeData.setText(longitudeToString);
    }

    private void setDate(long dateInUTCFormat) {
        Date date = new Date(dateInUTCFormat);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateToString = simpleDateFormat.format(date);

        TextView dateData = actualActivity.findViewById(R.id.dateData);
        dateData.setText(dateToString);
    }

    public void setSpeed(float speed) {
        String speedToString = String.valueOf(speed);

        TextView speedData = actualActivity.findViewById(R.id.speedData);
        speedData.setText(speedToString);
    }

    public boolean isRequestForLocation() {
        return isRequestForLocation;
    }
}
