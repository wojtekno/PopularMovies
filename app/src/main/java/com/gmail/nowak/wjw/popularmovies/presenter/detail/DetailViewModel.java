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
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.ReviewViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;
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
    //todo update lifecycle dependency so there is a MutableData construcot with initial value
    private MutableLiveData<Boolean> isVideoListFolded = new MutableLiveData<Boolean>();


    public DetailViewModel(@NonNull Application application, int apiId, int displayedTab) {
        super(application);
        Timber.d("constructor start  displayedTab: %d", displayedTab);
        //todo Q? transform apiResultObject to DetailActivityViewModel or to object held within (MovieVN)
        repository = MoviesRepository.getInstance(application);
        LiveData<ApiMovie> aMLD = repository.fetchMovieWithDetails(apiId);
        movie = (MutableLiveData) Transformations.map(aMLD, this::convertToMovieDetail);
        isVideoListFolded.setValue(true);
        Timber.d("Constructor END");
    }

    private MovieDetailViewData convertToMovieDetail(ApiMovie apiMovie) {
        if (apiMovie != null) {
            Timber.d("convertToMovieDetail apiMovie.videos %s  reviews: %s", apiMovie.getVideoList() == null ? "null" : String.valueOf(apiMovie.getVideoList().size()), apiMovie.getReviewList() == null ? "null" : String.valueOf(apiMovie.getReviewList().size()));
        } else {
            Timber.d("convertToMovieDetail:apiMovie == null");
            return null;
        }
        //TODO handle UI if there is no apimovie for favourite movie
        MutableLiveData<Boolean> isFavourite = (MutableLiveData) Transformations.map(repository.getFavouriteMovieByTmdId(apiMovie.getApiId()), this::transformIsFavourite);

        return MovieDetailViewDataFactory.create(apiMovie, isFavourite);
    }


    public MovieDetailViewData getMovieViewData() {
        return movie.getValue();
    }

    public LiveData<MovieDetailViewData> getMovieLD() {
        return movie;
    }

    private Boolean transformIsFavourite(FavouriteMovie source) {
        Timber.d("transforming isFavouriteFlag");
        if (source == null) {
            return false;
        } else {
            return true;
        }
    }

    public LiveData<Boolean> isFavourite() {
        if (movie.getValue() == null) {
            return null;
        }
        return movie.getValue().isFavourite();
    }

    private void addToFavourite() {
        Timber.d("Adding to Favourite");
        FavouriteMovie fMovie = new FavouriteMovie(movie.getValue().getApiId(), movie.getValue().getOriginalTitle(), movie.getValue().getPosterPath());
        repository.addFavouriteMovie(fMovie);
    }

    private void removeFromFavourite() {
        Timber.d("removingFromFav");
        repository.removeFavouriteMovieByServerId(movie.getValue().getApiId());
    }

    public LiveData<List<ReviewViewData>> getReviewsList() {
//        if (movie.getValue() == null) {
//            return new MutableLiveData<>();
//        }
        return movie.getValue().getReviewsLD();
    }

    public LiveData<List<VideoViewData>> getVideosLD() {
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
        List<VideoViewData> list = movie.getValue().getVideosLD().getValue();

        VideoViewData mvideo = new VideoViewData("Title", "nonexistingKey");
//        mvideo.setName("nowe");
        list.add(mvideo);
//        videosLD.setValue(list);
        movie.getValue().getVideosLD().setValue(list);
    }

    private LiveData<ApiMovie> turnFavToApi(FavouriteMovie fMovie) {
        Timber.d("getmovieDetails");
        LiveData<ApiMovie> apiMovie = repository.fetchMovieWithDetails(fMovie.getTMDId());
        return apiMovie;
    }

}
