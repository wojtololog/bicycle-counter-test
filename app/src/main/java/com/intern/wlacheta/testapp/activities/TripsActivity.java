package com.intern.wlacheta.testapp.activities;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.activities.adapters.TripsListAdapter;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.MapPointsViewModel;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.TripsViewModel;
import com.intern.wlacheta.testapp.activities.fragments.DatePickerFragment;
import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.utils.DateConverter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TripsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TripsViewModel tripsViewModel;
    private TripsListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private MapPointsViewModel mapPointsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        buildRecyclerView();

        tripsViewModel.getAllTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable final List<Trip> trips) {
                adapter.setAllTrips(trips);
            }
        });
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new TripsListAdapter(this);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        tripsViewModel = ViewModelProviders.of(this).get(TripsViewModel.class);

        adapter.setOnItemClickListener(new TripsListAdapter.OnItemClickListener() {
            @Override
            public void onExportIconClick(int position) {
                long tripIDToExport = adapter.getTripWithPosition(position).getId();
                mapPointsViewModel = ViewModelProviders.of(TripsActivity.this).get(MapPointsViewModel.class);
                mapPointsViewModel.findMapPointsForSelectedTrip(tripIDToExport);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String searchingDate = DateConverter.fromTimeStampToDBFormat(calendar.getTimeInMillis());

        tripsViewModel.getTripsByDate(searchingDate).observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable List<Trip> trips) {
                adapter.setAllTrips(trips);
            }
        });
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
            case R.id.search_trips:
                showDatePicker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "Date picker");
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
