package com.gmail.nowak.wjw.popularmovies.domain;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

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

    private MoviesRepository mMoviesRepository;
    private ListTag mListTag;
    private LiveData<List<MovieListItemViewData>> mMovieList;
    private LiveData<Integer> mErrMsgResId;
    public LiveData<PagedList<MovieListItemViewData>> pagedItems;


    public GetMovieListsUseCase(MoviesRepository moviesRepository, ListTag listTag) {
        mMoviesRepository = moviesRepository;
        mListTag = listTag;
        LiveData<ApiResponseMovieList> lApiResponseMovieList;
        DataSource.Factory<Integer, MovieListItemViewData> factory = moviesRepository.movieListDataSourceFactory.map(apiMovie ->
                new MovieListItemViewData(apiMovie.getApiId(), apiMovie.getOriginalTitle(), apiMovie.getPosterPath()));
        pagedItems = new LivePagedListBuilder(factory, 20).build();

//        switch (listTag) {
//            case POPULAR:
//                lApiResponseMovieList = moviesRepository.getPopularMoviesResponseLD();
//                break;
//            case TOP_RATED:
//                lApiResponseMovieList = moviesRepository.getTopRatedMoviesResponseLD();
//                break;
//            case FAVOURITE:
//                lApiResponseMovieList = null;
//                break;
//            default:
//                throw new UnsupportedOperationException();
//        }

//        if (lApiResponseMovieList == null) {
//            mErrMsgResId = new MutableLiveData<>(null);
//            mMovieList = Transformations.map(moviesRepository.getFavouriteMoviesLD(), (favourites) -> {
//                List<MovieListItemViewData> result = new ArrayList<>();
//                for (FavouriteMovie fm : favourites) {
//                    result.add(new MovieListItemViewData(fm.getApiId(), fm.getTitle(), fm.getPosterPath()));
//                }
//                return result;
//            });
//        } else {
//            LiveData<Integer> lRespCode = Transformations.map(lApiResponseMovieList, ApiResponseMovieList::getResponseCode);
//            mErrMsgResId = Transformations.map(lRespCode, getErrMsgFromResponseCode());
//            mMovieList = Transformations.map(lApiResponseMovieList, getMovieListFromApiResponse());
//        }

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


    public LiveData<List<MovieListItemViewData>> getMovieList() {
        return mMovieList;
    }

    public LiveData<Integer> getErrorMessageResId() {
        return mErrMsgResId;
    }

    public boolean refreshList() {
        if (mListTag == ListTag.FAVOURITE) return false;

        mMoviesRepository.reloadApiResponseMovieList(mListTag);
        return true;
    }

}
