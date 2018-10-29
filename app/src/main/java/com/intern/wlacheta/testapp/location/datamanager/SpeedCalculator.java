package com.intern.wlacheta.testapp.location.datamanager;

import com.intern.wlacheta.testapp.location.model.MapPointModel;

import java.util.ArrayList;
import java.util.List;

public class SpeedCalculator {
    private List<MapPointModel> mapPointModels = new ArrayList<MapPointModel>();
    private MapPointModel newestMapPointModel;
    private MapPointModel previousMapPointModel;

    private double speed;

    private boolean searchForComputingMapPoints() {
       if(mapPointModels.size() >= 2) {
           int lastIndex = mapPointModels.size() - 1;
           newestMapPointModel = mapPointModels.get(lastIndex);
           previousMapPointModel = mapPointModels.get(lastIndex-1);
           return true;
       }
       return false;
    }

    private double computeDistanceBetweenTwoPoints() {
        if(searchForComputingMapPoints()) {
            //haversine law
            double latitudeInRadian = Math.toRadians(newestMapPointModel.getLatitude() - previousMapPointModel.getLatitude());
            double longitudeInRadian = Math.toRadians(newestMapPointModel.getLongitude() - previousMapPointModel.getLongitude());
            double a = Math.sin(latitudeInRadian / 2) * Math.sin(latitudeInRadian / 2) + Math.cos(Math.toRadians(previousMapPointModel.getLatitude())) * Math.cos(Math.toRadians(newestMapPointModel.getLatitude())) * Math.sin(longitudeInRadian / 2) * Math.sin(longitudeInRadian / 2);
            int earthRay = 6371000; //in meters
            return 2 * earthRay * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            //approximate version (Equirectangular)
            /*int earthRay = 6371; //in km
            double x = Math.toRadians(newestMapPointModel.getLongitude() - previousMapPointModel.getLongitude()) * Math.cos(Math.toRadians((newestMapPointModel.getLatitude() + previousMapPointModel.getLatitude()) / 2));
            double y = Math.toRadians(newestMapPointModel.getLatitude() - previousMapPointModel.getLatitude());
            return (Math.sqrt(x * x + y * y) * earthRay) / 1000;*/
        }
        return 0;
    }

    private double computeTimeDifferenceBetweenTwoPoints() {
        long newestTime = newestMapPointModel.getTimestamp();
        long previousTime = previousMapPointModel.getTimestamp();
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
        this.mapPointModels.clear();
    }

    public List<MapPointModel> getMapPointModels() {
        return mapPointModels;
    }

    public void addToMapPoints(final MapPointModel mapPointModel) {
        this.mapPointModels.add(mapPointModel);
    }
}
