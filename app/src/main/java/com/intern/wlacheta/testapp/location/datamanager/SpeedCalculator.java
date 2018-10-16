package com.intern.wlacheta.testapp.location.datamanager;

import com.intern.wlacheta.testapp.location.model.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class SpeedCalculator {
    private List<MapPoint> mapPoints = new ArrayList<MapPoint>();
    private MapPoint newestMapPoint;
    private MapPoint previousMapPoint;

    private double speed;

    private boolean searchForComputingMapPoints() {
       if(mapPoints.size() > 2) {
           int lastIndex = mapPoints.size() - 1;
           newestMapPoint = mapPoints.get(lastIndex);
           previousMapPoint = mapPoints.get(lastIndex-1);
           return true;
       }
       return false;
    }

    private double computeDistanceBetweenTwoPoints() {
        if(searchForComputingMapPoints()) {
            //haversine law
            double latitudeInRadian = Math.toRadians(newestMapPoint.getLatitude() - previousMapPoint.getLatitude());
            double longitudeInRadian = Math.toRadians(newestMapPoint.getLongitude() - previousMapPoint.getLongitude());
            double a = Math.sin(latitudeInRadian / 2) * Math.sin(latitudeInRadian / 2) + Math.cos(Math.toRadians(previousMapPoint.getLatitude())) * Math.cos(Math.toRadians(newestMapPoint.getLatitude())) * Math.sin(longitudeInRadian / 2) * Math.sin(longitudeInRadian / 2);
            int earthRay = 6371000; //in meters
            return 2 * earthRay * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            //approximate version (Equirectangular)
            /*int earthRay = 6371; //in km
            double x = Math.toRadians(newestMapPoint.getLongitude() - previousMapPoint.getLongitude()) * Math.cos(Math.toRadians((newestMapPoint.getLatitude() + previousMapPoint.getLatitude()) / 2));
            double y = Math.toRadians(newestMapPoint.getLatitude() - previousMapPoint.getLatitude());
            return (Math.sqrt(x * x + y * y) * earthRay) / 1000;*/
        }
        return 0;
    }

    private double computeTimeDifferenceBetweenTwoPoints() {
        long newestTime = newestMapPoint.getTimestamp();
        long previousTime = previousMapPoint.getTimestamp();
        return (newestTime - previousTime) / 1000; //time delta in [s]
    }

    public void computeSpeed() {
        double distanceInMeters = computeDistanceBetweenTwoPoints();
        if(distanceInMeters != 0) {
            double timeInSeconds = computeTimeDifferenceBetweenTwoPoints();
            this.speed = (distanceInMeters / timeInSeconds) * 3.6; //[km/h]
            if(this.speed < 0.5) {
                this.speed = 0;
            }
        } else {
            this.speed = 0;
        }
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
