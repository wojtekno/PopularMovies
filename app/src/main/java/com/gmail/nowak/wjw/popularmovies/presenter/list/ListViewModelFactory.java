package com.gmail.nowak.wjw.popularmovies.presenter.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.navigation.Nav;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieListsUseCase;

public class ListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private GetMovieListsUseCase getMovieListsUseCase;
    private Nav mNav;

    public ListViewModelFactory(GetMovieListsUseCase getMovieListsUseCase, Nav nav) {
        this.getMovieListsUseCase = getMovieListsUseCase;
        this.mNav = nav;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListViewModel(getMovieListsUseCase, mNav);
    }
}
