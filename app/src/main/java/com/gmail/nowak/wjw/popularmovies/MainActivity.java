package com.gmail.nowak.wjw.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;

public class MainActivity extends AppCompatActivity {

    MovieAdapter movieAdapter;
    String[] mockupDataSet = {"Shrek", "Matrix", "Tangled", "Frozen", "Frozen II", "Ice Age", "The Greatest Showmen", "Avatar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mockupDataSet

        RecyclerView recyclerView = findViewById(R.id.rv_movies);

        movieAdapter = new MovieAdapter(mockupDataSet);
        recyclerView.setAdapter(movieAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}
