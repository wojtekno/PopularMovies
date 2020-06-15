package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewDataFactory;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import java.util.List;

import timber.log.Timber;

//todo Q? problem with this one is that it has the Application which makes it harder to test - doesn't it? - (i don't really do junit here so far, but for the future).
// And when I implement dagger it will be better? cause I won't need to pass Application as a VieWModel argument?
public class DetailViewModel extends ViewModel {
    private static final int VIDEOS_DISPLAYED = 2;
    private static final int MAX_VIDEOS_DISPLAYED = 10;
    //todo implement YT Android Player, so users can play trailer wihin the app

    //todo Q? is it better if I bind MutableLiveData<MovieDetailViewData> in activity_detail and my DetailViewModel, or maybe just DetailViewModel and get the movie using viewModel?
    private MutableLiveData<MovieDetailViewData> movie = new MutableLiveData<>();
    private MoviesRepository repository;
    //todo update lifecycle dependency so there is a MutableData construcot with initial value
    private MutableLiveData<Boolean> isVideoListFolded = new MutableLiveData<Boolean>();


    public DetailViewModel(MoviesRepository moviesRepository, int apiId, int displayedTab) {
        super();
        Timber.d("DetailViewModel::newInstance displayedTab: %d", displayedTab);
        repository = moviesRepository;
        LiveData<ApiMovie> apiMovieLiveData = repository.fetchMovieWithDetailsFromApi(apiId);
        movie = (MutableLiveData) Transformations.map(apiMovieLiveData, this::convertToMovieDetail);
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
        MutableLiveData<Boolean> isFavourite = (MutableLiveData) Transformations.map(repository.getFavouriteMovieByApiId(apiMovie.getApiId()), this::transformIsFavourite);

        return MovieDetailViewDataFactory.create(apiMovie, isFavourite);
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

    private void addToFavourite() {
        Timber.d("Adding to Favourite");
        FavouriteMovie fMovie = new FavouriteMovie(movie.getValue().getApiId(), movie.getValue().getOriginalTitle(), movie.getValue().getPosterPath());
        repository.addFavouriteMovie(fMovie);
    }

    private void removeFromFavourite() {
        Timber.d("removingFromFav");
        repository.removeFavouriteMovieByApiId(movie.getValue().getApiId());
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

    //todo debuging method, delete when submitting
    public void addVideo() {
        Timber.d("movie %d", movie.getValue().getApiId());
        List<VideoViewData> list = movie.getValue().getVideosLD().getValue();
        VideoViewData mvideo = new VideoViewData("Title", "nonexistingKey");
        list.add(mvideo);
        movie.getValue().getVideosLD().setValue(list);
    }

}
