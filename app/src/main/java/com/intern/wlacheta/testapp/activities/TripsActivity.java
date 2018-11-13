package com.intern.wlacheta.testapp.activities;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.activities.adapters.TripsListAdapter;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.TripsViewModel;
import com.intern.wlacheta.testapp.activities.fragments.DatePickerFragment;
import com.intern.wlacheta.testapp.activities.fragments.GPXExportDialog;
import com.intern.wlacheta.testapp.database.utils.DateConverter;
import com.intern.wlacheta.testapp.permissions.PermissionsProcessor;

import java.util.Calendar;

public class TripsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, GPXExportDialog.GPXExportDialogListener {
    private TripsViewModel tripsViewModel;
    private TripsListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar tripsToolbar;

    private final PermissionsProcessor permissionsProcessor = new PermissionsProcessor(this, this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        buildRecyclerView();

        tripsToolbar = findViewById(R.id.trips_toolbar);
        setSupportActionBar(tripsToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        adapter.setTrips(tripsViewModel.getAllTrips());
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
                if (permissionsProcessor.isPermissionsNotGranted()) {
                    permissionsProcessor.requestRequiredPermissions();
                } else {
                    long tripIDToExport = adapter.getTripWithPosition(position).getId();
                    openGPXExportDialog(tripIDToExport);
                }
            }

            private void openGPXExportDialog(long tripIDToExport) {
                GPXExportDialog gpxExportDialog = new GPXExportDialog();
                gpxExportDialog.setCancelable(false);

                Bundle dialogArguments = new Bundle();
                dialogArguments.putLong("tripIDToExport",tripIDToExport);
                gpxExportDialog.setArguments(dialogArguments);

                gpxExportDialog.show(getSupportFragmentManager(),"export_dialog");
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

        adapter.setTrips(tripsViewModel.getTripsByDate(searchingDate));
        if(adapter.getTrips().isEmpty()) {
            Toast.makeText(this,"No trips found",Toast.LENGTH_SHORT).show();
        }
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
            case R.id.search_trips:
                showDatePicker();
                return true;
            case R.id.refresh_icon:
                adapter.setTrips(tripsViewModel.getAllTrips());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "Date picker");
    }

    @Override
    public void getSavingFileStatus(String status) {
        Toast.makeText(this,status,Toast.LENGTH_LONG).show();
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
