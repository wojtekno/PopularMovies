package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

public class MovieListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private MoviesRepository moviesRepository;

    public MovieListViewModelFactory(Application application) {
        mApplication = application;
    }
    public MovieListViewModelFactory(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieListViewModel(moviesRepository);
    }
}
