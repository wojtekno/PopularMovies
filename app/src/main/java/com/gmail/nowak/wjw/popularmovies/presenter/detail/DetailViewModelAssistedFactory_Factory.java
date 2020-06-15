package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import timber.log.Timber;

public class DetailViewModelAssistedFactory_Factory {

    private MoviesRepository moviesRepository;

    public DetailViewModelAssistedFactory_Factory(MoviesRepository moviesRepository) {
        Timber.d("DetailViewModelAssistedFactory_Factory::newInstance");
        this.moviesRepository = moviesRepository;
    }

    public DetailViewModelAssistedFactory create(int listPosition, int displayedTab) {
        Timber.d("DetailViewModelAssistedFactory_Factory::create");
        return new DetailViewModelAssistedFactory(moviesRepository, listPosition, displayedTab);
    }
}
