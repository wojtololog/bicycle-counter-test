package com.intern.wlacheta.testapp.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.intern.wlacheta.testapp.database.DBManager;
import com.intern.wlacheta.testapp.database.dao.MapPointsDao;
import com.intern.wlacheta.testapp.database.entities.MapPoint;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapPointsRepository {
    private MapPointsDao mapPointsDao;
    private List<MapPoint> mapPointsForSelectedTrip;

    public MapPointsRepository(Application application) {
        DBManager dbManager = DBManager.getDatabase(application);
        mapPointsDao = dbManager.mapPointsDao();
    }

    public List<MapPoint> findMapPointsByTripID(long tripID) {
        try {
            mapPointsForSelectedTrip = new findMapPointsForTripAsyncTask(mapPointsDao).execute(tripID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mapPointsForSelectedTrip;
    }

    public void insert(MapPoint mapPoint) {
        new insertAsyncTask(mapPointsDao).execute(mapPoint);
    }

    private static class findMapPointsForTripAsyncTask extends AsyncTask<Long,Void,List<MapPoint>> {
        private MapPointsDao asyncTaskDao;

        public findMapPointsForTripAsyncTask(MapPointsDao mapPointsDao) {
            this.asyncTaskDao = mapPointsDao;
        }

        @Override
        protected List<MapPoint> doInBackground(final Long... params) {
           return asyncTaskDao.findMapPointsForTrip(params[0]);
        }
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
