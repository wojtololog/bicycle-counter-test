package com.intern.wlacheta.testapp.activites;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.gesture.PageAdaptor;
import com.intern.wlacheta.testapp.location.LocationTracker;
import com.intern.wlacheta.testapp.permissions.PermissionsProcessor;

public class MainActivity extends AppCompatActivity {
    private final LocationTracker locationTracker = new LocationTracker(this,this);
    private final PermissionsProcessor permissionsProcessor = new PermissionsProcessor(this,this);

    private Button startButton, stopButton;
    private Toolbar toolbar;

    private TabLayout tabLayout;
    private TabItem tabTracker, tabTrips;

    private PageAdaptor pageAdaptor;
    private ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        tabLayout = findViewById(R.id.tabLayout);
        tabTracker = findViewById(R.id.tabTracker);
        tabTrips = findViewById(R.id.tabTrips);
        viewPager = findViewById(R.id.viewPager);

        pageAdaptor = new PageAdaptor(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdaptor);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        if(permissionsProcessor.isPermissionsNotGranted()) {
            permissionsProcessor.requestLocationPermissions();
        }
        stopButton = findViewById(R.id.stopButton);
        stopButton.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(locationTracker.isRequestForLocation()) {
            locationTracker.requestForLocation();
        }
    }
    
    public void onStartButtonClick(View view) {
        if(permissionsProcessor.isPermissionsNotGranted()) {
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
        locationTracker.setCoordinatesData(0,0);
        locationTracker.setSpeed(0);
        locationTracker.setLocationSpeed(0);
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
