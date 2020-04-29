package com.gmail.nowak.wjw.popularmovies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.network.TMDResponse;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDatabaseAPI;
import com.gmail.nowak.wjw.popularmovies.repository.MoviesRepository;
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

import static com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils.TOP_RATED_TAG_TITLE;

public class MainViewModel extends AndroidViewModel {

    Retrofit retrofit;
    Call<TMDResponse> call;
    TheMovieDatabaseAPI theMovieDatabaseAPI;
    private MutableLiveData<List<MovieDTO>> moviesData;
    MoviesRepository repository;


    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    private OnResponseListener onResponseListener;


    public void setMoviesData(MutableLiveData<List<MovieDTO>> moviesData) {
        this.moviesData = moviesData;
    }

    public MainViewModel(@NonNull Application application, OnResponseListener onResponseListener) {
        super(application);
        Log.d("MainViewModel", "Constructor");
        repository = MoviesRepository.getInstance();
        repository.setOnResponseListener(onResponseListener);

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(NetworkUtils.API_KEY_PARAM, BuildConfig.TMD_API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder().baseUrl(NetworkUtils.THE_MOVIE_DATABASE_API_BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMovieDatabaseAPI = retrofit.create(TheMovieDatabaseAPI.class);
    }

    public LiveData<List<MovieDTO>> getPopularMovies(){
        return repository.getPopularMovies();
    }

    public LiveData<List<MovieDTO>> getTopRatedMovies(){
        return repository.getTopRatedMovies();
    }


    //TODO make thi work and figure out how to update UI
    private void fetchDataWithRetrofit(final String sortByTag, Integer page) {
        Log.d("MainViewMOdel", "fetchDatawithRetrofit");
        final int[] mCounter = {0};
        if (page != null) {
            mCounter[0] = page;
        }


        if (TOP_RATED_TAG_TITLE.equals(sortByTag)) {
            call = theMovieDatabaseAPI.getTopRatedMovies();
        } else {
            call = theMovieDatabaseAPI.getPopularMovies();
        }

        call.enqueue(new retrofit2.Callback<TMDResponse>() {
            @Override
            public void onResponse(Call<TMDResponse> call, final retrofit2.Response<TMDResponse> response) {

                moviesData.setValue(response.body().getResults());
                onResponseListener.onResponse(true, response.body().getResults().size());
//                if (mCounter[0] == 0) {
//                    movieAdapter.clearMoviesData();
//                    updateSortByTitleTV();
//                    binding.countTV.setText("");
//                }
//
//                movieAdapter.setMoviesData(response.body().getResults());
//                movieAdapter.notifyDataSetChanged();
//                updateUIOnResponse();
//                mCounter[0]++;
//                binding.countTV.append(String.format("(%d) ", movieAdapter.getItemCount()));
//                if (mCounter[0] < MAX_PAGES_TO_FETCH) {
//                    fetchDataWithRetrofit(sortByTag, mCounter[0]);
//                } else {
//                    isFetchingData = false;
//                }
            }

            @Override
            public void onFailure(Call<TMDResponse> call, Throwable t) {
//                call.cancel();
                onResponseListener.onResponse(false, 0);

//                if (call.isCanceled()) {
//                    Toast.makeText(MainActivity.this, "Request cancelled", Toast.LENGTH_SHORT).show();
//                }
//
//                updateUIOnFailure();
//                isFetchingData = false;
            }
        });
    }

    //TODO implement this
    public LiveData<List<MovieDTO>> getMoviesData() {
        if (moviesData == null) {
            Log.d("MainViewModel", "getMoviesData null");
            moviesData = new MutableLiveData<List<MovieDTO>>();
            fetchDataWithRetrofit(null, null);
        }

        return moviesData;
    }

    public interface OnResponseListener{
        void onResponse(boolean isResponse, int size);
    }
}
