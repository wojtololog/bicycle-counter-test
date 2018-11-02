package com.intern.wlacheta.testapp.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.intern.wlacheta.testapp.R;

public class GPXExportDialog extends AppCompatDialogFragment {
    private EditText filename;
    private long tripID;
    private GPXExportDialogListener gpxExportDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.filenamedialog_layout,null);
        filename = view.findViewById(R.id.filename_editext);
        alertBuilder.setView(view)
                    .setTitle("GPX export")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(getArguments() != null) {
                                tripID = getArguments().getLong("tripIDToExport");
                            }
                        }
                    });


        return alertBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            gpxExportDialogListener = (GPXExportDialogListener) context; //cannot open dialog without implement its interface
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement GPXExportDialogListener");
        }
    }

    public interface GPXExportDialogListener {
        void getSavingFileStatus(String status);
    }
}
