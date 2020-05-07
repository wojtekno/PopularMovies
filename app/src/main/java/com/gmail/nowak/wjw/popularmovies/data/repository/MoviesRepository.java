package com.gmail.nowak.wjw.popularmovies.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.AppExecutors;
import com.gmail.nowak.wjw.popularmovies.BuildConfig;
import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.gmail.nowak.wjw.popularmovies.data.db.AppDatabase;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.ApiResponseObject;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDatabaseOrgAPI;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils.POPULARITY_TAG_TITLE;
import static com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils.TOP_RATED_TAG_TITLE;

public class MoviesRepository {

    private static final String LOG_TAG = MoviesRepository.class.getSimpleName();

    Retrofit retrofit;
    Call<ApiResponseObject> call;
    TheMovieDatabaseOrgAPI theMovieDatabaseOrgAPI;
    private static final Object LOCK = new Object();
    private static MoviesRepository sInstance;
    private MutableLiveData<ApiResponseObject> topRatedMoviesResponseLD = new MutableLiveData<>();
    private MutableLiveData<ApiResponseObject> popularMovieResponseLD = new MutableLiveData<>();
    private LiveData<List<FavouriteMovie>> favMoviesData = new MutableLiveData<>();
    private AppDatabase database;

    private static final int POPULAR_MOVIES_TAG = 0;
    private static final int TOP_RATED_MOVIES_TAG = 1;


    public static MoviesRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d("Creating new repository instance");
                sInstance = new MoviesRepository(application);
            }
        }
        Timber.d("Getting the repository instance");
        return sInstance;
    }

    private MoviesRepository(Application application) {
        database = AppDatabase.getInstance(application);

        OkHttpClient okHttpClient = setUpOkHttpClient();

        retrofit = new Retrofit.Builder().baseUrl(NetworkUtils.THE_MOVIE_DATABASE_API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMovieDatabaseOrgAPI = retrofit.create(TheMovieDatabaseOrgAPI.class);
    }

    /**
     * Set up HttpClient intercepting with api key param
     *
     * @return
     */
    private OkHttpClient setUpOkHttpClient() {
        OkHttpClient.Builder httpClientBuilder =
                new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl newUrl = originalHttpUrl.newBuilder()
                        .addQueryParameter(NetworkUtils.API_KEY_PARAM, BuildConfig.TMD_API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(newUrl);

                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);
            }
        });
        return httpClientBuilder.build();
    }

    private void fetchFromTheMovieDatabase(final String category, Integer page) {
        if (TOP_RATED_TAG_TITLE.equals(category)) {
            call = theMovieDatabaseOrgAPI.getTopRatedMovies();
        } else {
            call = theMovieDatabaseOrgAPI.getPopularMovies();
        }

        call.enqueue(new retrofit2.Callback<ApiResponseObject>() {
            @Override
            public void onResponse(Call<ApiResponseObject> call, final retrofit2.Response<ApiResponseObject> response) {
                Timber.d("fetchFromTMD.onResponse");

                List<MovieVM> results = response.body().getResults();
                ApiResponseObject responseObj = null;
                if (results == null) {
                    responseObj = new ApiResponseObject(results, ApiResponseObject.RESULT_FAILURE);
                } else if (results.size() == 0) {
                    responseObj = new ApiResponseObject(results, ApiResponseObject.RESULT_NO_ITEMS);
                } else if (results.size() > 0) {
                    responseObj = new ApiResponseObject(results, ApiResponseObject.RESULT_OK);
                }

                if (TOP_RATED_TAG_TITLE.equals((category))) {
                    topRatedMoviesResponseLD.setValue(responseObj);
                } else {
                    popularMovieResponseLD.setValue(responseObj);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject> call, Throwable t) {
                call.cancel();
                Timber.d("fetchFromTMD.onFailure");
                if (TOP_RATED_TAG_TITLE.equals((category))) {
                    topRatedMoviesResponseLD.setValue(new ApiResponseObject(null, ApiResponseObject.RESULT_FAILURE));
                } else {
                    popularMovieResponseLD.setValue(new ApiResponseObject(null, ApiResponseObject.RESULT_FAILURE));
                }

            }

        });

    }

    //todo handle in viewmodel if list is null, or there is an error
    public LiveData<List<FavouriteMovie>> getFavouriteMoviesLD() {
        if (favMoviesData == null || favMoviesData.getValue() == null) {
//            Timber.d("Actively fetching from DB");
            favMoviesData = database.movieDao().getAllMoviesLIVE();
        } else {
//            Timber.d("Returning listOfMovies ");
        }
        return favMoviesData;
    }

    public void addFavouriteMovie(FavouriteMovie... movies) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            database.movieDao().insertMovie(movies);

        });
    }

    public void removeFavouriteMovie(FavouriteMovie... movies) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            database.movieDao().removeMovie(movies);
        });
    }

    public void removeFavouriteMovieByServerId(int id) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            database.movieDao().removeMovieById(id);
        });
    }


    public LiveData<ApiResponseObject> getPopularMoviesResponseLD() {
        if (popularMovieResponseLD.getValue() == null || popularMovieResponseLD.getValue().getResponseResult() != 100) {
            fetchFromTheMovieDatabase(POPULARITY_TAG_TITLE, null);
        }
        return popularMovieResponseLD;
    }

    public LiveData<ApiResponseObject> getTopRatedMoviesResponseLD() {
        if (topRatedMoviesResponseLD.getValue() == null || topRatedMoviesResponseLD.getValue().getResponseResult() != 100) {
            fetchFromTheMovieDatabase(TOP_RATED_TAG_TITLE, null);
        }
        return topRatedMoviesResponseLD;
    }

    public LiveData<FavouriteMovie> getFavouriteMovieByTmdId(int tmdId) {
        return database.movieDao().selectByTmdId(tmdId);
    }
}
