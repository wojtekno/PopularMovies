package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheMovieDataBaseOrgAPIwRx {
    @GET("movie/popular")
    Observable<ApiResponseMovieList> getPopularMovies();

    @GET("movie/top_rated")
    Observable<ApiResponseMovieList> getTopRatedMovies();

    @GET("movie/{apiId}?append_to_response=videos,reviews")
    Observable<ApiMovie> getMovieDetailsWithVideosAndReviews(@Path("apiId") int apiId);
}
