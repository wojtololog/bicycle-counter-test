package com.intern.wlacheta.testapp.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.intern.wlacheta.testapp.database.DBManager;
import com.intern.wlacheta.testapp.database.dao.TripDao;
import com.intern.wlacheta.testapp.database.entities.Trip;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TripRepository {
    private TripDao tripDao;
    private List<Trip> allTrips;
    private List<Trip> tripsByDate;

    public TripRepository(Application application) {
        DBManager dbManager = DBManager.getDatabase(application);
        tripDao = dbManager.tripDao();
    }

    public List<Trip> getAllTrips() {
        try {
            allTrips = new findAllTrips(tripDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allTrips;
    }

    public List<Trip> getTripsByStartDate(String pickedDate) {
        try {
            tripsByDate = new findTripsByStartDate(tripDao).execute(pickedDate).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tripsByDate;
    }

    public long insert(Trip trip) {
        long tripID = 0;
        try {
          tripID = new insertAsyncTask(tripDao).execute(trip).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tripID;
    }

    public void delete(Trip trip) {
        new deleteAsyncTask(tripDao).execute(trip);
    }

    private static class findTripsByStartDate extends AsyncTask<String,Void,List<Trip>> {
        private TripDao asyncTaskDao;

        public findTripsByStartDate(TripDao tripDao) {
            this.asyncTaskDao = tripDao;
        }

        @Override
        protected List<Trip> doInBackground(final String... params) {
            return asyncTaskDao.findTripsByStartDate(params[0]);
        }
    }

    private static class findAllTrips extends AsyncTask<Void,Void,List<Trip>> {
        private TripDao asyncTaskDao;

        public findAllTrips(TripDao tripDao) {
            this.asyncTaskDao = tripDao;
        }

        @Override
        protected List<Trip> doInBackground(final Void... params) {
            return asyncTaskDao.getAllTrips();
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Trip,Void,Void> {
        private TripDao asyncTaskDao;

        public deleteAsyncTask(TripDao tripDao) {
            this.asyncTaskDao = tripDao;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Trip,Void,Long> {
        private TripDao asyncTaskDao;

        public insertAsyncTask(TripDao tripDao) {
            this.asyncTaskDao = tripDao;
        }

        @Override
        protected Long doInBackground(final Trip... params) {
           return asyncTaskDao.insert(params[0]);
        }
    }
}
