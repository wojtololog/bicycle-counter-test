package com.intern.wlacheta.testapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.utils.DateConverter;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters(DateConverter.class)
public interface TripDao {
    @Insert
    long insert(Trip trip);

    @Query("DELETE FROM trips")
    void deleteAll();

    @Delete
    void delete(Trip trip);

    @Query("SELECT * FROM trips ORDER BY start_date_timestamp desc")
    List<Trip> getAllTrips();

    @Query("SELECT * FROM trips WHERE start_date=:pickedDate ORDER BY start_date_timestamp desc")
    List<Trip> findTripsByStartDate(final String pickedDate);
}
