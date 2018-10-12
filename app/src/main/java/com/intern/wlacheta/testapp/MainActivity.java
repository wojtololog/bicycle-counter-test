package com.intern.wlacheta.testapp;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.intern.wlacheta.testapp.permissions.PermissionsProcessor;

public class MainActivity extends AppCompatActivity {
    private final LocationTracker locationTracker = new LocationTracker(this);
    private final PermissionsProcessor permissionsProcessor = new PermissionsProcessor(this,this);

    private Button startButton, stopButton;
    private boolean isStartButtonClicked = false;
    private boolean isStopButtonClicked = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestLocationPermissions();
        }
        stopButton = findViewById(R.id.stopButton);
        stopButton.setEnabled(false);
    }

    public void onStartButtonClick(View view) {
        if(permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestLocationPermissions();
        } else {
            locationTracker.requestForLocation();
            setCoordinatesData(locationTracker.getLatitude(), locationTracker.getLongitude());
        }
    }

    public void onStopButtonClick(View view) {

    }

    private void setCoordinatesData(double latitude, double longitude) {
        String latitudeToString = String.valueOf(latitude);
        String longitudeToString = String.valueOf(longitude);

        TextView latitudeData = findViewById(R.id.latitudeData);
        latitudeData.setText(latitudeToString);
        TextView longitudeData = findViewById(R.id.longitudeData);
        longitudeData.setText(longitudeToString);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == permissionsProcessor.getLocationAllPermissions())  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
