package com.intern.wlacheta.testapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.intern.wlacheta.testapp.database.dao.MapPointsDao;
import com.intern.wlacheta.testapp.database.dao.TripDao;
import com.intern.wlacheta.testapp.database.entities.MapPoint;
import com.intern.wlacheta.testapp.database.entities.Trip;

@Database(entities = {Trip.class, MapPoint.class}, version = 4)
public abstract class DBManager extends RoomDatabase {
    public abstract TripDao tripDao();
    public abstract MapPointsDao mapPointsDao();

    private static volatile DBManager DBInstance;

    public static DBManager getDatabase(final Context context) {
        if(DBInstance == null) {
            synchronized (DBManager.class) {
                if(DBInstance == null) {
                    //create DB here
                    DBInstance = Room.databaseBuilder(context.getApplicationContext(), DBManager.class,"app_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return  DBInstance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(DBInstance).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TripDao mDao;

        PopulateDbAsync(DBManager db) {
            mDao = db.tripDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.deleteAll();
            long initialTimestamp = 1540665497;
            long endTimestamp = 1540666497;
            Trip trip = new Trip(initialTimestamp,"26-10-2018", endTimestamp);
            mDao.insert(trip);
            trip = new Trip(initialTimestamp + 1,"26-10-2018", endTimestamp + 15);
            mDao.insert(trip);
            trip = new Trip(initialTimestamp + 231,"27-10-2018", endTimestamp + 1215);
            mDao.insert(trip);
            trip = new Trip(initialTimestamp + 1231,"28-10-2018", endTimestamp + 3215);
            mDao.insert(trip);
            trip = new Trip(initialTimestamp + 3456,"28-10-2018", endTimestamp + 54354);
            mDao.insert(trip);
            trip = new Trip(initialTimestamp + 123456,"28-10-2018", endTimestamp + 54354);
            mDao.insert(trip);
            trip = new Trip(initialTimestamp + 34536,"29-10-2018", endTimestamp + 543524);
            mDao.insert(trip);
            trip = new Trip(initialTimestamp + 54536,"29-10-2018", endTimestamp + 643524);
            mDao.insert(trip);
            trip = new Trip(initialTimestamp + 54536,"25-10-2018", endTimestamp + 643524);
            mDao.insert(trip);
            return null;
        }
    }
}
