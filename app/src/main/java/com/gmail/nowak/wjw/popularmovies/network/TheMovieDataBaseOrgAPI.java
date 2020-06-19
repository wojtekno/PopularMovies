package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieList;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseReview;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseVideo;

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
    Call<ApiResponseMovieList> getPopularMovies();

    @GET(TMDB_API_TOP_RATED_MOVIE_PATH)
    Call<ApiResponseMovieList> getTopRatedMovies();

    @GET(TMDB_API_MOVIE_DETAILS_W_REVIEWS_AND_VIDEOS)
    Call<ApiMovie> getMovieDetailsWithVideosAndReviews(@Path("apiId") int apiId);

    @GET(TMDB_API_MOVIE_REVIEWS_PATH)
    Call<ApiResponseReview> getReviewsForMovie(@Path("apiId") int apiId);

    @GET(TMDB_API_MOVIE_VIDEOS_PATH)
    Call<ApiResponseVideo> getVideosForMovie(@Path("apiId") int apiId);
}
