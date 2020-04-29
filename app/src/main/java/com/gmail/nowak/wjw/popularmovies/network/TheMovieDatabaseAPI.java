package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.MovieDTO;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TheMovieDatabaseAPI {

    @GET(NetworkUtils.TMD_API_POPULAR_MOVIE_PATH)//+NetworkUtils.ADD_QUERY+NetworkUtils.TMD_API_KEY_QUERY)
    Call<TMDResponse> getPopularMovies();

    @GET(NetworkUtils.TMD_API_TOP_RATED_MOVIE_PATH)//+NetworkUtils.ADD_QUERY+NetworkUtils.TMD_API_KEY_QUERY)
    Call<TMDResponse> getTopRatedMovies();
}
