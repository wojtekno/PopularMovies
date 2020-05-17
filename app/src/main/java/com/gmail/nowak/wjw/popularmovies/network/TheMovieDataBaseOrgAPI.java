package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieObject;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseReviewObject;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseVideoObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheMovieDataBaseOrgAPI {
    String TMDB_API_POPULAR_MOVIE_PATH = "movie/popular";
    String TMDB_API_TOP_RATED_MOVIE_PATH = "movie/top_rated";
    String TMDB_API_MOVIE_REVIEWS_PATH = "movie/{apiId}/reviews";
    String TMDB_API_MOVIE_VIDEOS_PATH = "movie/{apiId}/videos";
    String TMDB_API_MOVIE_DETAILS_W_REVIEWS_AND_VIDEOS = "movie/{apiId}?append_to_response=videos,reviews";

    @GET(TMDB_API_POPULAR_MOVIE_PATH)
    Call<ApiResponseMovieObject> getPopularMovies();

    @GET(TMDB_API_TOP_RATED_MOVIE_PATH)
    Call<ApiResponseMovieObject> getTopRatedMovies();

    @GET(TMDB_API_MOVIE_REVIEWS_PATH)
    Call<ApiResponseReviewObject> getReviewsForMovie(@Path("apiId") int apiId);

    @GET(TMDB_API_MOVIE_VIDEOS_PATH)
    Call<ApiResponseVideoObject> getVideosForMovie(@Path("apiId") int apiId);

    @GET(TMDB_API_MOVIE_DETAILS_W_REVIEWS_AND_VIDEOS)
    Call<ApiResponseMovieObject> getMovieDetailsWithVideosAndReviews(@Path("apiId") int apiId);
}
