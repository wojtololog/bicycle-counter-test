package com.intern.wlacheta.testapp.location.model;

public class MapPointModel {
    private double latitude;
    private double longitude;
    private long timestamp;

    public MapPointModel(double latitude, double longitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
