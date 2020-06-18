package com.gmail.nowak.wjw.popularmovies.domain;

import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

public class GetMovieDetailsUseCaseAssistedFactory {

    private MoviesRepository mMoviesRepository;

    public GetMovieDetailsUseCaseAssistedFactory(MoviesRepository mMoviesRepository) {
        this.mMoviesRepository = mMoviesRepository;
    }

    public GetMovieDetailsUseCase create(int apiId) {
        return new GetMovieDetailsUseCase(mMoviesRepository, apiId);
    }
}
