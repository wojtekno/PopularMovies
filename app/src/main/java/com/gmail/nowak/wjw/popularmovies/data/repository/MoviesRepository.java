package com.gmail.nowak.wjw.popularmovies.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.AppExecutors;
import com.gmail.nowak.wjw.popularmovies.data.db.AppDatabase;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieList;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDataBaseOrgAPI;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

public class MoviesRepository {

    private TheMovieDataBaseOrgAPI theMovieDatabaseOrgAPI;
    private MutableLiveData<ApiResponseMovieList> topRatedMoviesResponseLD;
    private MutableLiveData<ApiResponseMovieList> popularMovieResponseLD;
    private LiveData<List<FavouriteMovie>> favMoviesData;
    private MutableLiveData<ApiMovie> apiMovieDetails;
    private AppDatabase database;
    private Call<ApiMovie> movieDetailsCall;

    public MoviesRepository(AppDatabase appDatabase, TheMovieDataBaseOrgAPI theMovieDataBaseOrgAPI) {
        Timber.d("MoviesRepository:newInstance");
        database = appDatabase;
        this.theMovieDatabaseOrgAPI = theMovieDataBaseOrgAPI;
    }


    /**
     * Fetching data - list of movies from theMovieDatabase.org
     *
     * @param category either topRated or popular
     * @param page     unused, in the future implementing the possibility of fetching more than just one page
     */
    private void fetchMovieListFromApi(final ListTag category, Integer page) {
//        Timber.d("fetchMovieListFromApi.start");
        Call<ApiResponseMovieList> movieListCall;
        if (ListTag.TOP_RATED.equals(category)) {
            movieListCall = theMovieDatabaseOrgAPI.getTopRatedMovies();
        } else if (ListTag.POPULAR.equals(category)) {
            movieListCall = theMovieDatabaseOrgAPI.getPopularMovies();
        } else {
            throw new RuntimeException();
        }
//        Timber.d("calling TMDB");
        movieListCall.enqueue(new retrofit2.Callback<ApiResponseMovieList>() {
            @Override
            public void onResponse(Call<ApiResponseMovieList> call, final retrofit2.Response<ApiResponseMovieList> response) {
                Timber.d("fetchMovieListFromApi(%s).onResponse response.code:%d", category, response.code());

                ApiResponseMovieList apiResponseMovieList = response.body();
                if (apiResponseMovieList != null) {
                    apiResponseMovieList.setResponseCode(response.code());
                } else {
                    //create a special object when apiResponseMovieList == null
                    apiResponseMovieList = new ApiResponseMovieList(new ArrayList<>(), response.code());
                }

                if (ListTag.TOP_RATED.equals((category))) {
                    topRatedMoviesResponseLD.setValue(apiResponseMovieList);
                } else if (ListTag.POPULAR.equals(category)) {
                    popularMovieResponseLD.setValue(apiResponseMovieList);
                } else {
                    // TODO: customize
                    throw new RuntimeException();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseMovieList> call, Throwable t) {
                call.cancel();
                Timber.d("fetchMovieListFromApi(%s).onFailure", category);
                //create a special object
                ApiResponseMovieList apiResponseMovieList = new ApiResponseMovieList(new ArrayList<>(), ApiResponseMovieList.RESULT_FAILURE);
                if (ListTag.TOP_RATED.equals((category))) {
                    topRatedMoviesResponseLD.setValue(apiResponseMovieList);
                } else {
                    popularMovieResponseLD.setValue(apiResponseMovieList);
                }

            }

        });

    }

    public LiveData<ApiMovie> fetchMovieWithDetailsFromApi(int apiId) {
        //clear apiMovieDetails
        apiMovieDetails = new MutableLiveData<>();
        if (movieDetailsCall != null) {
            movieDetailsCall.cancel();
        }
        movieDetailsCall = theMovieDatabaseOrgAPI.getMovieDetailsWithVideosAndReviews(apiId);

//        Timber.d("calling TMDB");
        movieDetailsCall.enqueue(new Callback<ApiMovie>() {
            @Override
            public void onResponse(Call<ApiMovie> call, retrofit2.Response<ApiMovie> response) {
//                Timber.d("fetchMovieWithDetailsFromApi onResponse");
                ApiMovie lMovie = response.body();
                if (lMovie == null) {
                    //if ApiMovieResponse is null, create a special object
                    lMovie = new ApiMovie();
                }
                lMovie.setResponseCode(response.code());
                apiMovieDetails.setValue(lMovie);
            }

            @Override
            public void onFailure(Call<ApiMovie> call, Throwable t) {
                Timber.d("fetchMovieWithDetailsFromApi onFailure");
                Timber.e(t.getMessage());
                //create a special object
                ApiMovie lMovie = new ApiMovie(ApiMovie.RESULT_FAILURE);
                apiMovieDetails.setValue(lMovie);
            }
        });
        return apiMovieDetails;
    }

    public void reloadApiResponseMovieList(ListTag value) {
//        Timber.d("reloadList %s", value);
        if (value == ListTag.FAVOURITE) {
            return;
        }
        fetchMovieListFromApi(value, 0);
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMoviesLD() {
        if (favMoviesData == null) {
            Timber.d("Actively fetching from DB");
            favMoviesData = database.movieDao().getAllMoviesLIVE();
        } else {
            Timber.d("Returning listOfMovies ");
        }
        return favMoviesData;
    }

    public void addFavouriteMovie(FavouriteMovie... movies) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            database.movieDao().insertMovie(movies);
        });
    }

    public void removeFavouriteMovieByApiId(int id) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            database.movieDao().removeMovieById(id);
        });
    }

    /**
     * Get popularMoviesList. The result when fetched successfully is cached as field in repository
     *
     * @return
     */
    public LiveData<ApiResponseMovieList> getPopularMoviesResponseLD() {
        if (popularMovieResponseLD == null) {
            popularMovieResponseLD = new MutableLiveData<>();
            fetchMovieListFromApi(ListTag.POPULAR, null);
        }
        return popularMovieResponseLD;
    }

    public LiveData<ApiResponseMovieList> getTopRatedMoviesResponseLD() {
        Timber.d("getTopRatedMoviesResponseLD");
        if (topRatedMoviesResponseLD == null) {
            Timber.d("topRatedMoviesResponseLD::initializing");
            topRatedMoviesResponseLD = new MutableLiveData<>();
            fetchMovieListFromApi(ListTag.TOP_RATED, null);
        }
        return topRatedMoviesResponseLD;
    }

    public LiveData<FavouriteMovie> getFavouriteMovieByApiId(int tmdId) {
        return database.movieDao().selectByTmdId(tmdId);
    }

}
