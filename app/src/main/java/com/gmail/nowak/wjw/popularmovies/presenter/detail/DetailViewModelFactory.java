package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MovieVM movieVM;
    private Application application;
    private int listPosition;
    private int displayedTab;

    public DetailViewModelFactory(Application application, MovieVM movieVM) {
        super();
        this.application = application;
        this.movieVM = movieVM;
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
        if(movieVM==null){
            return (T) new DetailViewModel(application, listPosition, displayedTab);
        }
        return (T) new DetailViewModel(application, movieVM);
    }




}
