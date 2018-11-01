package com.intern.wlacheta.testapp.mappers;

import com.intern.wlacheta.testapp.database.entities.MapPoint;
import com.intern.wlacheta.testapp.location.model.MapPointModel;

import java.util.ArrayList;
import java.util.List;

public class MapPointMapper {
    private long tripID;

    public MapPointMapper(long tripID) {
        this.tripID = tripID;
    }

    public List<MapPoint> fromModelMapPointsToDB(List<MapPointModel> mapPointsModel) {
        List<MapPoint> dbMapPoints = new ArrayList<MapPoint>(mapPointsModel.size());
        for(int i = 0; i < mapPointsModel.size(); i++) {
            dbMapPoints.add(fromModelMapPointToDB(mapPointsModel.get(i)));
        }
        return dbMapPoints;
    }

    private MapPoint fromModelMapPointToDB(MapPointModel mapPointModel) {
        if (mapPointModel != null) {
            return new MapPoint(tripID, mapPointModel.getLongitude(), mapPointModel.getLatitude(), mapPointModel.getTimestamp());
        }
        return null;
    }
}
