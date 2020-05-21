package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieListObject;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
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
        return list.stream().map(fm -> new MovieListItemViewData(fm.getApiId(), fm.getTitle(), fm.getPosterPath())).collect(Collectors.toList());
    }

    /**
     * Fetches data from repository, transforms it and populates popularMoviesLD and popularMoviesStatusLD
     */
    private void setPopularMoviesLD() {
        Timber.d("Processing PopularLD from repo");
        LiveData<ApiResponseMovieListObject> response = repository.getPopularMoviesResponseLD();
        LiveData<List<MovieListItemViewData>> newLiveData = Transformations.map(response, this::transformTMDResponseResults);
        popularMoviesLD = (MutableLiveData) newLiveData;
        LiveData<Boolean> newStatus = Transformations.map(response, this::transformTMDResponseStatus);
        popularMoviesStatusLD = (MutableLiveData) newStatus;
    }

    /**
     * Fetches data from repository, transforms it and populates topRatedMoviesLD and topRatedMoviesStatusLD
     */
    private void setTopRatedMoviesLD() {
        Timber.d("Processing TopRatedLD from repo");
        LiveData<ApiResponseMovieListObject> response = repository.getTopRatedMoviesResponseLD();
        LiveData<List<MovieListItemViewData>> newLiveData = Transformations.map(response, this::transformTMDResponseResults);
        topRatedMoviesLD = (MutableLiveData) newLiveData;
        LiveData<Boolean> newStatus = Transformations.map(response, this::transformTMDResponseStatus);
        topRatedMoviesStatusLD = (MutableLiveData) newStatus;
    }


    public LiveData<Boolean> getPopularMoviesStatusLD() {
        return popularMoviesStatusLD;
    }

    public LiveData<List<MovieListItemViewData>> getPopularMoviesLD() {
        Timber.d("getPopularMoviesLD()");
        if (!isStatusTrue(popularMoviesStatusLD) || !isListPopulated(popularMoviesLD)) {
            setPopularMoviesLD();
        }
        Timber.d("Returning LD");
        return popularMoviesLD;
    }

    public LiveData<Boolean> getTopRatedMoviesStatusLD() {
        return topRatedMoviesStatusLD;
    }

    public LiveData<List<MovieListItemViewData>> getTopRatedMoviesLD() {
        Timber.d("getTopRatedMoviesLD()");
        if (!isStatusTrue(topRatedMoviesStatusLD) || !isListPopulated(topRatedMoviesLD)) {
            setTopRatedMoviesLD();
        }
        Timber.d("Returning LD");
        return topRatedMoviesLD;
    }

    /**
     * Transform object held in repository to viewDataObjects
     * @param input
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<MovieListItemViewData> transformTMDResponseResults(ApiResponseMovieListObject input) {
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

    //todo Q?  is it reasonable to use the status at all? Maybe I should just update view based on List<MovieListItemViewData> ?
    private Boolean transformTMDResponseStatus(ApiResponseMovieListObject input) {
        if (input.getResponseResult() == 400) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns false if liveData.getValue == null or list's size inside of it is 0
     * @param liveData
     * @return
     */
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

    /**
     *
     * @return list with all LiveData objects exposed to ListActivity
     */
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
