package com.intern.wlacheta.testapp.location.datamanager;

import com.intern.wlacheta.testapp.location.model.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class SpeedCalculator {
    private List<MapPoint> mapPoints = new ArrayList<MapPoint>();
    private MapPoint newestMapPoint;
    private MapPoint previousMapPoint;

    private double speed;

    private void searchForComputingMapPoints() {
       if(mapPoints.size() > 2) {
           int lastIndex = mapPoints.size() - 1;
           newestMapPoint = mapPoints.get(lastIndex);
           previousMapPoint = mapPoints.get(lastIndex-1);
       }
    }

    private double computeDistanceBetweenTwoPoints() {
        searchForComputingMapPoints();
        double latitudeInRadian = Math.toRadians(newestMapPoint.getLatitude() - previousMapPoint.getLatitude());
        double longitutdeInRadian = Math.toRadians(newestMapPoint.getLongitude() - previousMapPoint.getLongitude());
        double a = Math.sin(latitudeInRadian / 2) * Math.sin(latitudeInRadian / 2) + Math.cos(Math.toRadians(previousMapPoint.getLatitude())) * Math.cos(Math.toRadians(newestMapPoint.getLatitude())) * Math.sin(longitutdeInRadian / 2) * Math.sin(longitutdeInRadian / 2);
        int earthRay = 6371000; //in meters
        return 2 * earthRay * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    private double computeTimeDifferenceBetweenTwoPoints() {
        long newestTime = newestMapPoint.getTimestamp();
        long previousTime = previousMapPoint.getTimestamp();
        long timeDelta = (newestTime - previousTime) / 1000; //in [s]
        return timeDelta;
    }

    public void computeSpeed() {
        double distanceInMeters = computeDistanceBetweenTwoPoints();
        double distanceInKiloMeters = distanceInMeters / 1000;
        double timeInSeconds = computeTimeDifferenceBetweenTwoPoints();
        this.speed = distanceInKiloMeters / timeInSeconds;
    }

    public double getSpeed() {
        return speed;
    }

    public void clearMapPoints() {
        this.mapPoints.clear();
    }

    public List<MapPoint> getMapPoints() {
        return mapPoints;
    }

    public void addToMapPoints(final MapPoint mapPoint) {
        this.mapPoints.add(mapPoint);
    }
}
