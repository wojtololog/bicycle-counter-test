package com.intern.wlacheta.testapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.intern.wlacheta.testapp.database.entities.Word;

import java.util.List;

@Dao
public interface IWordDao {
    @Insert
    void insert(Word word);

    @Query("DELETE FROM words")
    void deleteAll();

    @Query("SELECT * FROM words ORDER BY word asc")
    LiveData<List<Word>> getAllWords();
}
