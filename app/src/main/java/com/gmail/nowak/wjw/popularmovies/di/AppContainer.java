package com.gmail.nowak.wjw.popularmovies.di;

import android.app.Application;

import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;
import com.gmail.nowak.wjw.popularmovies.domain.AddRemoveFromFavouriteUseCase;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieDetailsUseCaseAssistedFactory;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieListsCaseUseCaseFactory;
import com.gmail.nowak.wjw.popularmovies.navigation.Nav;
import com.gmail.nowak.wjw.popularmovies.network.HttpClientFactory;
import com.gmail.nowak.wjw.popularmovies.network.NetworkUtils;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDataBaseOrgAPI;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDataBaseOrgAPIwRx;
import com.gmail.nowak.wjw.popularmovies.presenter.detail.DetailViewModelAssistedFactory_Factory;
import com.gmail.nowak.wjw.popularmovies.presenter.list.ListViewModelFactory_Factory;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
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

    //todo move it to ServiceGenerator
    private TheMovieDataBaseOrgAPIwRx theMovieDataBaseOrgAPIwRx = new Retrofit.Builder()
            .baseUrl(NetworkUtils.THE_MOVIE_DATABASE_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(TheMovieDataBaseOrgAPIwRx.class);

    private MoviesRepository moviesRepository;
    public Nav nav = new Nav();

    public AppContainer(Application application) {
        Timber.d("AppContainer::newInstance");
        moviesRepository = new MoviesRepository(new DatabaseModule(application).appDatabase, theMovieDataBaseOrgAPI, theMovieDataBaseOrgAPIwRx);
    }

    private GetMovieListsCaseUseCaseFactory getMovieListsCaseUseCaseAssistedFactory(){
        return new GetMovieListsCaseUseCaseFactory(moviesRepository);
    }

    public ListViewModelFactory_Factory listViewModeFactory_factory() {
        return new ListViewModelFactory_Factory(nav, getMovieListsCaseUseCaseAssistedFactory());
    }

    public DetailViewModelAssistedFactory_Factory detailViewModelAssistedFactory_factory() {
        return new DetailViewModelAssistedFactory_Factory(getMovieDetailsUseCaseAssistedFactory(), addRemoveFromFavouriteUseCase());
    }

    private AddRemoveFromFavouriteUseCase addRemoveFromFavouriteUseCase() {
        return new AddRemoveFromFavouriteUseCase(moviesRepository);
    }

    private GetMovieDetailsUseCaseAssistedFactory getMovieDetailsUseCaseAssistedFactory() {
        return new GetMovieDetailsUseCaseAssistedFactory(moviesRepository);
    }



}
