package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import timber.log.Timber;

public class DetailViewModelAssistedFactory extends ViewModelProvider.NewInstanceFactory {
    private int listPosition;
    private int displayedTab;
    private MoviesRepository moviesRepository;

    public DetailViewModelAssistedFactory(MoviesRepository moviesRepository, int listPosition, int displayedTab) {
        super();
        Timber.d("DetailViewModelFactory::newInstance");
        this.moviesRepository = moviesRepository;
        this.listPosition = listPosition;
        this.displayedTab = displayedTab;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Timber.d("DetailViewModelFactory::create");
        return (T) new DetailViewModel(moviesRepository, listPosition, displayedTab);
    }


}
