package com.intern.wlacheta.testapp.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "map_points", indices = {@Index("tripID")}, foreignKeys = @ForeignKey(entity = Trip.class,
                                                                parentColumns = "id",
                                                                childColumns = "tripID",
                                                                onDelete = CASCADE))
public class MapPoint {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "tripID")
    private long tripID;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public MapPoint(long tripID, double longitude, double latitude, long timestamp) {
        this.tripID = tripID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTripID() {
        return tripID;
    }

    public void setTripID(long tripID) {
        this.tripID = tripID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
