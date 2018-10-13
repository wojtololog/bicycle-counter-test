package com.intern.wlacheta.testapp.location.datamanager;

import com.intern.wlacheta.testapp.location.model.MapPoint;

import java.util.ArrayList;
import java.util.List;


public class SpeedCalculator {
    private MapPoint lastMapPoint;
    private List<MapPoint> mapPoints = new ArrayList<MapPoint>();
    private float speed;

    public void computeDistanceBetweenPoints() {

    }

    public List<MapPoint> getMapPoints() {
        return mapPoints;
    }

    public float getSpeed() {
        return speed;
    }
}
