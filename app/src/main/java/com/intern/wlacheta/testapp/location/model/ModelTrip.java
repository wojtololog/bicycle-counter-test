package com.intern.wlacheta.testapp.location.model;

import java.util.Date;

public class ModelTrip {
    private long startTripTimestamp;
    private Date startTripDate;
    private long endTripTimestamp;

    public ModelTrip(long startTripTimestamp, Date startTripDate, long endTripTimestamp) {
        this.startTripTimestamp = startTripTimestamp;
        this.startTripDate = startTripDate;
        this.endTripTimestamp = endTripTimestamp;
    }

    public long getStartTripTimestamp() {
        return startTripTimestamp;
    }

    public Date getStartTripDate() {
        return startTripDate;
    }

    public long getEndTripTimestamp() {
        return endTripTimestamp;
    }
}
