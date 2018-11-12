package com.intern.wlacheta.testapp.database.utils;

import android.arch.persistence.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat gpxDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat uiDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

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

    public static String fromTimeStampToDBFormat(long dateInUTC) {
        Date date = new Date(dateInUTC);
        String dateToString = dbDateFormat.format(date);
        return  dateToString;
    }

    public static String fromTimeStampToGPXFormat(long dateInUTC) {
        Date date = new Date(dateInUTC);
        String dateToString = gpxDateFormat.format(date);
        return dateToString;
    }

    public static String fromTimeStampToUI(long dateInUTC) {
        Date date = new Date(dateInUTC);
        String dateToString = uiDateFormat.format(date);
        return dateToString;
    }
}
