package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MovieVM movieVM;
    private Application application;

    public DetailViewModelFactory(Application application, MovieVM movieVM) {
        super();
        this.application = application;
        this.movieVM = movieVM;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(application, movieVM);
    }
}
