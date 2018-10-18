package com.intern.wlacheta.testapp.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.intern.wlacheta.testapp.database.DBManager;
import com.intern.wlacheta.testapp.database.dao.IWordDao;
import com.intern.wlacheta.testapp.database.entities.Word;

import java.util.List;

public class WordRepository {
    private IWordDao wordDao;
    private LiveData<List<Word>> allWords;

    public WordRepository(Application application) {
        DBManager dbManager = DBManager.getDatabase(application);
        wordDao = dbManager.wordDao();
        allWords = wordDao.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        new insertAsyncTask(wordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Word,Void,Void> {
        private IWordDao asyncTaskDao;

        public insertAsyncTask(IWordDao wordDao) {
            this.asyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
