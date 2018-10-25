package com.intern.wlacheta.testapp.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.intern.wlacheta.testapp.database.DBManager;
import com.intern.wlacheta.testapp.database.dao.TripDao;
import com.intern.wlacheta.testapp.database.entities.Trip;

import java.util.Date;
import java.util.List;

public class TripRepository {
    private TripDao tripDao;
    private LiveData<List<Trip>> allTrips;
    private LiveData<List<Trip>> tripsByDate;

    public TripRepository(Application application) {
        DBManager dbManager = DBManager.getDatabase(application);
        tripDao = dbManager.tripDao();
    }

    public LiveData<List<Trip>> getAllTrips() {
        allTrips = tripDao.getAllTrips();
        return allTrips;
    }

    public LiveData<List<Trip>> getTripsByStartDate(Date pickedDate) {
        tripsByDate = tripDao.findTripsByStartDate(pickedDate);
        return tripsByDate;
    }

    public void insert(Trip trip) {
        new insertAsyncTask(tripDao).execute(trip);
    }

    private static class insertAsyncTask extends AsyncTask<Trip,Void,Void> {
        private TripDao asyncTaskDao;

        public insertAsyncTask(TripDao tripDao) {
            this.asyncTaskDao = tripDao;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
