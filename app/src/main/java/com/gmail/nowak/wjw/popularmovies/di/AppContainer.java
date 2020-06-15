package com.gmail.nowak.wjw.popularmovies.di;

import android.app.Application;

import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;
import com.gmail.nowak.wjw.popularmovies.network.HttpClientFactory;
import com.gmail.nowak.wjw.popularmovies.network.NetworkUtils;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDataBaseOrgAPI;
import com.gmail.nowak.wjw.popularmovies.presenter.detail.DetailViewModelAssistedFactory_Factory;
import com.gmail.nowak.wjw.popularmovies.presenter.list.MovieListViewModelFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class AppContainer {

    private OkHttpClient okHttpClient = new HttpClientFactory().create();
    private TheMovieDataBaseOrgAPI theMovieDataBaseOrgAPI = new Retrofit.Builder()
            .baseUrl(NetworkUtils.THE_MOVIE_DATABASE_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(TheMovieDataBaseOrgAPI.class);

    private MoviesRepository moviesRepository;

    public AppContainer(Application application) {
        Timber.d("AppContainer::newInstance");
        moviesRepository = new MoviesRepository(new DatabaseModule(application).appDatabase, okHttpClient, theMovieDataBaseOrgAPI);
    }

    public MovieListViewModelFactory listViewModelFactory() {
        return new MovieListViewModelFactory(moviesRepository);
    }

    public DetailViewModelAssistedFactory_Factory detailViewModelAssistedFactory_factory() {
        return new DetailViewModelAssistedFactory_Factory(moviesRepository);
    }


}
