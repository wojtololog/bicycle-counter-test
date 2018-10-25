package com.intern.wlacheta.testapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.activities.adapters.WordListAdapter;
import com.intern.wlacheta.testapp.database.entities.Word;
import com.intern.wlacheta.testapp.activities.adapters.viewmodel.WordsViewModel;

import java.util.List;

public class TripsActivity extends AppCompatActivity {
    private WordsViewModel wordsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wordsViewModel = ViewModelProviders.of(this).get(WordsViewModel.class);
        wordsViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                adapter.setWords(words);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.trips_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.GPS_trips:
                showTracker();
                return true;
            case R.id.settings_trips:
                showSettings();
                return true;
            case R.id.help_trips:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showHelp() {

    }

    private void showSettings() {

    }

    private void showTracker() {
        Intent intent = new Intent(this, TrackerActivity.class);
        startActivity(intent);
    }
}
