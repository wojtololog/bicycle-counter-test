package com.intern.wlacheta.testapp.parsers;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import com.intern.wlacheta.testapp.activities.adapters.viewmodel.MapPointsViewModel;
import com.intern.wlacheta.testapp.database.entities.MapPoint;
import com.intern.wlacheta.testapp.database.utils.DateConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class GPXgenerator {
    private final long tripID;
    private final String appFolderName = "BicyclesTrips";
    private final String filename;
    private final String fileBeginning = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gpx\n" +
            "  version=\"1.1\"\n" +
            "  creator=\"BicycleCounter\"\n" +
            "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "  xmlns=\"http://www.topografix.com/GPX/1/1\"\n" +
            "  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">\n" +
            " <trk>\n" +
            "  <trkseg>\n";
    private String mapPointLine = "   <trkpt lat=\"%s\" lon=\"%s\"><time>%s</time></trkpt>\n";
    private final String fileEnd = "  </trkseg>\n" +
            " </trk>\n" +
            "</gpx>";

    private FragmentActivity fragmentActivity;
    private MapPointsViewModel mapPointsViewModel;

    public GPXgenerator(long tripID, String filename, FragmentActivity activity) {
        this.tripID = tripID;
        this.filename = filename + ".gpx";
        this.fragmentActivity = activity;
    }

    public String generateFromDB() {
        String externalState = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(externalState)) {
            return "External Storage is not available";
        }
        StringBuilder fileContent = new StringBuilder();
        fileContent.append(fileBeginning)
                   .append(appendMapPoints())
                   .append(fileEnd);
        if(saveFile(fileContent)) {
            return "File saved successfully";
        }
        return "File not saved";
    }

    private boolean saveFile(StringBuilder fileContent) {
        File folderToCreate = new File(Environment.getExternalStorageDirectory(), appFolderName);
        if(!folderToCreate.exists()) {
           boolean isFolderCreated = folderToCreate.mkdirs();
        }

        File fileToSave = new File(folderToCreate, filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileToSave);
            fileOutputStream.write(fileContent.toString().getBytes());
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private StringBuffer appendMapPoints() {
        mapPointsViewModel = ViewModelProviders.of(fragmentActivity).get(MapPointsViewModel.class);
        List<MapPoint> mapPointsToParse = mapPointsViewModel.findMapPointsForSelectedTrip(tripID);
        if(mapPointsToParse != null) {
            StringBuffer fileMiddle = new StringBuffer();
            final DecimalFormat doubleWithDot = new DecimalFormat("###.######", new DecimalFormatSymbols(Locale.US));
            for(int i = 0; i < mapPointsToParse.size(); i++) {
                String dateInGPX = parseTimestampToGPXDate(mapPointsToParse.get(i).getTimestamp());
                fileMiddle.append(String.format(mapPointLine,doubleWithDot.format(mapPointsToParse.get(i).getLatitude()),doubleWithDot.format(mapPointsToParse.get(i).getLongitude()), dateInGPX));
            }
            return fileMiddle;
        }
        return null;
    }

    private String parseTimestampToGPXDate(long timestamp) {
        String dateFormat = DateConverter.fromTimeStampToGPXFormat(timestamp);
        String[] splitDate = dateFormat.split(" ");
        return splitDate[0] + "T" + splitDate[1] + "Z";
    }

}
