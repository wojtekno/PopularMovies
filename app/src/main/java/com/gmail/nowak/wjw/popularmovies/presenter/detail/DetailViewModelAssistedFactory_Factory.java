package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;
import com.gmail.nowak.wjw.popularmovies.domain.AddRemoveFromFavouriteUseCase;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieDetailsUseCaseAssistedFactory;

import timber.log.Timber;

public class DetailViewModelAssistedFactory_Factory {

    private GetMovieDetailsUseCaseAssistedFactory mGetMovieDetailsUseCaseAssistedFactory;
    private AddRemoveFromFavouriteUseCase mAddRemoveFromFavouriteUseCase;

    public DetailViewModelAssistedFactory_Factory(GetMovieDetailsUseCaseAssistedFactory mGetMovieDetailsUseCaseAssistedFactory, AddRemoveFromFavouriteUseCase mAddRemoveFromFavouriteUseCase) {
        this.mGetMovieDetailsUseCaseAssistedFactory = mGetMovieDetailsUseCaseAssistedFactory;
        this.mAddRemoveFromFavouriteUseCase = mAddRemoveFromFavouriteUseCase;
    }

    public DetailViewModelAssistedFactory create(int apiId) {
        Timber.d("DetailViewModelAssistedFactory_Factory::create");
        return new DetailViewModelAssistedFactory(mGetMovieDetailsUseCaseAssistedFactory.create(apiId), mAddRemoveFromFavouriteUseCase);
    }
}
