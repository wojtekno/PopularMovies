package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieApiResponseObject;
import com.gmail.nowak.wjw.popularmovies.data.model.ReviewApiResponseObject;
import com.gmail.nowak.wjw.popularmovies.data.model.VideoApiResponseObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheMovieDataBaseOrgAPI {
    public static final String TMDB_API_POPULAR_MOVIE_PATH = "movie/popular";
    public static final String TMDB_API_TOP_RATED_MOVIE_PATH = "movie/top_rated";
    String TMDB_API_MOVIE_REVIEWS_PATH = "movie/{apiId}/reviews";
    String TMDB_API_MOVIE_VIDEOS_PATH = "movie/{apiId}/videos";

    @GET(TMDB_API_POPULAR_MOVIE_PATH)
    Call<MovieApiResponseObject> getPopularMovies();

    @GET(TMDB_API_TOP_RATED_MOVIE_PATH)
    Call<MovieApiResponseObject> getTopRatedMovies();

    @GET(TMDB_API_MOVIE_REVIEWS_PATH)
    Call<ReviewApiResponseObject> getReviewsForMovie(@Path("apiId") int apiId);

    @GET(TMDB_API_MOVIE_VIDEOS_PATH)
    Call<VideoApiResponseObject> getVideosForMovie(@Path("apiId") int apiId);
}
