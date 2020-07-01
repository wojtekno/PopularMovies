package com.gmail.nowak.wjw.popularmovies.data.model.api;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDataBaseOrgAPI;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class MovieListDataSource extends PageKeyedDataSource<Integer, ApiMovie> {

    private TheMovieDataBaseOrgAPI mMovieDataBaseOrgAPI;
    private MutableLiveData<ApiResponseMovieList> topRatedMoviesResponseLD;
    private MutableLiveData<ApiResponseMovieList> popularMovieResponseLD;


    public MovieListDataSource(TheMovieDataBaseOrgAPI theMovieDataBaseOrgAPI) {
        mMovieDataBaseOrgAPI = theMovieDataBaseOrgAPI;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ApiMovie> callback) {
        Timber.d("loadInitial");
        LiveData<ApiResponseMovieList> mData = fetchMovieListFromApi(ListTag.POPULAR, 1);
//        List<ApiMovie> vieItems = getMovieListFromApiResponse().apply(mData.getValue());
        callback.onResult(mData.getValue().getResults(), null, 2);

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ApiMovie> callback) {
        Timber.d("loadBefore:%s", params.key);
        LiveData<ApiResponseMovieList> mData = fetchMovieListFromApi(ListTag.POPULAR, params.key);
//        List<ApiMovie> vieItems = getMovieListFromApiResponse().apply(mData.getValue());
        callback.onResult(mData.getValue().getResults(), params.key - 1);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ApiMovie> callback) {
        Timber.d("loadAfter:%s", params.key);
        LiveData<ApiResponseMovieList> mData = fetchMovieListFromApi(ListTag.POPULAR, params.key);
//        List<ApiMovie> vieItems = getMovieListFromApiResponse().apply(mData.getValue());
        callback.onResult(mData.getValue().getResults(), params.key + 1);
    }


    private LiveData<ApiResponseMovieList> fetchMovieListFromApi(final ListTag category, Integer page) {
        Timber.d("fetchMovieListFromApi.start");
        MutableLiveData<ApiResponseMovieList> mResponse = new MutableLiveData<>();
        ApiResponseMovieList lResp = null;
        Call<ApiResponseMovieList> movieListCall;
        if (ListTag.TOP_RATED.equals(category)) {
            movieListCall = mMovieDataBaseOrgAPI.getTopRatedMovies();
        } else if (ListTag.POPULAR.equals(category)) {
            movieListCall = mMovieDataBaseOrgAPI.getPopularMoviesByPage(page);
        } else {
            throw new RuntimeException();
        }
//        Timber.d("calling TMDB");

        try {
            Response<ApiResponseMovieList> response = movieListCall.execute();
            lResp = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        movieListCall.enqueue(new retrofit2.Callback<ApiResponseMovieList>() {
//            @Override
//            public void onResponse(Call<ApiResponseMovieList> call, final retrofit2.Response<ApiResponseMovieList> response) {
//                Timber.d("fetchMovieListFromApi(%s).onResponse response.code:%d", category, response.code());
//
//                ApiResponseMovieList apiResponseMovieList = response.body();
//                if (apiResponseMovieList != null) {
//                    apiResponseMovieList.setResponseCode(response.code());
//                } else {
//                    //create a special object when apiResponseMovieList == null
//                    apiResponseMovieList = new ApiResponseMovieList(new ArrayList<>(), response.code());
//                }
//
//                mResponse.setValue(apiResponseMovieList);
//
////                if (ListTag.TOP_RATED.equals((category))) {
////                    topRatedMoviesResponseLD.setValue(apiResponseMovieList);
////                } else if (ListTag.POPULAR.equals(category)) {
////                    popularMovieResponseLD.setValue(apiResponseMovieList);
////                } else {
////                    throw new UnsupportedOperationException();
////                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponseMovieList> call, Throwable t) {
//                call.cancel();
//                Timber.d("fetchMovieListFromApi(%s).onFailure", category);
//                //create a special object
//                ApiResponseMovieList apiResponseMovieList = new ApiResponseMovieList(new ArrayList<>(), ApiResponseMovieList.RESULT_FAILURE);
//                mResponse.setValue(apiResponseMovieList);
//
////                if (ListTag.TOP_RATED.equals((category))) {
////                    topRatedMoviesResponseLD.setValue(apiResponseMovieList);
////                } else {
////                    popularMovieResponseLD.setValue(apiResponseMovieList);
////                }
//
//            }
//
//        });

        return new MutableLiveData<>(lResp);

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
}
