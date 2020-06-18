package com.gmail.nowak.wjw.popularmovies.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewDataFactory;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

public class GetMovieDetailsUseCase {

    private MoviesRepository mRepository;
    private LiveData<MovieDetailViewData> mMovieLd;

    public GetMovieDetailsUseCase(MoviesRepository mRepository, int apiId) {
        this.mRepository = mRepository;
        LiveData<ApiMovie> lApiMovie = mRepository.fetchMovieWithDetailsFromApi(apiId);
        LiveData<Boolean> isFav = Transformations.map(mRepository.getFavouriteMovieByApiId(apiId), (resp) -> {
            if (resp == null) return false;
            else return true;
        });
        mMovieLd = Transformations.map(lApiMovie, (m) -> MovieDetailViewDataFactory.create(m, isFav));
    }

    public LiveData<MovieDetailViewData> getMovieDetails() {
        return mMovieLd;
    }
}
