package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.domain.AddRemoveFromFavouriteUseCase;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieDetailsUseCase;

public class DetailViewModelAssistedFactory extends ViewModelProvider.NewInstanceFactory {
    private GetMovieDetailsUseCase mGetMovieDetailsUseCase;
    private AddRemoveFromFavouriteUseCase mAddRemoveFromFavouriteUseCase;

    public DetailViewModelAssistedFactory(GetMovieDetailsUseCase mGetMovieDetailsUseCase, AddRemoveFromFavouriteUseCase mAddRemoveFromFavouriteUseCase) {
        this.mGetMovieDetailsUseCase = mGetMovieDetailsUseCase;
        this.mAddRemoveFromFavouriteUseCase = mAddRemoveFromFavouriteUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        Timber.d("DetailViewModelFactory::create");
        return (T) new DetailViewModel(mGetMovieDetailsUseCase, mAddRemoveFromFavouriteUseCase);
    }


}
