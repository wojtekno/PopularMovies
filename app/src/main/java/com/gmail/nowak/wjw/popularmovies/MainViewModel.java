package com.gmail.nowak.wjw.popularmovies;

import android.app.Application;
import android.util.Log;
import android.util.Printer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.repository.MoviesRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MoviesRepository repository;
    private MutableLiveData<Boolean> lastCallStatus;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d("MainViewModel", "Constructor");
        repository = MoviesRepository.getInstance();
        lastCallStatus = new MutableLiveData<>();
        lastCallStatus.setValue(false);
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

    public LiveData<Boolean> getLastCallStatus() {
        return repository.getLastCallStatus();
    }
}
