package com.gmail.nowak.wjw.popularmovies.presenter.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.MovieApiResponseObject;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainViewModel extends AndroidViewModel {

    private MoviesRepository repository;
    private MutableLiveData<List<MovieVM>> popularMoviesLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> popularMoviesStatusLD = new MutableLiveData<>();

    private MutableLiveData<List<MovieVM>> topRatedMoviesLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> topRatedMoviesStatusLD = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d("MainViewModel", "Constructor");
        repository = MoviesRepository.getInstance(application);
    }


    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return repository.getFavouriteMoviesLD();
    }

    private void setPopularMoviesLD() {
        Timber.d("Processing PopularLD from repo");
        LiveData<MovieApiResponseObject> response = repository.getPopularMoviesResponseLD();
        LiveData<List<MovieVM>> newLiveData = Transformations.map(response, this::transformTMDResponseResults);
        popularMoviesLD = (MutableLiveData) newLiveData;
        LiveData<Boolean> newStatus = Transformations.map(response, this::transformTMDResponseStatus);
        popularMoviesStatusLD = (MutableLiveData) newStatus;
    }

    private void setTopRatedMoviesLD() {
        Timber.d("Processing TopRatedLD from repo");
        LiveData<MovieApiResponseObject> response = repository.getTopRatedMoviesResponseLD();
        LiveData<List<MovieVM>> newLiveData = Transformations.map(response, this::transformTMDResponseResults);
        topRatedMoviesLD = (MutableLiveData) newLiveData;
        LiveData<Boolean> newStatus = Transformations.map(response, this::transformTMDResponseStatus);
        topRatedMoviesStatusLD = (MutableLiveData) newStatus;
    }

    public LiveData<List<MovieVM>> getPopularMoviesLD() {
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

    public LiveData<List<MovieVM>> getTopRatedMoviesLD() {
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

    private List<MovieVM> transformTMDResponseResults(MovieApiResponseObject input) {
        switch (input.getResponseResult()) {
            case 100:
                return input.getResults();
            case 300:
                return new ArrayList<MovieVM>();
            default:
                return null;
        }
    }

    private Boolean transformTMDResponseStatus(MovieApiResponseObject input) {
        if (input.getResponseResult() == 400) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isListPopulated(LiveData<List<MovieVM>> liveData) {
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

    public List<LiveData> getLivaDataList() {
        List<LiveData> lDList = new ArrayList();
        lDList.add(popularMoviesLD);
        lDList.add(popularMoviesStatusLD);
        lDList.add(topRatedMoviesLD);
        lDList.add(topRatedMoviesStatusLD);
        return lDList;
    }
}
