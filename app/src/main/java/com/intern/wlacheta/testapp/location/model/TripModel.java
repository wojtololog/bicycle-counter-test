package com.intern.wlacheta.testapp.location.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TripModel implements Parcelable {
    private long startTripTimestamp;
    private String startTripDate;
    private long endTripTimestamp;

    public TripModel(long startTripTimestamp, String startTripDate, long endTripTimestamp) {
        this.startTripTimestamp = startTripTimestamp;
        this.startTripDate = startTripDate;
        this.endTripTimestamp = endTripTimestamp;
    }

    protected TripModel(Parcel in) {
        startTripTimestamp = in.readLong();
        startTripDate = in.readString();
        endTripTimestamp = in.readLong();
    }

    public static final Creator<TripModel> CREATOR = new Creator<TripModel>() {
        @Override
        public TripModel createFromParcel(Parcel in) {
            return new TripModel(in);
        }

        @Override
        public TripModel[] newArray(int size) {
            return new TripModel[size];
        }
    };

    public long getStartTripTimestamp() {
        return startTripTimestamp;
    }

    public String getStartTripDate() {
        return startTripDate;
    }

    public long getEndTripTimestamp() {
        return endTripTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTripTimestamp);
        dest.writeString(startTripDate);
        dest.writeLong(endTripTimestamp);
    }
}
