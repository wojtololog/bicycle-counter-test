package com.intern.wlacheta.testapp.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class PermissionsProcessor {
    private final int APP_REQUIRED_PERMISSIONS = 1;
    private final String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context context;
    private Activity activity;

    public PermissionsProcessor(Context passedContext, Activity passedActivity) {
        this.context = passedContext;
        this.activity = passedActivity;
    }

    public boolean isPermissionsNotGranted() {
        if (ContextCompat.checkSelfPermission(context, PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, PERMISSIONS[2]) == PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void requestRequiredPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSIONS[1]) || ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSIONS[2])) {
            new AlertDialog.Builder(context)
                    .setTitle("Permissions needed")
                    .setMessage(" - This app requires GPS to track your location and to show date and speed." +
                            "\n" +
                            " - Write to external storage is required in order to save your trips in GPX format.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, PERMISSIONS, APP_REQUIRED_PERMISSIONS);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, APP_REQUIRED_PERMISSIONS);
        }
    }

    public int getAllPermisionsCode() {
        return APP_REQUIRED_PERMISSIONS;
    }
}
