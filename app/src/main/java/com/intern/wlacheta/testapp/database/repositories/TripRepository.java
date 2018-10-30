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
    private LiveData<List<Trip>> tripByStartDateTimestamp;

    public TripRepository(Application application) {
        DBManager dbManager = DBManager.getDatabase(application);
        tripDao = dbManager.tripDao();
    }

    public LiveData<List<Trip>> getAllTrips() {
        allTrips = tripDao.getAllTrips();
        return allTrips;
    }

    public LiveData<List<Trip>> getTripsByStartDate(String pickedDate) {
        tripsByDate = tripDao.findTripsByStartDate(pickedDate);
        return tripsByDate;
    }

    public LiveData<List<Trip>> getTripByStartDateTimestamp(long startDateTimestamp) {
        tripByStartDateTimestamp = tripDao.findTripByStartDateTimestamp(startDateTimestamp);
        return tripByStartDateTimestamp;
    }

    public long insert(Trip trip) {
        new insertAsyncTask(tripDao).execute(trip);
        return insertAsyncTask.getTripID();
    }

    public void delete(Trip trip) {
        new deleteAsyncTask(tripDao).execute(trip);
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
        private static long tripID;

        public insertAsyncTask(TripDao tripDao) {
            this.asyncTaskDao = tripDao;
        }

        @Override
        protected Long doInBackground(final Trip... params) {
           return asyncTaskDao.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            insertAsyncTask.tripID = aLong;
        }

        public static long getTripID() {
            return tripID;
        }
    }
}
