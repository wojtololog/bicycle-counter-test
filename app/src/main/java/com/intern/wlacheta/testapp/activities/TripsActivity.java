package com.intern.wlacheta.testapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.intern.wlacheta.testapp.R;

public class TripsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.trips_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.GPS_trips:
                showTracker();
                return true;
            case R.id.settings_trips:
                showSettings();
                return true;
            case R.id.help_trips:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showHelp() {

    }

    private void showSettings() {

    }

    private void showTracker() {
        Intent intent = new Intent(this, TrackerActivity.class);
        startActivity(intent);
    }
}
