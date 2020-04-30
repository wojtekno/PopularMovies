package com.gmail.nowak.wjw.popularmovies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gmail.nowak.wjw.popularmovies.repository.MoviesRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    MoviesRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d("MainViewModel", "Constructor");
        repository = MoviesRepository.getInstance();
    }

    public LiveData<List<MovieDTO>> getMoviesData() {
        return repository.getMoviesData();
    }

    public void loadPopularMovies() {
        repository.getPopularMovies();
    }

    public void loadTopRatedMovies() {
        repository.loadTopRatedMovies();
    }

}
