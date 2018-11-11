package com.intern.wlacheta.testapp.location.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MapPointModel implements Parcelable {
    private double latitude;
    private double longitude;
    private long timestamp;
    private float locationSpeed;
    private double computedSpeed;

    public MapPointModel(double latitude, double longitude, long timestamp, float locationSpeed, double computedSpeed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.locationSpeed = locationSpeed;
        this.computedSpeed = computedSpeed;
    }

    protected MapPointModel(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        timestamp = in.readLong();
        locationSpeed = in.readFloat();
        computedSpeed = in.readDouble();
    }

    public static final Creator<MapPointModel> CREATOR = new Creator<MapPointModel>() {
        @Override
        public MapPointModel createFromParcel(Parcel in) {
            return new MapPointModel(in);
        }

        @Override
        public MapPointModel[] newArray(int size) {
            return new MapPointModel[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getLocationSpeed() {
        return locationSpeed;
    }

    public double getComputedSpeed() {
        return computedSpeed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(timestamp);
        dest.writeFloat(locationSpeed);
        dest.writeDouble(computedSpeed);
    }
}
