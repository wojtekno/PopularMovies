package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;

    public MovieListViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieListViewModel(mApplication);
    }
}
