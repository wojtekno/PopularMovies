package com.gmail.nowak.wjw.popularmovies.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.BuildConfig;
import com.gmail.nowak.wjw.popularmovies.MainViewModel;
import com.gmail.nowak.wjw.popularmovies.MovieDTO;
import com.gmail.nowak.wjw.popularmovies.network.TMDResponse;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDatabaseAPI;
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

import static com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils.POPULARITY_TAG_TITLE;
import static com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils.TOP_RATED_TAG_TITLE;

public class MoviesRepository {

    private static final String LOG_TAG = MoviesRepository.class.getSimpleName();

    Retrofit retrofit;
    Call<TMDResponse> call;
    TheMovieDatabaseAPI theMovieDatabaseAPI;
    private static final Object LOCK = new Object();
    private static MoviesRepository sInstance;
    MutableLiveData<List<MovieDTO>> movieData;
    private MainViewModel.OnResponseListener mOnResponseListener;


    public static MoviesRepository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new repository instance");
                sInstance = new MoviesRepository();
//                sInstance = Room.databaseBuilder(context.getApplicationContext(),
//                        AppDatabase.class, AppDatabase.DATABASE_NAME)
//                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the repository instance");
        return sInstance;
    }

    private MoviesRepository() {
        OkHttpClient okHttpClient = setUpOkHttpClient();

        retrofit = new Retrofit.Builder().baseUrl(NetworkUtils.THE_MOVIE_DATABASE_API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMovieDatabaseAPI = retrofit.create(TheMovieDatabaseAPI.class);

        movieData = new MutableLiveData<List<MovieDTO>>();
    }

    /**
     * Set up HttpClient intercepting with api key param
     *
     * @return
     */
    private OkHttpClient setUpOkHttpClient() {
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
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
        return httpClient.build();
    }


    public LiveData<List<MovieDTO>> getPopularMovies() {
       fetchFromTMD(POPULARITY_TAG_TITLE, null);
        return movieData;
    }

    public LiveData<List<MovieDTO>> getTopRatedMovies() {
        fetchFromTMD(TOP_RATED_TAG_TITLE, null);
        return movieData;
    }

    private void fetchFromTMD(String category, Integer page) {
        if (TOP_RATED_TAG_TITLE.equals(category)) {
            call = theMovieDatabaseAPI.getTopRatedMovies();
        } else {
            call = theMovieDatabaseAPI.getPopularMovies();
        }

        call.enqueue(new retrofit2.Callback<TMDResponse>() {
            @Override
            public void onResponse(Call<TMDResponse> call, final retrofit2.Response<TMDResponse> response) {
                Log.d(LOG_TAG, "fetchFromTMD.onResponse");
                movieData.setValue(response.body().getResults());
                mOnResponseListener.onResponse(true, response.body().getResults().size());
            }

            @Override
            public void onFailure(Call<TMDResponse> call, Throwable t) {
                call.cancel();
                mOnResponseListener.onResponse(false, 0);
            }

        });

    }

    public void setOnResponseListener(MainViewModel.OnResponseListener mOnResponseListener) {
        this.mOnResponseListener = mOnResponseListener;
    }
}
