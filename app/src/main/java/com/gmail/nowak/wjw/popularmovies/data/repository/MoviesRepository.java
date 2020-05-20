package com.gmail.nowak.wjw.popularmovies.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.AppExecutors;
import com.gmail.nowak.wjw.popularmovies.BuildConfig;
import com.gmail.nowak.wjw.popularmovies.data.db.AppDatabase;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieListObject;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseReviewObject;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseVideoObject;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiReview;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiVideo;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDataBaseOrgAPI;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils.POPULARITY_TAG_TITLE;
import static com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils.TOP_RATED_TAG_TITLE;

public class MoviesRepository {

    private static final String LOG_TAG = MoviesRepository.class.getSimpleName();

    Retrofit retrofit;
    TheMovieDataBaseOrgAPI theMovieDatabaseOrgAPI;
    private static final Object LOCK = new Object();
    private static MoviesRepository sInstance;
    private MutableLiveData<ApiResponseMovieListObject> topRatedMoviesResponseLD = new MutableLiveData<>();
    private MutableLiveData<ApiResponseMovieListObject> popularMovieResponseLD = new MutableLiveData<>();
    private LiveData<List<FavouriteMovie>> favMoviesData;// = new MutableLiveData<>();
    private MutableLiveData<ApiMovie> apiMovieDetails = new MutableLiveData<>();
    public AppDatabase database;
    OkHttpClient okHttpClient;

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

        okHttpClient = setUpOkHttpClient();

        retrofit = new Retrofit.Builder().baseUrl(NetworkUtils.THE_MOVIE_DATABASE_API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMovieDatabaseOrgAPI = retrofit.create(TheMovieDataBaseOrgAPI.class);
    }

    //todo Q? move to another class?

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
        okHttpClient.dispatcher().cancelAll();
        Call<ApiResponseMovieListObject> call;
        if (TOP_RATED_TAG_TITLE.equals(category)) {
            call = theMovieDatabaseOrgAPI.getTopRatedMovies();
        } else {
            call = theMovieDatabaseOrgAPI.getPopularMovies();
        }
        Timber.d("calling TMDB");
        call.enqueue(new retrofit2.Callback<ApiResponseMovieListObject>() {
            @Override
            public void onResponse(Call<ApiResponseMovieListObject> call, final retrofit2.Response<ApiResponseMovieListObject> response) {
                Timber.d("fetchFromTMD.onResponse");

                List<ApiMovie> results = response.body().getResults();
                ApiResponseMovieListObject responseObj = null;
                if (results == null) {
                    responseObj = new ApiResponseMovieListObject(results, ApiResponseMovieListObject.RESULT_FAILURE);
                } else if (results.size() == 0) {
                    responseObj = new ApiResponseMovieListObject(results, ApiResponseMovieListObject.RESULT_NO_ITEMS);
                } else if (results.size() > 0) {
                    responseObj = new ApiResponseMovieListObject(results, ApiResponseMovieListObject.RESULT_OK);
                }

                if (TOP_RATED_TAG_TITLE.equals((category))) {
                    topRatedMoviesResponseLD.setValue(responseObj);
                } else {
                    popularMovieResponseLD.setValue(responseObj);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseMovieListObject> call, Throwable t) {
                call.cancel();
                Timber.d("fetchFromTMD.onFailure");
                if (TOP_RATED_TAG_TITLE.equals((category))) {
                    topRatedMoviesResponseLD.setValue(new ApiResponseMovieListObject(null, ApiResponseMovieListObject.RESULT_FAILURE));
                } else {
                    popularMovieResponseLD.setValue(new ApiResponseMovieListObject(null, ApiResponseMovieListObject.RESULT_FAILURE));
                }

            }

        });

    }

