package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;
import com.gmail.nowak.wjw.popularmovies.domain.AddRemoveFromFavouriteUseCase;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieDetailsUseCase;

import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import timber.log.Timber;

public class DetailViewModel extends ViewModel {

//    private MutableLiveData<MovieDetailViewData> movie;
    private MutableLiveData<Boolean> isVideoListFolded = new MutableLiveData<>(true);
    private AddRemoveFromFavouriteUseCase mAddRemoveUseCase;
    public MutableLiveData<Boolean> isProgressBarVisible;
    public MutableLiveData<Boolean> isErrMsgVisible;
    private LiveData<Integer> errorMessageResId;
    public LiveData<Boolean> isMoreBtnVisible;
    public LiveData<MovieDetailViewData> movieFromRx;// = new MutableLiveData<>();


    public DetailViewModel(GetMovieDetailsUseCase getMovieDetailsUseCase, AddRemoveFromFavouriteUseCase addRemoveFromFavouriteUseCase) {
        super();
        Timber.d("DetailViewModel::newInstance");
        mAddRemoveUseCase = addRemoveFromFavouriteUseCase;
        errorMessageResId = getMovieDetailsUseCase.getErrorMessageResId();
        isErrMsgVisible = (MutableLiveData<Boolean>) Transformations.map(errorMessageResId, (msg) -> {
            if (msg == null) return false;
            else return true;
        });
//        movie = (MutableLiveData<MovieDetailViewData>) getMovieDetailsUseCase.getMovieDetails();
        movieFromRx =  LiveDataReactiveStreams.fromPublisher(getMovieDetailsUseCase.viewDataObservable.filter(a -> {
            Timber.d("movie %s", a.getOriginalTitle());
            return true;
        }).toFlowable(BackpressureStrategy.BUFFER));
        isProgressBarVisible = (MutableLiveData<Boolean>) Transformations.map(movieFromRx, (m) -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        });
        isProgressBarVisible.setValue(true);
        isMoreBtnVisible = Transformations.map(movieFromRx, (m) -> {
            if (m.getVideosLD().getValue().size() > 2) return true;
            else return false;
        });
        Timber.d("Constructor END");
    }

//    public LiveData<MovieDetailViewData> getMovieLD() {
//        return movie;
//    }

    public LiveData<Integer> getErrorMessageResId() {
        return errorMessageResId;
    }

    public LiveData<Boolean> isVideoListFolded() {
        return isVideoListFolded;
    }

    public void favouriteButtonClicked() {
        if (movieFromRx.getValue().isFavourite().getValue()) {
            removeFromFavourite();
        } else {
            addToFavourite();
        }
    }

    private void addToFavourite() {
        Timber.d("Adding to Favourite");
        mAddRemoveUseCase.addToFavourites(movieFromRx.getValue());
    }

    private void removeFromFavourite() {
        Timber.d("removingFromFav");
        mAddRemoveUseCase.removeFromFavourites(movieFromRx.getValue().getApiId());
    }

    public void unfoldVideoRVClicked() {
        isVideoListFolded.setValue(!isVideoListFolded.getValue());
    }

    //todo debugging method, delete when submitting
    public void addVideo() {
        Timber.d("movie %d", movieFromRx.getValue().getApiId());
        List<VideoViewData> list = movieFromRx.getValue().getVideosLD().getValue();
        VideoViewData lVideo = new VideoViewData("Title", "nonExistingKey");
        list.add(lVideo);
        movieFromRx.getValue().getVideosLD().setValue(list);
    }

    @Override
    protected void onCleared() {
        Timber.d("onCleared()");
        super.onCleared();
    }

}
