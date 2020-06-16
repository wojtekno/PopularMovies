package com.gmail.nowak.wjw.popularmovies.domain;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieList;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GetMovieListsUseCase {
    private MoviesRepository moviesRepository;
    private LiveData<List<MovieListItemViewData>> mPopularMovieListLd;
    private LiveData<Integer> mPopularResponseCode;
    private LiveData<List<MovieListItemViewData>> mTopRatedMovieListLd;
    private LiveData<Integer> mTopRatedResponseCode;
    private LiveData<List<MovieListItemViewData>> mFavouriteMovieListLd;

    public GetMovieListsUseCase(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
        LiveData<ApiResponseMovieList> apiResponseMovieListLiveData = moviesRepository.getPopularMoviesResponseLD();
        LiveData<Integer> lRespCode = Transformations.map(apiResponseMovieListLiveData, ApiResponseMovieList::getResponseCode);
        mPopularResponseCode = Transformations.map(lRespCode, getErrMsgFromResponseCode());

        mPopularMovieListLd = Transformations.map(apiResponseMovieListLiveData, getMovieListFromApiResponse());
    }

    @NotNull
    private Function<ApiResponseMovieList, List<MovieListItemViewData>> getMovieListFromApiResponse() {
        return (input) -> {
            List<MovieListItemViewData> list = new ArrayList<>();
            for (ApiMovie r : input.getResults()) {
                MovieListItemViewData movieListItemViewData = new MovieListItemViewData(r.getApiId(), r.getOriginalTitle(), r.getPosterPath());
                list.add(movieListItemViewData);
            }
            return list;
        };
    }

    private Function<Integer, Integer> getErrMsgFromResponseCode() {
        return (code) -> {
            if (code == 200) return null;
            else if (code == ApiResponseMovieList.RESULT_FAILURE)
                return R.string.error_cannot_connect_with_server;
            else return R.string.error_problem_with_response;
        };
    }

    public LiveData<List<MovieListItemViewData>> getPopularMovieList() {
        return mPopularMovieListLd;
    }

    public LiveData<List<MovieListItemViewData>> getTopRatedMovieList() {
//        Timber.d("getTopRatedMovieList");
        if (mTopRatedMovieListLd == null) {
            mTopRatedMovieListLd = Transformations.map(moviesRepository.getTopRatedMoviesResponseLD(), getMovieListFromApiResponse());
        }
        return mTopRatedMovieListLd;
    }

    public LiveData<Integer> getPopularResponseCode() {
        return mPopularResponseCode;
    }

    public LiveData<Integer> getTopRatedResponseCode() {
//        Timber.d("getTopRatedResponseCode");
        if (mTopRatedResponseCode == null) {
            LiveData<Integer> lRespCode = Transformations.map(moviesRepository.getTopRatedMoviesResponseLD(), ApiResponseMovieList::getResponseCode);
            mTopRatedResponseCode = Transformations.map(lRespCode, getErrMsgFromResponseCode());
        }
        return mTopRatedResponseCode;
    }

    public void refreshMovieList(ListTag listTag) {
        moviesRepository.reloadApiResponseMovieList(listTag);
    }

    public LiveData<List<MovieListItemViewData>> getFavouriteMovieList() {
        if (mFavouriteMovieListLd == null) {
            mFavouriteMovieListLd = Transformations.map(moviesRepository.getFavouriteMoviesLD(), (input) -> {
                List<MovieListItemViewData> result = new ArrayList<>();
                for (FavouriteMovie fm : input) {
                    result.add(new MovieListItemViewData(fm.getApiId(), fm.getTitle(), fm.getPosterPath()));
                }
                return result;
            });
        }
        return mFavouriteMovieListLd;
    }
}
