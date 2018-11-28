package com.intern.wlacheta.testapp.activities;


import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.MapPointsViewModel;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.TripsViewModel;
import com.intern.wlacheta.testapp.activities.fragments.SaveTripToDBDialog;
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

public class TrackerActivity extends AppCompatActivity implements SaveTripToDBDialog.SaveTripToDBDialogListener {
    private final PermissionsProcessor permissionsProcessor = new PermissionsProcessor(this, this);
    private BroadcastReceiver locationDataReceiver, tripToSaveWithMapPointsReceiver;

    private Button startButton, stopButton;
    private TripsViewModel tripsViewModel;
    private MapPointsViewModel mapPointsViewModel;

    private TripModel tripToSaveModel;
    private List<MapPointModel> mapPointsToSave;
    private MapPointModel actualLocationData;

    private TextView longitudeTextView, latitudeTextView, dateTextView, locationSpeedTextView, computedSpeedTextView;
    private TextView longitudeLabel, latitudeLabel, dateLabel, locationSpeedLabel, computedSpeedLabel;
    private Toolbar trackerToolbar;
    private final DecimalFormat speedFormat = new DecimalFormat("###");

    private boolean isTrackingRequest, isBackButtonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        if (permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestRequiredPermissions();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        trackerToolbar = findViewById(R.id.tracker_toolbar);
        setSupportActionBar(trackerToolbar);
        findViews();
        createReceivers();
        if(isBackButtonPressed = loadFromPreferences()) {
            restoreButtonsStateIfActivityDestroyed();
        } else {
            restoreButtonsState(savedInstanceState);
        }
        restoreTextViewsState(savedInstanceState);
    }

    private void restoreButtonsStateIfActivityDestroyed() {
        if(isTrackingRequest) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

    private boolean loadFromPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        isTrackingRequest = sharedPreferences.getBoolean("back_button_tracking_request",false);
        return sharedPreferences.getBoolean("back_button", false);
    }

    private void restoreTextViewsState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            MapPointModel restoredGpsData = savedInstanceState.getParcelable("gpsDataToRestore");
            if(restoredGpsData != null) {
                setTextViewsLabels();
                setLocationDataUI(restoredGpsData);
            } else {
                setTextViewsLabels();
            }
        }
    }

    private void restoreButtonsState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            isTrackingRequest = savedInstanceState.getBoolean("tracking_state");
            if(isTrackingRequest) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            } else {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        } else {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

    private void createReceivers() {
        if(locationDataReceiver == null) {
            locationDataReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    actualLocationData = (MapPointModel) intent.getExtras().get("currentLocation");
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

    private void setTextViewsLabels() {
        longitudeLabel.setText(R.string.longitudeText);
        latitudeLabel.setText(R.string.latitudeText);
        dateLabel.setText(R.string.dateText);
        locationSpeedLabel.setText(R.string.locationSpeedText);
        computedSpeedLabel.setText(R.string.computeSpeedText);
    }

    private void setLocationDataUI(MapPointModel actualLocationData) {
        if(actualLocationData != null) {
            locationSpeedTextView.setText(speedFormat.format(actualLocationData.getLocationSpeed()));
            computedSpeedTextView.setText(speedFormat.format(actualLocationData.getComputedSpeed()));
            longitudeTextView.setText(String.valueOf(actualLocationData.getLongitude()));
            latitudeTextView.setText(String.valueOf(actualLocationData.getLongitude()));
            dateTextView.setText(DateConverter.fromTimeStampToUI(actualLocationData.getTimestamp()));
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

        longitudeLabel = findViewById(R.id.longitudeLabel);
        latitudeLabel = findViewById(R.id.latitudeLabel);
        dateLabel = findViewById(R.id.dateLabel);
        locationSpeedLabel = findViewById(R.id.locationSpeedLabel);
        computedSpeedLabel = findViewById(R.id.computedSpeedLabel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tracker_menu, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trips_tracker:
                showSavedTrips();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        isBackButtonPressed = true;
        saveStateOnBackButtonPressed(isBackButtonPressed, isTrackingRequest);
        super.onBackPressed();
    }

    private void saveStateOnBackButtonPressed(boolean isBackButtonPressed, boolean isTrackingRequest) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("back_button", isBackButtonPressed);
        editor.putBoolean("back_button_tracking_request", isTrackingRequest);
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("tracking_state", isTrackingRequest);
        outState.putParcelable("gpsDataToRestore", actualLocationData);
    }

    private void showSavedTrips() {
        Intent intent = new Intent(this, TripsActivity.class);
        startActivity(intent);
    }

    public void onStartButtonClick(View view) {
        if (permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestRequiredPermissions();
        } else {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            Intent serviceIntent = new Intent(this, LocationTrackerService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
            isTrackingRequest = true;
        }
    }

    private void createSaveToDBRequestDialog() {
        SaveTripToDBDialog saveTripToDBDialog = new SaveTripToDBDialog();
        saveTripToDBDialog.setCancelable(false);
        saveTripToDBDialog.show(getSupportFragmentManager(),"save_dialog");
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
        isTrackingRequest = false;
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

    @Override
    public void getIsSaveToDBRequest(boolean isSaveToDBRequest) {
        if(isSaveToDBRequest) {
            insertTripToDB(tripToSaveModel);
        } else {
            Toast.makeText(this, "Trip NOT saved", Toast.LENGTH_SHORT).show();
        }
    }
}
