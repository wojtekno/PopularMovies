package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private ApiMovie apiMovie;
    private Application application;
    private int listPosition;
    private int displayedTab;

    public DetailViewModelFactory(Application application, ApiMovie apiMovie) {
        super();
        this.application = application;
        this.apiMovie = apiMovie;
    }

    public DetailViewModelFactory(Application application, int listPosition, int displayedTab) {
        super();
        this.application = application;
        this.listPosition = listPosition;
        this.displayedTab = displayedTab;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DetailViewModel(application, listPosition, displayedTab);
    }




}
