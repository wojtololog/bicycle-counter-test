package com.intern.wlacheta.testapp.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class PermissionsProcessor {
    private final int LOCATION_ALL_PERMISSIONS = 1;
    private final String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private Context context;
    private Activity activity;

    public PermissionsProcessor(Context passedContext, Activity passedActivity) {
        this.context = passedContext;
        this.activity = passedActivity;
    }

    public boolean isPermissionsNotGranted() {
        if (ContextCompat.checkSelfPermission(context, PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(context, "You have already granted geolocation permission!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSIONS[1])) {
            new AlertDialog.Builder(context)
                    .setTitle("Permission needed")
                    .setMessage("This app requires GPS to track your location and to show date and speed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, PERMISSIONS, LOCATION_ALL_PERMISSIONS);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, LOCATION_ALL_PERMISSIONS);
        }
    }

    public int getLocationAllPermissions() {
        return LOCATION_ALL_PERMISSIONS;
    }
}
