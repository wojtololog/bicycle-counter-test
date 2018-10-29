package com.intern.wlacheta.testapp.location.model;

import java.util.Date;

public class TripModel {
    private long startTripTimestamp;
    private String startTripDate;
    private long endTripTimestamp;

    public TripModel(long startTripTimestamp, String startTripDate, long endTripTimestamp) {
        this.startTripTimestamp = startTripTimestamp;
        this.startTripDate = startTripDate;
        this.endTripTimestamp = endTripTimestamp;
    }

    public long getStartTripTimestamp() {
        return startTripTimestamp;
    }

    public String getStartTripDate() {
        return startTripDate;
    }

    public long getEndTripTimestamp() {
        return endTripTimestamp;
    }
}
