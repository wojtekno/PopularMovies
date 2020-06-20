package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;
import com.gmail.nowak.wjw.popularmovies.domain.AddRemoveFromFavouriteUseCase;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieDetailsUseCase;

import java.util.List;

import timber.log.Timber;

public class DetailViewModel extends ViewModel {

    private MutableLiveData<MovieDetailViewData> movie;
    private MutableLiveData<Boolean> isVideoListFolded = new MutableLiveData<>(true);
    private AddRemoveFromFavouriteUseCase mAddRemoveUseCase;
    public MutableLiveData<Boolean> isProgressBarVisible;
    public MutableLiveData<Boolean> isErrMsgVisible;
    private LiveData<Integer> errorMessageResId;
    public LiveData<Boolean> isMoreBtnVisible;


    public DetailViewModel(GetMovieDetailsUseCase getMovieDetailsUseCase, AddRemoveFromFavouriteUseCase addRemoveFromFavouriteUseCase) {
        super();
        Timber.d("DetailViewModel::newInstance");
        mAddRemoveUseCase = addRemoveFromFavouriteUseCase;
        errorMessageResId = getMovieDetailsUseCase.getErrorMessageResId();
        isErrMsgVisible = (MutableLiveData<Boolean>) Transformations.map(errorMessageResId, (msg) -> {
            if (msg == null) return false;
            else return true;
        });
        movie = (MutableLiveData<MovieDetailViewData>) getMovieDetailsUseCase.getMovieDetails();
        isProgressBarVisible = (MutableLiveData<Boolean>) Transformations.map(movie, (m) -> false);
        isProgressBarVisible.setValue(true);
        isMoreBtnVisible = Transformations.map(movie, (m) -> {
            if (m.getVideosLD().getValue().size() > 2) return true;
            else return false;
        });
        Timber.d("Constructor END");
    }

    public LiveData<MovieDetailViewData> getMovieLD() {
        return movie;
    }

    public LiveData<Integer> getErrorMessageResId() {
        return errorMessageResId;
    }

    public LiveData<Boolean> isVideoListFolded() {
        return isVideoListFolded;
    }

    public void favouriteButtonClicked() {
        if (movie.getValue().isFavourite().getValue()) {
            removeFromFavourite();
        } else {
            addToFavourite();
        }
    }

    private void addToFavourite() {
        Timber.d("Adding to Favourite");
        mAddRemoveUseCase.addToFavourites(movie.getValue());
    }

    private void removeFromFavourite() {
        Timber.d("removingFromFav");
        mAddRemoveUseCase.removeFromFavourites(movie.getValue().getApiId());
    }

    public void unfoldVideoRVClicked() {
        isVideoListFolded.setValue(!isVideoListFolded.getValue());
    }

    //todo debugging method, delete when submitting
    public void addVideo() {
        Timber.d("movie %d", movie.getValue().getApiId());
        List<VideoViewData> list = movie.getValue().getVideosLD().getValue();
        VideoViewData lVideo = new VideoViewData("Title", "nonExistingKey");
        list.add(lVideo);
        movie.getValue().getVideosLD().setValue(list);
    }

}
