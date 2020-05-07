package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieApiResponseObject;
import com.gmail.nowak.wjw.popularmovies.data.model.ReviewApiResponseObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheMovieDataBaseOrgAPI {
    public static final String TMDB_API_POPULAR_MOVIE_PATH = "movie/popular";
    public static final String TMDB_API_TOP_RATED_MOVIE_PATH = "movie/top_rated";
    String TMDB_API_MOVIE_REVIEWS_PATH = "movie/{apiId}/reviews";

    @GET(TMDB_API_POPULAR_MOVIE_PATH)
    Call<MovieApiResponseObject> getPopularMovies();

    @GET(TMDB_API_TOP_RATED_MOVIE_PATH)
    Call<MovieApiResponseObject> getTopRatedMovies();

    @GET(TMDB_API_MOVIE_REVIEWS_PATH)
    Call<ReviewApiResponseObject> getReviewsForMovie(@Path("apiId") int apiId);
}
