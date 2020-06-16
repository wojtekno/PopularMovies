package com.gmail.nowak.wjw.popularmovies.presenter.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.domain.GetMovieListsUseCase;

public class ListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private GetMovieListsUseCase getMovieListsUseCase;

    public ListViewModelFactory(GetMovieListsUseCase useCase) {
        getMovieListsUseCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListViewModel(getMovieListsUseCase);
    }
}
