package com.intern.wlacheta.testapp.database.utils;

import android.arch.persistence.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String fromTimeStampToString(long dateInUTC) {
        Date date = new Date(dateInUTC);
        String dateToString = simpleDateFormat.format(date);
        return  dateToString;
    }
}