    //todo handle in viewmodel if list is null, or there is an error
    public LiveData<List<FavouriteMovie>> getFavouriteMoviesLD() {
        if (favMoviesData == null || favMoviesData.getValue() == null) {
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


    public LiveData<ApiResponseMovieListObject> getPopularMoviesResponseLD() {
        if (popularMovieResponseLD.getValue() == null || popularMovieResponseLD.getValue().getResponseResult() != 100) {
            fetchFromTheMovieDatabase(POPULARITY_TAG_TITLE, null);
        }
        return popularMovieResponseLD;
    }

    public LiveData<ApiResponseMovieListObject> getTopRatedMoviesResponseLD() {
        if (topRatedMoviesResponseLD.getValue() == null || topRatedMoviesResponseLD.getValue().getResponseResult() != 100) {
            fetchFromTheMovieDatabase(TOP_RATED_TAG_TITLE, null);
        }
        return topRatedMoviesResponseLD;
    }

    public LiveData<FavouriteMovie> getFavouriteMovieByTmdId(int tmdId) {
        return database.movieDao().selectByTmdId(tmdId);
    }

    public FavouriteMovie getFavouriteMovieByTmdIdDirctly(int tmdId) {
        final FavouriteMovie[] fm = new FavouriteMovie[1];
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                fm[0] = database.movieDao().selectFMByTmdId(tmdId);
                notify();
            }
        });
        return fm[0];
    }

    public FavouriteMovie getFavouriteMovieByIdDirectly(int dbID) {
        final FavouriteMovie[] fm = new FavouriteMovie[1];
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                fm[0] = database.movieDao().selectFMByTmdId(dbID);
            }
        });
        return fm[0];
    }

    public LiveData<List<ApiReview>> getReviewsByMovieApiId(int apiId) {
        MutableLiveData<List<ApiReview>> list = new MutableLiveData<>();
        Call<ApiResponseReviewObject> call;
        call = theMovieDatabaseOrgAPI.getReviewsForMovie(apiId);
        Timber.d("calling TMDB");
        call.enqueue(new Callback<ApiResponseReviewObject>() {
            @Override
            public void onResponse(Call<ApiResponseReviewObject> call, retrofit2.Response<ApiResponseReviewObject> response) {
                if (response.code() != 200) {
                    Timber.d("responseCode != 200");
                    list.setValue(new ArrayList<>());
                } else {
                    list.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseReviewObject> call, Throwable t) {

            }
        });
        return list;
    }

    public LiveData<List<ApiVideo>> getVideosByMovieApiId(int apiId) {
        //todo add here ?          okHttpClient.dispatcher().cancelAll();
        MutableLiveData<List<ApiVideo>> list = new MutableLiveData<>();
        Call<ApiResponseVideoObject> call;
        call = theMovieDatabaseOrgAPI.getVideosForMovie(apiId);
        Timber.d("calling TMDB");
        call.enqueue(new Callback<ApiResponseVideoObject>() {
            @Override
            public void onResponse(Call<ApiResponseVideoObject> call, retrofit2.Response<ApiResponseVideoObject> response) {
//                list.setValue(response.body().getResults());

                if (response.code() != 200) {
                    Timber.d("responseCode != 200");
                    list.setValue(new ArrayList<>());
                } else {
                    list.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseVideoObject> call, Throwable t) {

            }
        });
        return list;
    }

    public ApiMovie getApiMovieFromList(int listTag, int position) {
        if (0 == listTag) {
            ApiMovie apiMovie = popularMovieResponseLD.getValue().getResults().get(position);
            return apiMovie;
//            return popularMovieResponseLD.getValue().getResults().get(position);
        } else {
            return topRatedMoviesResponseLD.getValue().getResults().get(position);
        }
    }

    public LiveData<ApiMovie> fetchMovieWithDetails(int apiId) {
        //todo call.cancel!!
        okHttpClient.dispatcher().cancelAll();
        Call<ApiMovie> call = theMovieDatabaseOrgAPI.getMovieDetailsWithVideosAndReviews(apiId);

        Timber.d("calling TMDB");
        //todo clear apiMovieDetails
        apiMovieDetails = new MutableLiveData<>();
        call.enqueue(new Callback<ApiMovie>() {
            @Override
            public void onResponse(Call<ApiMovie> call, retrofit2.Response<ApiMovie> response) {
                //todo handle if error response
                Timber.d("Fetching moviedetails response");
                if (response.code() != 200) {
                    apiMovieDetails.setValue(null);
                }

//                List<ApiVideo> videos =
                apiMovieDetails.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ApiMovie> call, Throwable t) {
                Timber.d("Fetching moviedetails failure");
                Timber.e(t.getMessage());
            }
        });
        return apiMovieDetails;
    }
}
