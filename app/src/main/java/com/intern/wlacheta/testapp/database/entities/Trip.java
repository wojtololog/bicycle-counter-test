package com.intern.wlacheta.testapp.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.intern.wlacheta.testapp.database.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "trips")
@TypeConverters(DateConverter.class)
public class Trip {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "start_date_timestamp")
    private long startTripTimestamp;

    @ColumnInfo(name = "start_date")
    private Date startTripDate;

    @ColumnInfo(name = "end_date_timestamp")
    private long endTripTimestamp;

    public Trip(long startTripTimestamp, Date startTripDate, long endTripTimestamp) {
        this.startTripTimestamp = startTripTimestamp;
        this.startTripDate = startTripDate;
        this.endTripTimestamp = endTripTimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public long getStartTripTimestamp() {
        return startTripTimestamp;
    }

    public void setStartTripTimestamp(@NonNull long startTripTimestamp) {
        this.startTripTimestamp = startTripTimestamp;
    }

    @NonNull
    public long getEndTripTimestamp() {
        return endTripTimestamp;
    }

    public void setEndTripTimestamp(@NonNull long endTripTimestamp) {
        this.endTripTimestamp = endTripTimestamp;
    }

    public Date getStartTripDate() {
        return startTripDate;
    }

    public void setStartTripDate(Date startTripDate) {
        this.startTripDate = startTripDate;
    }
}
