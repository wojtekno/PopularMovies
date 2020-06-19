package com.gmail.nowak.wjw.popularmovies.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewDataFactory;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

public class GetMovieDetailsUseCase {

    private MoviesRepository mRepository;
    private LiveData<MovieDetailViewData> mMovieLd;
    private LiveData<Integer> mErrorMessageResId;

    public GetMovieDetailsUseCase(MoviesRepository mRepository, int apiId) {
        this.mRepository = mRepository;
        LiveData<ApiMovie> lApiMovie = mRepository.fetchMovieWithDetailsFromApi(apiId);
        mErrorMessageResId = Transformations.map(lApiMovie, (m) -> {
            if (m.getResponseCode() == 200) return null;
            else if (m.getResponseCode() == ApiMovie.RESULT_FAILURE)
                return R.string.error_cannot_connect_with_server;
            else {
                return R.string.error_problem_with_response;
            }
        });
        LiveData<Boolean> isFav = Transformations.map(mRepository.getFavouriteMovieByApiId(apiId), (resp) -> {
            if (resp == null) return false;
            else return true;
        });
        mMovieLd = Transformations.map(lApiMovie, (m) -> MovieDetailViewDataFactory.create(m, isFav));
    }

    public LiveData<MovieDetailViewData> getMovieDetails() {
        return mMovieLd;
    }

    public LiveData<Integer> getErrorMessageResId() {
        return mErrorMessageResId;
    }
}
