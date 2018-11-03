package com.intern.wlacheta.testapp.activities.adapters.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.intern.wlacheta.testapp.database.entities.MapPoint;
import com.intern.wlacheta.testapp.database.repositories.MapPointsRepository;

import java.util.ArrayList;
import java.util.List;

public class MapPointsViewModel extends AndroidViewModel {
    private MapPointsRepository mapPointsRepository;

    public MapPointsViewModel(Application application) {
        super(application);
        mapPointsRepository = new MapPointsRepository(application);
    }

    public void insert(MapPoint mapPoint) {
        mapPointsRepository.insert(mapPoint);
    }

    public List<MapPoint> findMapPointsForSelectedTrip(long tripID) {
       return mapPointsRepository.findMapPointsByTripID(tripID);
    }
}
