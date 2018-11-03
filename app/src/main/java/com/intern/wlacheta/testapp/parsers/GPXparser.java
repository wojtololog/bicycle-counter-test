package com.intern.wlacheta.testapp.parsers;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import com.intern.wlacheta.testapp.activities.adapters.viewmodel.MapPointsViewModel;
import com.intern.wlacheta.testapp.database.entities.MapPoint;

import java.io.File;
import java.util.List;

public class GPXparser {
    private final long tripID;
    private final String filename;
    private final String fileBeginning = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gpx\n" +
            "  version=\"1.1\"\n" +
            "  creator=\"BicycleCounter\"\n" +
            "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "  xmlns=\"http://www.topografix.com/GPX/1/1\"\n" +
            "  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">";
    private String mapPointLine = "<wpt lat=\"%f\" lon=\"%f\">%s</time></wpt>";
    private final String fileEnd = "</gpx>";

    private FragmentActivity fragmentActivity;
    private MapPointsViewModel mapPointsViewModel;


    public GPXparser(long tripID, String filename, FragmentActivity activity) {
        this.tripID = tripID;
        this.filename = filename;
        this.fragmentActivity = activity;
    }

    public String parseFromDB() {
        String externalState = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(externalState)) {
            return "External Storage is not available";
        }

        mapPointsViewModel = ViewModelProviders.of(fragmentActivity).get(MapPointsViewModel.class);
        List<MapPoint> mapPointsToParse = mapPointsViewModel.findMapPointsForSelectedTrip(tripID);
        return "Kurwa";

    }
}
