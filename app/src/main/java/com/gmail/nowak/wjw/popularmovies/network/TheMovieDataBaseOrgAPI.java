package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieList;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseReview;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseVideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheMovieDataBaseOrgAPI {
    @GET("movie/popular")
    Call<ApiResponseMovieList> getPopularMovies();

    @GET("movie/top_rated")
    Call<ApiResponseMovieList> getTopRatedMovies();

    @GET("movie/{apiId}?append_to_response=videos,reviews")
    Call<ApiMovie> getMovieDetailsWithVideosAndReviews(@Path("apiId") int apiId);
}
