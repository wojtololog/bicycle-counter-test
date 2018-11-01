package com.intern.wlacheta.testapp.activities.adapters.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.repositories.TripRepository;

import java.util.Date;
import java.util.List;

public class TripsViewModel extends AndroidViewModel {
    private TripRepository tripRepository;
    private LiveData<List<Trip>> allTrips;
    private LiveData<List<Trip>> tripsByDate;

    public TripsViewModel(Application application) {
        super(application);
        tripRepository = new TripRepository(application);
    }

    public long insert(Trip trip) {
        return tripRepository.insert(trip);
    }

    public void delete(Trip trip) {
        tripRepository.delete(trip);
    }

    public LiveData<List<Trip>> getAllTrips() {
        allTrips = tripRepository.getAllTrips();
        return allTrips;
    }

    public LiveData<List<Trip>> getTripsByDate(String pickedDate) {
        tripsByDate = tripRepository.getTripsByStartDate(pickedDate);
        return tripsByDate;
    }
}
