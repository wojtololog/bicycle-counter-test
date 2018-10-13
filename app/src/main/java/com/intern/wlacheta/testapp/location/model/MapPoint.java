package com.intern.wlacheta.testapp.location.model;

public class MapPoint {
    private double latitude;
    private double longitude;

    public MapPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
