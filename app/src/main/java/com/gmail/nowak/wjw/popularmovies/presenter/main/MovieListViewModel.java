package com.gmail.nowak.wjw.popularmovies.presenter.main;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieObject;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import timber.log.Timber;

public class MovieListViewModel extends AndroidViewModel {

    private MoviesRepository repository;
    private MutableLiveData<List<MovieListItemViewData>> popularMoviesLD = new MutableLiveData<List<MovieListItemViewData>>();
    private MutableLiveData<Boolean> popularMoviesStatusLD = new MutableLiveData<>();

    private MutableLiveData<List<MovieListItemViewData>> topRatedMoviesLD = new MutableLiveData<List<MovieListItemViewData>>();
    private MutableLiveData<Boolean> topRatedMoviesStatusLD = new MutableLiveData<>();
    private MutableLiveData<List<MovieListItemViewData>> favouriteMoviesLD = new MutableLiveData<>();


    public MovieListViewModel(@NonNull Application application) {
        super(application);
        Log.d("MainViewModel", "Constructor");
        repository = MoviesRepository.getInstance(application);
    }

    public LiveData<List<MovieListItemViewData>> getFavouriteMovies() {
        if (!isListPopulated(favouriteMoviesLD)) {
            favouriteMoviesLD = (MutableLiveData) Transformations.map(repository.getFavouriteMoviesLD(), this::convertToMovieListItem);
        }
        return favouriteMoviesLD;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<MovieListItemViewData> convertToMovieListItem(List<FavouriteMovie> list) {
        if (list == null) {
            return null;
        }
        return list.stream().map(fm -> new MovieListItemViewData(fm.getTMDId(), fm.getTitle(), fm.getPosterPath())).collect(Collectors.toList());
    }

    private void setPopularMoviesLD() {
        Timber.d("Processing PopularLD from repo");
        LiveData<ApiResponseMovieObject> response = repository.getPopularMoviesResponseLD();
        LiveData<List<MovieListItemViewData>> newLiveData = Transformations.map(response, this::transformTMDResponseResults);
        popularMoviesLD = (MutableLiveData) newLiveData;
        LiveData<Boolean> newStatus = Transformations.map(response, this::transformTMDResponseStatus);
        popularMoviesStatusLD = (MutableLiveData) newStatus;
    }

    private void setTopRatedMoviesLD() {
        Timber.d("Processing TopRatedLD from repo");
        LiveData<ApiResponseMovieObject> response = repository.getTopRatedMoviesResponseLD();
        LiveData<List<MovieListItemViewData>> newLiveData = Transformations.map(response, this::transformTMDResponseResults);
        topRatedMoviesLD = (MutableLiveData) newLiveData;
        LiveData<Boolean> newStatus = Transformations.map(response, this::transformTMDResponseStatus);
        topRatedMoviesStatusLD = (MutableLiveData) newStatus;
    }

    public LiveData<List<MovieListItemViewData>> getPopularMoviesLD() {
        Timber.d("getPopularMoviesLD()");
        if (!isStatusTrue(popularMoviesStatusLD) || !isListPopulated(popularMoviesLD)) {
            setPopularMoviesLD();
        }
        Timber.d("Returning LD");
        return popularMoviesLD;
    }

    public LiveData<Boolean> getPopularMoviesStatusLD() {
        return popularMoviesStatusLD;
    }

    public LiveData<List<MovieListItemViewData>> getTopRatedMoviesLD() {
        Timber.d("getTopRatedMoviesLD()");
        if (!isStatusTrue(topRatedMoviesStatusLD) || !isListPopulated(topRatedMoviesLD)) {
            setTopRatedMoviesLD();
        }
        Timber.d("Returning LD");
        return topRatedMoviesLD;
//        return new MutableLiveData<>();
    }

    public LiveData<Boolean> getTopRatedMoviesStatusLD() {
        return topRatedMoviesStatusLD;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<MovieListItemViewData> transformTMDResponseResults(ApiResponseMovieObject input) {
        Timber.d("transformTMDResponseResults()");
        switch (input.getResponseResult()) {
            case 100:
                return input.getResults().stream().map((r) -> new MovieListItemViewData(r.getApiId(), r.getOriginalTitle(), r.getPosterPath())).collect(Collectors.toList());
            case 300:
                return new ArrayList<MovieListItemViewData>();
            default:
                return null;
        }
    }

    private Boolean transformTMDResponseStatus(ApiResponseMovieObject input) {
        if (input.getResponseResult() == 400) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isListPopulated(LiveData<List<MovieListItemViewData>> liveData) {
        if (liveData.getValue() == null || liveData.getValue().size() == 0) {
            return false;
        }
        return true;
    }

    private boolean isStatusTrue(LiveData<Boolean> liveData) {
        if (popularMoviesStatusLD.getValue() == null || popularMoviesStatusLD.getValue().booleanValue() == false) {
            return false;
        }
        return true;
    }

    public List<LiveData> getLiveDataList() {
        List<LiveData> lDList = new ArrayList();
        lDList.add(popularMoviesLD);
        lDList.add(popularMoviesStatusLD);
        lDList.add(topRatedMoviesLD);
        lDList.add(topRatedMoviesStatusLD);
        lDList.add(favouriteMoviesLD);
        return lDList;
    }
}
