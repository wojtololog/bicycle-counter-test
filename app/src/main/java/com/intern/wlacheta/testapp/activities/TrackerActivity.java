package com.intern.wlacheta.testapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.intern.wlacheta.testapp.location.LocationTracker;
import com.intern.wlacheta.testapp.permissions.PermissionsProcessor;

public class TrackerActivity extends AppCompatActivity {
    private final LocationTracker locationTracker = new LocationTracker(this, this);
    private final PermissionsProcessor permissionsProcessor = new PermissionsProcessor(this, this);

    private Button startButton, stopButton;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        if (permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestLocationPermissions();
        }
        stopButton = findViewById(R.id.stopButton);
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
            startButton = findViewById(R.id.startButton);
            stopButton = findViewById(R.id.stopButton);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            locationTracker.requestForLocation();
        }
    }

    public void onStopButtonClick(View view) {
        clearTrackingData();

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
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
