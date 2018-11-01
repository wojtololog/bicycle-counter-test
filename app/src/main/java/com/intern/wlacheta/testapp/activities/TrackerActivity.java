package com.intern.wlacheta.testapp.activities;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.MapPointsViewModel;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.TripsViewModel;
import com.intern.wlacheta.testapp.database.entities.MapPoint;
import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.repositories.TripRepository;
import com.intern.wlacheta.testapp.location.LocationTracker;
import com.intern.wlacheta.testapp.mappers.MapPointMapper;
import com.intern.wlacheta.testapp.permissions.PermissionsProcessor;

import java.util.ArrayList;
import java.util.List;

public class TrackerActivity extends AppCompatActivity {
    private final LocationTracker locationTracker = new LocationTracker(this, this);
    private final PermissionsProcessor permissionsProcessor = new PermissionsProcessor(this, this);

    private Button startButton, stopButton;
    private TripsViewModel tripsViewModel;
    private MapPointsViewModel mapPointsViewModel;

    private int searchedID;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        if (permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestLocationPermissions();
        }
        stopButton = findViewById(R.id.stopButton);
        startButton = findViewById(R.id.startButton);
        stopButton.setEnabled(false);
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
        if (locationTracker.isRequestForLocation()) {
            locationTracker.requestForLocation();
        }
    }

    public void onStartButtonClick(View view) {
        if (permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestLocationPermissions();
        } else {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            locationTracker.requestForLocation();
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
                        insertTripToDB();
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

    private void insertTripToDB() {
        Trip tripToInsert = locationTracker.getTripToSave();
        if(tripToInsert != null) {
            tripsViewModel = ViewModelProviders.of(TrackerActivity.this).get(TripsViewModel.class);
            long tripID = tripsViewModel.insert(tripToInsert); //
            Toast.makeText(TrackerActivity.this, "Trip saved!", Toast.LENGTH_SHORT).show();
            //getRecentlyInsertedID(tripToInsert);
           // insertMapPointsForTrip(4);
        } else {
            Toast.makeText(TrackerActivity.this, "Wait for established GPS connection!", Toast.LENGTH_LONG).show();
        }
    }

    private void insertMapPointsForTrip(int tripID) {
        MapPointMapper mapPointMapper = new MapPointMapper(tripID);
        List<MapPoint> mapPointsDB = mapPointMapper.fromModelMapPointsToDB(locationTracker.getMapPointsModel());
        mapPointsViewModel = ViewModelProviders.of(TrackerActivity.this).get(MapPointsViewModel.class);
        for(int i =0; i < mapPointsDB.size(); i++) {
            mapPointsViewModel.insert(mapPointsDB.get(i));
        }
        locationTracker.clearMapPointsModel();
        Toast.makeText(TrackerActivity.this, "Trip saved", Toast.LENGTH_SHORT).show();
    }

    public void onStopButtonClick(View view) {
        clearTrackingData();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        createSaveToDBRequestDialog();
    }


    private void clearTrackingData() {
        locationTracker.stopTracking();
        locationTracker.setCoordinatesData(0, 0);
        locationTracker.setSpeed(0);
        locationTracker.setLocationSpeed(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == permissionsProcessor.getLocationAllPermissions()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
