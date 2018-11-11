package com.intern.wlacheta.testapp.activities;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.MapPointsViewModel;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.TripsViewModel;
import com.intern.wlacheta.testapp.database.entities.MapPoint;
import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.utils.DateConverter;
import com.intern.wlacheta.testapp.location.LocationTrackerService;
import com.intern.wlacheta.testapp.location.model.MapPointModel;
import com.intern.wlacheta.testapp.location.model.TripModel;
import com.intern.wlacheta.testapp.mappers.MapPointMapper;
import com.intern.wlacheta.testapp.mappers.TripMapper;
import com.intern.wlacheta.testapp.permissions.PermissionsProcessor;

import java.text.DecimalFormat;
import java.util.List;

public class TrackerActivity extends AppCompatActivity {
    //private final LocationTrackerService locationTrackerService = new LocationTrackerService(this, this);
    private final PermissionsProcessor permissionsProcessor = new PermissionsProcessor(this, this);
    private BroadcastReceiver locationDataReceiver, tripToSaveWithMapPointsReceiver;

    private Button startButton, stopButton;
    private TripsViewModel tripsViewModel;
    private MapPointsViewModel mapPointsViewModel;

    private TripModel tripToSaveModel;
    private List<MapPointModel> mapPointsToSave;

    private TextView longitudeTextView, latitudeTextView, dateTextView, locationSpeedTextView, computedSpeedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        if (permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestRequiredPermissions();
        }
        findViews();
        createReceivers();
        stopButton.setEnabled(false);
    }

    private void createReceivers() {
        if(locationDataReceiver == null) {
            locationDataReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    MapPointModel actualLocationData = (MapPointModel) intent.getExtras().get("currentLocation");
                    setLocationDataUI(actualLocationData);
                }
            };
        }
        registerReceiver(locationDataReceiver, new IntentFilter("currentLocationListener"));

        if(tripToSaveWithMapPointsReceiver == null) {
            tripToSaveWithMapPointsReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    tripToSaveModel = (TripModel) intent.getExtras().get("tripToSave");
                    mapPointsToSave = intent.getParcelableArrayListExtra("mapPoints");
                }
            };
        }
        registerReceiver(tripToSaveWithMapPointsReceiver, new IntentFilter("tripWithMapPoints"));
    }

    private void setLocationDataUI(MapPointModel actualLocationData) {
        if(actualLocationData != null) {
            final DecimalFormat speedFormat = new DecimalFormat("###.#");

            locationSpeedTextView.setText(speedFormat.format(actualLocationData.getLocationSpeed()));
            computedSpeedTextView.setText(speedFormat.format(actualLocationData.getComputedSpeed()));
            longitudeTextView.setText(String.valueOf(actualLocationData.getLongitude()));
            latitudeTextView.setText(String.valueOf(actualLocationData.getLongitude()));
            dateTextView.setText(DateConverter.fromTimeStampToString(actualLocationData.getTimestamp()));
        }
    }


    private void findViews() {
        stopButton = findViewById(R.id.stopButton);
        startButton = findViewById(R.id.startButton);
        longitudeTextView = findViewById(R.id.longitudeData);
        latitudeTextView = findViewById(R.id.latitudeData);
        dateTextView = findViewById(R.id.dateData);
        locationSpeedTextView = findViewById(R.id.locationSpeedData);
        computedSpeedTextView = findViewById(R.id.computedSpeedData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tracker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trips_tracker:
                showSavedTrips();
                return true;
            case R.id.settings_tracker:
                showSettings();
                return true;
            case R.id.help_tracker:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //todo new activities
    private void showHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void showSavedTrips() {
        Intent intent = new Intent(this, TripsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(locationDataReceiver);
        unregisterReceiver(tripToSaveWithMapPointsReceiver);
        super.onDestroy();
    }

    public void onStartButtonClick(View view) {
        if (permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestRequiredPermissions();
        } else {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            Intent serviceIntent = new Intent(this, LocationTrackerService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    private void createSaveToDBRequestDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Saving")
                .setCancelable(false)
                .setMessage("Would you like to save your trip ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertTripToDB(tripToSaveModel);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void insertTripToDB(TripModel tripToSaveModel) {
        if(tripToSaveModel != null) {
            TripMapper tripMapper = new TripMapper();
            Trip tripToInsert = tripMapper.fromModelToDB(tripToSaveModel);
            tripsViewModel = ViewModelProviders.of(TrackerActivity.this).get(TripsViewModel.class);
            long tripID = tripsViewModel.insert(tripToInsert);
            insertMapPointsForTrip(tripID);
        } else {
            Toast.makeText(TrackerActivity.this, "Wait for established GPS connection!", Toast.LENGTH_LONG).show();
        }
    }

    private void insertMapPointsForTrip(long tripID) {
        MapPointMapper mapPointMapper = new MapPointMapper(tripID);
        List<MapPoint> mapPointsDB = mapPointMapper.fromModelMapPointsToDB(mapPointsToSave);
        mapPointsViewModel = ViewModelProviders.of(TrackerActivity.this).get(MapPointsViewModel.class);
        for(int i = 0; i < mapPointsDB.size(); i++) {
            mapPointsViewModel.insert(mapPointsDB.get(i));
        }
        Toast.makeText(TrackerActivity.this, "Trip saved", Toast.LENGTH_SHORT).show();
    }

    public void onStopButtonClick(View view) {
        clearTrackingUIData();
        destroyTrackingService();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        createSaveToDBRequestDialog();
    }

    private void destroyTrackingService() {
        Intent serviceIntent = new Intent(this,LocationTrackerService.class);
        stopService(serviceIntent);
    }


    private void clearTrackingUIData() {
        locationSpeedTextView.setText("");
        computedSpeedTextView.setText("");
        longitudeTextView.setText("");
        latitudeTextView.setText("");
        dateTextView.setText("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == permissionsProcessor.getAllPermisionsCode()) {
            if (grantResults.length > 0 && ((grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) && grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permissions GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
