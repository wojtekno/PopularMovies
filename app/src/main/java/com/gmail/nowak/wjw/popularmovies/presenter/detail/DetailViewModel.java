package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiReview;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiVideo;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import java.util.List;

import timber.log.Timber;

public class DetailViewModel extends AndroidViewModel {
    private static final int VIDEOS_DISPLAYED = 2;
    private static final int MAX_VIDEOS_DISPLAYED = 10;
    //todo implement YT Android Player, so users can play trailer wihin the app

    //todo Q? ask how to resolve this? why having MovieVN here? is having two variables in layout ok (movieVn and ViewModel)?
    private MutableLiveData<MovieDetailViewData> movie = new MutableLiveData<>();
    private MoviesRepository repository;
    //    private LiveData<Boolean> isFavourite = new MutableLiveData<>();
    //    private LiveData<List<ApiReview>> reviewsLD;
//    private MutableLiveData<List<ApiVideo>> videosLD;
    //todo update lifecycle dependency so there is a MutableData construcot with initial value
    private MutableLiveData<Boolean> isVideoListFolded = new MutableLiveData<Boolean>();
    public LiveData<FavouriteMovie> fm = new MutableLiveData<>();
    LiveData<List<FavouriteMovie>> fList = new MutableLiveData<>();


    public DetailViewModel(@NonNull Application application, int listPosition, int displayedTab) {
        super(application);
        Timber.d("constructor start  displayedTab: %d", displayedTab);
        //todo Q? transform apiResultObject to DetailActivityViewModel or to object held within (MovieVN)
        repository = MoviesRepository.getInstance(application);
        switch (displayedTab) {
            case 2:
                fm = repository.getFavouriteMovieByTmdId(listPosition);
                fList = Transformations.map(repository.getFavouriteMoviesLD(), (x) -> (x));
                movie = (MutableLiveData) Transformations.map(fm, this::convertToMovieDetail);
//                isFavourite = Transformations.map(repository.getFavouriteMovieByTmdId(listPosition), this::transformIsFavourite);
                break;
            default:
                movie.setValue(convertToMovieDetail(repository.getApiMovieFromList(displayedTab, listPosition)));
//                isFavourite = Transformations.map(repository.getFavouriteMovieByTmdId(movie.getValue().getApiId()), this::transformIsFavourite);
//                reviewsLD = repository.getReviewsByMovieApiId(movie.getValue().getApiId());
//                videosLD = (MutableLiveData) repository.getVideosByMovieApiId(movie.getValue().getApiId());
        }
        isVideoListFolded.setValue(true);
        Timber.d("Constructor END");
    }

    private Boolean transformIsFavourite(FavouriteMovie source) {
        Timber.d("transforming isFavouriteFlag");
        if (source == null) {
            return false;
        } else {
            return true;
        }
    }

    public MovieDetailViewData getMovieViewData() {
        return movie.getValue();
    }

    public LiveData<MovieDetailViewData> getMovieLD() {
        return movie;
    }

    public LiveData<Boolean> isFavourite() {
        if (movie.getValue() == null) {
            return null;
        }
        return movie.getValue().isFavourite();
    }

    private void addToFavourite() {
        Timber.d("Adding to Favourite");
        FavouriteMovie fMovie = new FavouriteMovie(movie.getValue().getApiId(), movie.getValue().getOriginalTitle());
        repository.addFavouriteMovie(fMovie);
    }

    private void removeFromFavourite() {
        Timber.d("removingFromFav");
        repository.removeFavouriteMovieByServerId(movie.getValue().getApiId());
    }

    public LiveData<List<ApiReview>> getReviewsList() {
//        if (movie.getValue() == null) {
//            return new MutableLiveData<>();
//        }
        return movie.getValue().getReviewsLD();
    }

    public LiveData<List<ApiVideo>> getVideosLD() {
        return movie.getValue().getVideosLD();
    }

    public LiveData<Boolean> isVideoListFolded() {
        return isVideoListFolded;
    }

    public void favouriteButtonClicked(View view) {
        if (movie.getValue().isFavourite().getValue()) {
            removeFromFavourite();
        } else {
            addToFavourite();
        }
    }

    public void unfoldVideoRVClicked(View view) {
        isVideoListFolded.setValue(!isVideoListFolded.getValue());
    }

    public interface OnVideoItemClickListener {
        void onVideoClick(String videoUrl);
    }

    public void addVideo() {
        Timber.d("movie %d", movie.getValue().getApiId());
//        List<ApiVideo> list = videosLD.getValue();
        List<ApiVideo> list = movie.getValue().getVideosLD().getValue();

        ApiVideo mvideo = new ApiVideo();
        mvideo.setName("nowe");
        list.add(mvideo);
//        videosLD.setValue(list);
        movie.getValue().getVideosLD().setValue(list);
    }

    private MovieDetailViewData convertToMovieDetail(ApiMovie apiMovie) {
        MutableLiveData<List<ApiReview>> reviewsLD = (MutableLiveData) repository.getReviewsByMovieApiId(apiMovie.getApiId());
        MutableLiveData<List<ApiVideo>> mVid = (MutableLiveData) repository.getVideosByMovieApiId(apiMovie.getApiId());
        MutableLiveData<Boolean> isFavourite = (MutableLiveData) Transformations.map(repository.getFavouriteMovieByTmdId(apiMovie.getApiId()), this::transformIsFavourite);

        return MovieDetailViewDataFactory.create(apiMovie, mVid, reviewsLD, isFavourite);
    }

    private MovieDetailViewData convertToMovieDetail(FavouriteMovie fMovie) {
        int movieApiId;

        if (fMovie == null) {
            movieApiId = movie.getValue().getApiId();
            fMovie= new FavouriteMovie(movieApiId, movie.getValue().getOriginalTitle());
        } else {
            movieApiId = fMovie.getTMDId();
        }
        MutableLiveData<List<ApiReview>> reviewsLD = (MutableLiveData) repository.getReviewsByMovieApiId(movieApiId);
        MutableLiveData<List<ApiVideo>> mVid = (MutableLiveData) repository.getVideosByMovieApiId(movieApiId);
        MutableLiveData<Boolean> isFavourite = (MutableLiveData) Transformations.map(repository.getFavouriteMovieByTmdId(movieApiId), this::transformIsFavourite);

        Timber.d("converting value");
        return MovieDetailViewDataFactory.create(fMovie, mVid, reviewsLD, isFavourite);
    }

}
