package com.gmail.nowak.wjw.popularmovies.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.AppExecutors;
import com.gmail.nowak.wjw.popularmovies.data.db.AppDatabase;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieList;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseReviewObject;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseVideoObject;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiReview;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiVideo;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDataBaseOrgAPI;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import timber.log.Timber;

public class MoviesRepository {

    //    Retrofit retrofit;
    private TheMovieDataBaseOrgAPI theMovieDatabaseOrgAPI;
    //    private static final Object LOCK = new Object();
//    private static MoviesRepository sInstance;
    private MutableLiveData<ApiResponseMovieList> topRatedMoviesResponseLD = new MutableLiveData<>();
    private MutableLiveData<ApiResponseMovieList> popularMovieResponseLD = new MutableLiveData<>();
    private LiveData<List<FavouriteMovie>> favMoviesData;// = new MutableLiveData<>();
    private MutableLiveData<ApiMovie> apiMovieDetails = new MutableLiveData<>();
    private AppDatabase database;
    private OkHttpClient okHttpClient;

    public MoviesRepository(AppDatabase appDatabase, OkHttpClient okHttpClient, TheMovieDataBaseOrgAPI theMovieDataBaseOrgAPI) {
        Timber.d("MoviesRepository:newInstance");
        database = appDatabase;
        this.okHttpClient = okHttpClient;
        this.theMovieDatabaseOrgAPI = theMovieDataBaseOrgAPI;
        //todo Q? do I pass retrofit, or the TheMovieDataBaseOrgAPI
//        retrofit = retrofit;
//        theMovieDatabaseOrgAPI = retrofit.create(TheMovieDataBaseOrgAPI.class);
    }


    public void reloadApiResponseMovieList(ListTag value) {
        Timber.d("reloadList %s", value);
        if (value == ListTag.FAVOURITE) {
            return;
        }
        fetchMovieListFromApi(value, 0);
    }

    /**
     * Fetching data - list of movies from theMovieDatabase.org
     *
     * @param category either topRated or popular
     * @param page     unused, in the future implementing the possibility of fetching more than just one page
     */
    private void fetchMovieListFromApi(final ListTag category, Integer page) {
        //cancel all previous calls
//        okHttpClient.dispatcher().cancelAll();
        Call<ApiResponseMovieList> call;
        if (ListTag.TOP_RATED.equals(category)) {
            call = theMovieDatabaseOrgAPI.getTopRatedMovies();
        } else if (ListTag.POPULAR.equals(category)) {
            call = theMovieDatabaseOrgAPI.getPopularMovies();
        } else {
            // TODO: 10.06.20 throw exception
            return;
        }
//        Timber.d("calling TMDB");
        call.enqueue(new retrofit2.Callback<ApiResponseMovieList>() {
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
                    // TODO: 10.06.20 throw exception
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
    //todo handle in viewModel if list is null, or there is an error

    public LiveData<ApiMovie> fetchMovieWithDetailsFromApi(int apiId) {
        //todo call.cancel!!
        okHttpClient.dispatcher().cancelAll();
        Call<ApiMovie> call = theMovieDatabaseOrgAPI.getMovieDetailsWithVideosAndReviews(apiId);

//        Timber.d("calling TMDB");
        //todo clear apiMovieDetails
        apiMovieDetails = new MutableLiveData<>();
        call.enqueue(new Callback<ApiMovie>() {
            @Override
            public void onResponse(Call<ApiMovie> call, retrofit2.Response<ApiMovie> response) {
                //todo handle if error response
//                Timber.d("Fetching moviedetails response");
                if (response.code() != 200) {
                    apiMovieDetails.setValue(null);
                }
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

    public void removeFavouriteMovieByApiId(int id) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            database.movieDao().removeMovieById(id);
        });
    }

    public void removeFavouriteMovie(FavouriteMovie... movies) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            database.movieDao().removeMovie(movies);
        });
    }

    /**
     * Get popularMoviesList. The result when fetched successfully is cached as field in repository
     *
     * @return
     */
    public LiveData<ApiResponseMovieList> getPopularMoviesResponseLD() {
        if (popularMovieResponseLD.getValue() == null || popularMovieResponseLD.getValue().getResponseCode() != 100) {
            fetchMovieListFromApi(ListTag.POPULAR, null);
        }
        return popularMovieResponseLD;
    }

    public LiveData<ApiResponseMovieList> getTopRatedMoviesResponseLD() {
        if (topRatedMoviesResponseLD.getValue() == null || topRatedMoviesResponseLD.getValue().getResponseCode() != 100) {
            fetchMovieListFromApi(ListTag.TOP_RATED, null);
        }
        return topRatedMoviesResponseLD;
    }

    public LiveData<FavouriteMovie> getFavouriteMovieByApiId(int tmdId) {
        return database.movieDao().selectByTmdId(tmdId);
    }

    //todo Q? how difficult for me was to fetch the databse objects not as the LiveData - i quit!

    public FavouriteMovie getFavouriteMovieByApiIdDirectly(int tmdId) {
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


//    public static MoviesRepository getInstance(Application application) {
//        if (sInstance == null) {
//            synchronized (LOCK) {
//                Timber.d("Creating new repository instance");
//                sInstance = new MoviesRepository(application);
//            }
//        }
//        Timber.d("Getting the repository instance");
//        return sInstance;
//    }
//
//    private MoviesRepository(Application application) {
//        database = AppDatabase.getInstance(application);
//
//        okHttpClient = setUpOkHttpClient();
//
//        retrofit = new Retrofit.Builder().baseUrl(NetworkUtils.THE_MOVIE_DATABASE_API_BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        theMovieDatabaseOrgAPI = retrofit.create(TheMovieDataBaseOrgAPI.class);
//    }
//
//    //todo Q? move setUpOkHttpClient and all this retrofit preparing from constructor to another class? ie NetworkClient.getTheMovieDatabaseClient()?
//
//    /**
//     * Set up HttpClient intercepting with api key param
//     *
//     * @return
//     */
//    private OkHttpClient setUpOkHttpClient() {
//        OkHttpClient.Builder httpClientBuilder =
//                new OkHttpClient.Builder();
//        httpClientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//
//                HttpUrl originalHttpUrl = original.url();
//
//                HttpUrl newUrl = originalHttpUrl.newBuilder()
//                        .addQueryParameter(NetworkUtils.API_KEY_PARAM, BuildConfig.TMD_API_KEY)
//                        .build();
//
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                        .url(newUrl);
//
//                Request newRequest = requestBuilder.build();
//
//                return chain.proceed(newRequest);
//            }
//        });
//
//        if (BuildConfig.DEBUG) {
//            Timber.d("adding NetworkInterceptor");
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            httpClientBuilder.addNetworkInterceptor(loggingInterceptor);
//        }
//
//
//        return httpClientBuilder.build();
//    }

}
