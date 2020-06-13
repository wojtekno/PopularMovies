package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieList;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MovieListViewModel extends AndroidViewModel {

    private MoviesRepository repository;
    private MutableLiveData<List<MovieListItemViewData>> popularMoviesLD;
    private MutableLiveData<List<MovieListItemViewData>> topRatedMoviesLD;
    private MutableLiveData<List<MovieListItemViewData>> favouriteMoviesLD;
    private MutableLiveData<Integer> errorMessagePopularResIdLd;
    private MutableLiveData<Integer> errorMessageTopRatedResIdLd;
    private MutableLiveData<Integer> errorMessageFavouriteResIdLd = new MutableLiveData<>();

    private MutableLiveData<ListTag> listTagLD = new MutableLiveData<>(ListTag.POPULAR);

    public MutableLiveData<Boolean> isErrorMessageVisible;
    public MutableLiveData<Boolean> isProgressBarVisible;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        repository = MoviesRepository.getInstance(application);
        isErrorMessageVisible = (MutableLiveData) Transformations.switchMap(listTagLD, transformErrorMessageVisibility);
        isProgressBarVisible = (MutableLiveData) Transformations.switchMap(listTagLD, transformProgressBarVisibility);
        //set the progress bar to be visible when starting application
        isProgressBarVisible.setValue(true);

        setUpPopularMovieList();
        setUpTopRatedMovieList();
        setUpFavouriteMovies();
    }


    private void setUpPopularMovieList() {
//        Timber.d("setUpPopularMovieList");
        LiveData<ApiResponseMovieList> response = repository.getPopularMoviesResponseLD();
        errorMessagePopularResIdLd = (MutableLiveData) Transformations.map(response, this::getErrorMessageFromApiResponse);
        LiveData<List<MovieListItemViewData>> newLiveData = Transformations.map(response, this::getMovieListFromApiResponse);
        popularMoviesLD = (MutableLiveData) newLiveData;
    }


    private void setUpTopRatedMovieList() {
//        Timber.d("setUpTopRatedMovieList");
        LiveData<ApiResponseMovieList> response = repository.getTopRatedMoviesResponseLD();
        errorMessageTopRatedResIdLd = (MutableLiveData) Transformations.map(response, this::getErrorMessageFromApiResponse);
        LiveData<List<MovieListItemViewData>> newLiveData = Transformations.map(response, this::getMovieListFromApiResponse);
        topRatedMoviesLD = (MutableLiveData) newLiveData;
    }

    /**
     * Returns resource id of an string error
     *
     * @param input
     * @return null when getResponseCode == 200
     */
    private Integer getErrorMessageFromApiResponse(ApiResponseMovieList input) {
//        Timber.d("getErrorMessageFromApiResponse");
        if (input.getResponseCode() == 200) {
            return null;
        } else if (input.getResponseCode() == ApiResponseMovieList.RESULT_FAILURE) {
            return R.string.error_cannot_connect_with_server;
        } else {
            return R.string.error_problem_with_response;
        }
    }


    /**
     * Transform object held in repository to viewDataObjects
     *
     * @param input
     * @return
     */
    private List<MovieListItemViewData> getMovieListFromApiResponse(ApiResponseMovieList input) {
//        Timber.d("getMovieListFromApiResponse()");
        List<MovieListItemViewData> list = new ArrayList<>();
        for (ApiMovie r : input.getResults()) {
            MovieListItemViewData movieListItemViewData = new MovieListItemViewData(r.getApiId(), r.getOriginalTitle(), r.getPosterPath());
            list.add(movieListItemViewData);
        }
        return list;
    }


    private void setUpFavouriteMovies() {
//        Timber.d("setUpFavouriteMovies");
        favouriteMoviesLD = (MutableLiveData) Transformations.map(repository.getFavouriteMoviesLD(), this::getMovieListFromFavourites);
    }


    private List<MovieListItemViewData> getMovieListFromFavourites(List<FavouriteMovie> list) {
        if (list == null) {
            return null;
        }
        List<MovieListItemViewData> result = new ArrayList<>();
        for (FavouriteMovie fm : list) {
            result.add(new MovieListItemViewData(fm.getApiId(), fm.getTitle(), fm.getPosterPath()));
        }
        return result;
    }


    /**
     * Get appropriate movie list based on the ListTag
     *
     * @return movie list
     */
    public LiveData<List<MovieListItemViewData>> getMovieList() {
        return Transformations.switchMap(listTagLD, getMovieListByListTag);
    }


    /**
     * Provides currently displayed listTag
     *
     * @return
     */
    public LiveData<ListTag> getListTag() {
        return listTagLD;
    }


    public LiveData<Integer> getErrorMessage() {
        Timber.d("getErrorMessage");
        return Transformations.switchMap(listTagLD, getErrorMessageByListTag);
    }


    public void reloadMovieList() {
        handleProgressBarEndErrorMessageVisibility();
        repository.reloadApiResponseMovieList(listTagLD.getValue());
    }


    /**
     * Changes listTag to be displayed
     *
     * @param listTag listTag to be displayed
     */
    public void changeTabClicked(ListTag listTag) {
        handleProgressBarEndErrorMessageVisibility();
        listTagLD.setValue(listTag);
    }


    private Function<ListTag, LiveData<List<MovieListItemViewData>>> getMovieListByListTag = new Function<ListTag, LiveData<List<MovieListItemViewData>>>() {
        @Override
        public LiveData<List<MovieListItemViewData>> apply(ListTag input) {
            if (ListTag.TOP_RATED == input) {
//                    Timber.d("getMovieListByListTag transformation %s LD: %s", input, topRatedMoviesLD.getValue() == null ? null : topRatedMoviesLD.getValue().size());
                //todo Q? what to do when the response was null, or size==0  how to fetch data again automatically?
                // todo fix - when error is notnull - prevent from reloading multiple times
                if (errorMessageTopRatedResIdLd.getValue() != null) {
                    reloadMovieList();
                }
                return topRatedMoviesLD;
            } else if (ListTag.FAVOURITE == input) {
//                    Timber.d("getMovieListByListTag transformation %s LD: %s", input, favouriteMoviesLD.getValue() == null ? null : favouriteMoviesLD.getValue().size());
                return favouriteMoviesLD;
            } else {
//                    Timber.d("getMovieListByListTag transformation %s LD: %s", input, popularMoviesLD.getValue() == null ? null : popularMoviesLD.getValue().size());
                if (errorMessagePopularResIdLd.getValue() != null) {
                    reloadMovieList();
                }
                return popularMoviesLD;
            }
        }
    };


    private Function<ListTag, LiveData<Integer>> getErrorMessageByListTag = new Function<ListTag, LiveData<Integer>>() {
        @Override
        public LiveData<Integer> apply(ListTag input) {

            switch (input) {
                case TOP_RATED:
//                    Timber.d("getErrorMessageByListTag: %s %s", input, errorMessageTopRatedResIdLd.getValue());
                    return errorMessageTopRatedResIdLd;
                case FAVOURITE:
                    return errorMessageFavouriteResIdLd;
                case POPULAR:
//                    Timber.d("getErrorMessageByListTag: %s %s", input, errorMessagePopularResIdLd.getValue());
                    return errorMessagePopularResIdLd;
                default:
                    // TODO: 11.06.20 customize
                    throw new RuntimeException();
            }
        }
    };

    private Function<ListTag, LiveData<Boolean>> transformErrorMessageVisibility = (input -> {
//        Timber.d("transformErrorMessageVisibility");
        LiveData<Integer> ld = getErrorMessageByListTag.apply(input);

        return Transformations.map(ld, (err) -> {
//            Timber.d("transformingErrorMessageVisibility: %s", err != null ? "visible" : "invisible");
            if (err == null) return false;
            else return true;
        });
    });

    private Function<ListTag, LiveData<Boolean>> transformProgressBarVisibility = (input -> {
//        Timber.d("transformProgressBarVisibility");
        LiveData<List<MovieListItemViewData>> ld = getMovieListByListTag.apply(input);
        return Transformations.map(ld, (list) -> false);
    });


    private void handleProgressBarEndErrorMessageVisibility() {
        isProgressBarVisible.setValue(true);
        isErrorMessageVisible.setValue(false);
    }


    // TODO: 11.06.20 remove - debugging method
    public void addPopularMovie() {
        Timber.d("addPopularMovie");
//        List<MovieListItemViewData> list = new ArrayList<>(popularMoviesLD.getValue());
        List<MovieListItemViewData> list = popularMoviesLD.getValue();
        list.add(new MovieListItemViewData(12345, "Newly added one", null));
        popularMoviesLD.setValue(list);
    }

}
