package com.intern.wlacheta.testapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.intern.wlacheta.testapp.database.entities.MapPoint;

import java.util.List;

@Dao
public interface MapPointsDao {
    @Insert
    void insert(MapPoint mapPoint);

    @Query("DELETE FROM map_points")
    void deleteAll();

    @Delete
    void delete(MapPoint mapPoint);

    @Query("SELECT * FROM map_points WHERE tripID=:tripID")
    LiveData<List<MapPoint>> findMapPointsForTrip(final int tripID);
}
