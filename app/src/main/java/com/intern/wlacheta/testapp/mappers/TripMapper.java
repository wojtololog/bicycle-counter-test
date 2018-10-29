package com.intern.wlacheta.testapp.mappers;

import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.utils.DateConverter;
import com.intern.wlacheta.testapp.location.model.TripModel;

public class TripMapper {
    public Trip fromModelToDB(TripModel tripModel) {
        if(tripModel == null) {
            return null;
        }
        return new Trip(tripModel.getStartTripTimestamp(),tripModel.getStartTripDate(),tripModel.getEndTripTimestamp());
    }

    public TripModel fromDBToModel(Trip trip) {
        return null;
    }

}
