package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import androidx.lifecycle.LiveData;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiReview;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiVideo;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.MovieDetailViewData;

import java.util.List;

public class MovieDetailViewDataFactory {

    public MovieDetailViewDataFactory() {

    }

    public static MovieDetailViewData create(FavouriteMovie favouriteMovie, LiveData<List<ApiVideo>> videoLD, LiveData<List<ApiReview>> reviewLD, LiveData<Boolean> isFavourite) {
        return new MovieDetailViewData(favouriteMovie.getTMDId(), favouriteMovie.getTitle(),favouriteMovie.getPosterPath(), reviewLD, videoLD, isFavourite);
    }

    public static MovieDetailViewData create(ApiMovie apiMovie, LiveData<List<ApiVideo>> videoLD, LiveData<List<ApiReview>> reviewLD,  LiveData<Boolean> isFavourite) {
        return new MovieDetailViewData(apiMovie.getApiId(), apiMovie.getPosterPath(), apiMovie.getOriginalTitle(), apiMovie.getTitle(),
                apiMovie.getOverview(), apiMovie.getAverageRating(), apiMovie.getReleaseDate(), apiMovie.getOriginalLanguage(), reviewLD, videoLD, isFavourite);
    }
}
