package com.intern.wlacheta.testapp.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.intern.wlacheta.testapp.database.DBManager;
import com.intern.wlacheta.testapp.database.dao.MapPointsDao;
import com.intern.wlacheta.testapp.database.entities.MapPoint;

import java.util.List;

public class MapPointsRepository {
    private MapPointsDao mapPointsDao;
    private LiveData<List<MapPoint>> mapPointsForSelectedTrip;

    public MapPointsRepository(Application application) {
        DBManager dbManager = DBManager.getDatabase(application);
        mapPointsDao = dbManager.mapPointsDao();
    }

    public LiveData<List<MapPoint>> findMapPointsByTripID(int tripID) {
        mapPointsForSelectedTrip = mapPointsDao.findMapPointsForTrip(tripID);
        return mapPointsForSelectedTrip;
    }

    public void insert(MapPoint mapPoint) {
        new insertAsyncTask(mapPointsDao).execute(mapPoint);
    }

    private static class insertAsyncTask extends AsyncTask<MapPoint,Void,Void> {
        private MapPointsDao asyncTaskDao;

        public insertAsyncTask(MapPointsDao mapPointsDao) {
            this.asyncTaskDao = mapPointsDao;
        }

        @Override
        protected Void doInBackground(final MapPoint... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
