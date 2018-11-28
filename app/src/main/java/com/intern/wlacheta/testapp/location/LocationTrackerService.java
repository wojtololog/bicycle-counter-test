package com.intern.wlacheta.testapp.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.activities.TrackerActivity;
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

import static com.intern.wlacheta.testapp.notifications.App.LOCATION_CHANNEL_ID;

public class LocationTrackerService extends Service implements LocationListener {
    private LocationManager locationManager;

    private final SpeedCalculator speedCalculator = new SpeedCalculator();
    private MapPointModel mapPointModel;

    private TripModel tripModel;
    private List<MapPointModel> mapPointsModel = new ArrayList<>();

    private final int minIntervalTimeInMiliSeconds = 1000;
    private final int minIntervalDistanceInMeters = 0;
    private final short minPointsInterval = 2;
    private static short countToMinPointsInterval = 0;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, TrackerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, LOCATION_CHANNEL_ID)
                .setContentTitle("Bicycle Counter")
                .setContentText("Tracking location !")
                .setContentIntent(pendingIntent)
                .build();

        return notification;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = buildNotification();
        requestForLocation();
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopTracking();
        Intent intent = new Intent("tripWithMapPoints");
        intent.putExtra("tripToSave", tripModel);
        intent.putParcelableArrayListExtra("mapPoints", (ArrayList<? extends Parcelable>) mapPointsModel);
        sendBroadcast(intent);
        clearMapPointsModel();
        super.onDestroy();
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (countToMinPointsInterval == minPointsInterval) {
                speedCalculator.computeSpeed();
                if (location.hasSpeed()) {
                    mapPointModel = new MapPointModel(location.getLatitude(), location.getLongitude(), location.getTime(), (location.getSpeed() * 3.6f), speedCalculator.getSpeed());
                } else {
                    mapPointModel = new MapPointModel(location.getLatitude(), location.getLongitude(), location.getTime(), 0, speedCalculator.getSpeed());
                }
                speedCalculator.addToMapPoints(mapPointModel);

                Intent intent = new Intent("currentLocationListener");
                intent.putExtra("currentLocation", mapPointModel);
                sendBroadcast(intent);

                mapPointsModel.add(mapPointModel);
                countToMinPointsInterval = 0;
            }
            countToMinPointsInterval++;
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


    @SuppressLint("MissingPermission") //it is handled by permission processor
    private void requestForLocation() {
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minIntervalTimeInMiliSeconds, minIntervalDistanceInMeters, this);
        }
    }

    private void stopTracking() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationTrackerService.this);
            tripModel = setTripData();
            speedCalculator.clearMapPoints();
            countToMinPointsInterval = 0;
        }
    }

    private TripModel setTripData() {
        if (mapPointsModel.size() >= 2) {
            MapPointModel firstLocation = mapPointsModel.get(0);
            int lastIndex = mapPointsModel.size() - 1;
            MapPointModel lastLocation = mapPointsModel.get(lastIndex);
            return new TripModel(firstLocation.getTimestamp(), DateConverter.fromTimeStampToDBFormat(firstLocation.getTimestamp()), lastLocation.getTimestamp());
        } else return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void clearMapPointsModel() {
        this.mapPointsModel.clear();
    }
}
