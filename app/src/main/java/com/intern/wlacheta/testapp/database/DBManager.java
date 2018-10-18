package com.intern.wlacheta.testapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.intern.wlacheta.testapp.database.dao.IWordDao;
import com.intern.wlacheta.testapp.database.entities.Word;

@Database(entities = {Word.class}, version = 1)
public abstract class DBManager extends RoomDatabase {
    public abstract IWordDao wordDao();

    private static volatile DBManager DBInstance;

    public static DBManager getDatabase(final Context context) {
        if(DBInstance == null) {
            synchronized (DBManager.class) {
                if(DBInstance == null) {
                    //create DB here
                    DBInstance = Room.databaseBuilder(context.getApplicationContext(), DBManager.class,"app_database").build();
                }
            }
        }
        return  DBInstance;
    }
}
