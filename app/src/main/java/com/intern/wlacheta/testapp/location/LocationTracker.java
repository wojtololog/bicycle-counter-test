package com.intern.wlacheta.testapp.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.utils.DateConverter;
import com.intern.wlacheta.testapp.location.datamanager.SpeedCalculator;
import com.intern.wlacheta.testapp.location.model.MapPointModel;
import com.intern.wlacheta.testapp.location.model.TripModel;
import com.intern.wlacheta.testapp.mappers.TripMapper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationTracker implements LocationListener {
    private final Context actualContext;
    private final Activity actualActivity;

    private LocationManager locationManager;
    private Location location;

    private final SpeedCalculator speedCalculator = new SpeedCalculator();
    private MapPointModel mapPointModel;

    private TripModel tripModel;
    private TripMapper tripMapper = new TripMapper();
    private List<MapPointModel> mapPointsModel = new ArrayList<MapPointModel>();

    private Trip tripToSave;

    private boolean isRequestForLocation;
    private final int minIntervalTimeInMiliSeconds = 1000;
    private final int minIntervalDistanceInMeters = 0;
    private final short minPointsInterval = 2;
    private static short countToMinPointsInterval = 0;
    private final DecimalFormat speedFormat = new DecimalFormat("##.#");

    public LocationTracker(Context context, Activity activity) {
        actualContext = context;
        actualActivity = activity;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            this.location = location;
            mapPointModel = new MapPointModel(location.getLatitude(),location.getLongitude(),location.getTime());
            mapPointsModel.add(mapPointModel);
            if (countToMinPointsInterval == minPointsInterval) {
                speedCalculator.addToMapPoints(mapPointModel);
                countToMinPointsInterval = 0;
            }
            setCoordinatesData(mapPointModel.getLatitude(), mapPointModel.getLongitude());
            setDate(mapPointModel.getTimestamp());
            speedCalculator.computeSpeed();
            if(this.location.hasSpeed()) {
                setLocationSpeed(location.getSpeed());
            }
            setSpeed(speedCalculator.getSpeed());
            countToMinPointsInterval++;
        }
    }

    public void setLocationSpeed(float speed) {
        String speedToString = String.valueOf(speed);

        TextView speedData = actualActivity.findViewById(R.id.locationSpeedData);
        speedData.setText(speedToString);
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


    @SuppressLint("MissingPermission") //it is handled by permission processor
    public void requestForLocation() {
        isRequestForLocation = true;
        locationManager = (LocationManager) actualContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minIntervalTimeInMiliSeconds, minIntervalDistanceInMeters, this);
    }

    public void stopTracking() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationTracker.this);
            tripModel = setTripData();
            tripToSave = tripMapper.fromModelToDB(tripModel);
            isRequestForLocation = false;

            speedCalculator.clearMapPoints();
            this.mapPointsModel.clear();
        }
    }

    private TripModel setTripData() {
        if(mapPointsModel.size() >= 2) {
            MapPointModel firstLocation = mapPointsModel.get(0);
            int lastIndex = mapPointsModel.size() - 1;
            MapPointModel lastLocation = mapPointsModel.get(lastIndex);
            return new TripModel(firstLocation.getTimestamp(), DateConverter.fromTimeStampToDBFormat(firstLocation.getTimestamp()), lastLocation.getTimestamp());
        }
        else return null;
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

    public void setSpeed(double speed) {
        String speedToString = String.valueOf(speed);

        TextView speedData = actualActivity.findViewById(R.id.computedSpeedData);
        speedData.setText(speedToString);
    }

    public Trip getTripToSave() {
        return tripToSave;
    }

    public boolean isRequestForLocation() {
        return isRequestForLocation;
    }
}
