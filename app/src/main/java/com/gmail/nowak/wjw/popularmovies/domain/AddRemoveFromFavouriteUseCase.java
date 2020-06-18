package com.gmail.nowak.wjw.popularmovies.domain;

import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

public class AddRemoveFromFavouriteUseCase {

    MoviesRepository mRepository;

    public AddRemoveFromFavouriteUseCase(MoviesRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void addToFavourites(MovieDetailViewData movie) {
        FavouriteMovie lFavourite = new FavouriteMovie(movie.getApiId(), movie.getOriginalTitle(), movie.getPosterPath());
        mRepository.addFavouriteMovie(lFavourite);
    }

    public void removeFromFavourites(int apiId) {
        mRepository.removeFavouriteMovieByApiId(apiId);
    }

}
