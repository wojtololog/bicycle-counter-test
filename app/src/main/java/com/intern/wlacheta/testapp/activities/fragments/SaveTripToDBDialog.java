package com.intern.wlacheta.testapp.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

public class SaveTripToDBDialog extends AppCompatDialogFragment {
    private SaveTripToDBDialogListener saveTripToDBDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        alertBuilder.setTitle("Saving")
                .setMessage("Would you like to save your trip ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveTripToDBDialogListener.getIsSaveToDBRequest(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveTripToDBDialogListener.getIsSaveToDBRequest(false);
                    }
                });

        return alertBuilder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            saveTripToDBDialogListener = (SaveTripToDBDialogListener) context; //cannot open dialog without implement its interface
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SaveTripToDBDialogListener");
        }
    }

    public interface SaveTripToDBDialogListener {
        void getIsSaveToDBRequest(boolean isSaveToDBRequest);
    }
}
