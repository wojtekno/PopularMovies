package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.app.Application;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.gmail.nowak.wjw.popularmovies.data.model.ReviewAPI;
import com.gmail.nowak.wjw.popularmovies.data.model.VideoAPI;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import java.util.List;

import timber.log.Timber;

public class DetailViewModel extends AndroidViewModel {
    private static final int VIDEOS_DISPLAYED = 2;
    private static final int MAX_VIDEOS_DISPLAYED = 10;
    //todo implement YT Android Player, so users can play trailer wihin the app

    //todo Q? ask how to resolve this? why having MovieVN here? is having two variables in layout ok (movieVn and ViewModel)?
    private MovieVM movieVM;
    private MoviesRepository repository;
    private LiveData<Boolean> isFavourite = new MutableLiveData<>();
    private LiveData<List<ReviewAPI>> reviewsLD;
    private MutableLiveData<List<VideoAPI>> videosLD;
    private MutableLiveData<Boolean> isVideoListFolded = new MutableLiveData<>();

    public DetailViewModel(@NonNull Application application, MovieVM movieVM) {
        super(application);
        this.movieVM = movieVM;
        repository = MoviesRepository.getInstance(application);
        isFavourite = Transformations.map(repository.getFavouriteMovieByTmdId(movieVM.getApiId()), (source) -> {
            if (source == null) {
                return false;
            } else {
                return true;
            }
        });

    }

    public DetailViewModel(@NonNull Application application, int listPosition, int displayedTab) {
        super(application);
        //todo Q? transform apiResultObject to DetailActivityViewModel or to object held within (MovieVN)
        repository = MoviesRepository.getInstance(application);
        switch (displayedTab) {
            case 0:
                movieVM = repository.getPopularMoviesResponseLD().getValue().getResults().get(listPosition);
                break;
            default:
                movieVM = repository.getTopRatedMoviesResponseLD().getValue().getResults().get(listPosition);
        }

        isFavourite = Transformations.map(repository.getFavouriteMovieByTmdId(movieVM.getApiId()), DetailViewModel::transformIsFavourite);
        reviewsLD = repository.getReviewsByMovieApiId(movieVM.getApiId());
        videosLD = (MutableLiveData) repository.getVideosByMovieApiId(movieVM.getApiId());
        isVideoListFolded.setValue(true);

    }

    private static Boolean transformIsFavourite(FavouriteMovie source) {
        if (source == null) {
            return false;
        } else {
            return true;
        }
    }

    public MovieVM getMovie() {
        return movieVM;
    }

    public LiveData<Boolean> isFavourite() {
        return isFavourite;
    }

    private void addToFavourite() {
        FavouriteMovie movie = new FavouriteMovie(movieVM.getApiId(), movieVM.getOriginalTitle());
        repository.addFavouriteMovie(movie);
    }

    private void removeFromFavourite() {
        Timber.d("removing");
        repository.removeFavouriteMovieByServerId(movieVM.getApiId());
    }

    public LiveData<List<ReviewAPI>> getReviewsList() {
        return reviewsLD;
    }

    public LiveData<List<VideoAPI>> getVideosLD() {
        return videosLD;
    }

    public LiveData<Boolean> isVideoListFolded() {
        return isVideoListFolded;
    }

    public void favouriteButtonClicked(View view) {
        if (isFavourite.getValue()) {
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
        List<VideoAPI> list = videosLD.getValue();
        VideoAPI mvideo = new VideoAPI();
        mvideo.setName("nowe");
        list.add(mvideo);
        videosLD.setValue(list);
    }

}
