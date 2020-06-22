package com.gmail.nowak.wjw.popularmovies.domain;

import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

public class GetMovieListsCaseUseCaseFactory {

    private MoviesRepository mMoviesRepository;

    public GetMovieListsCaseUseCaseFactory(MoviesRepository moviesRepository) {
        this.mMoviesRepository = moviesRepository;
    }

    public GetMovieListsUseCase create(ListTag listTag) {
        return new GetMovieListsUseCase(mMoviesRepository, listTag);
    }
}
