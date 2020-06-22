package com.gmail.nowak.wjw.popularmovies.presenter.list;

import com.gmail.nowak.wjw.popularmovies.navigation.Nav;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieListsCaseUseCaseFactory;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

public class ListViewModelFactory_Factory {

    private Nav mNav;
    private GetMovieListsCaseUseCaseFactory mGetMovieListsCaseUseCaseFactory;

    public ListViewModelFactory_Factory(Nav nav, GetMovieListsCaseUseCaseFactory getMovieListsCaseUseCaseFactory) {
        this.mNav = nav;
        this.mGetMovieListsCaseUseCaseFactory = getMovieListsCaseUseCaseFactory;
    }

    public ListViewModelFactory create(ListTag listTag) {
        return new ListViewModelFactory(mGetMovieListsCaseUseCaseFactory.create(listTag), mNav);
    }
}
