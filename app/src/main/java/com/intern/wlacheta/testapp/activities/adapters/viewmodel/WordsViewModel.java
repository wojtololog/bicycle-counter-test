package com.intern.wlacheta.testapp.activities.adapters.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.intern.wlacheta.testapp.database.entities.Word;
import com.intern.wlacheta.testapp.database.repositories.WordRepository;

import java.util.List;

public class WordsViewModel extends AndroidViewModel {
    private WordRepository wordRepository;
    private LiveData<List<Word>> allWords;

    public WordsViewModel(Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<List<Word>> getAllWords() {
        allWords = wordRepository.getAllWords();
        return allWords;
    }

    public void insert(Word word) {
        wordRepository.insert(word);
    }
}
