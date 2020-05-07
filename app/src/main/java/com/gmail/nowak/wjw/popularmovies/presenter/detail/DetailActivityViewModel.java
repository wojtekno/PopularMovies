package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.gmail.nowak.wjw.popularmovies.data.model.ReviewAPI;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import java.util.List;

import timber.log.Timber;

public class DetailActivityViewModel extends AndroidViewModel {

    //todo ask how to resolve this? why having MovieVN here? is having two variables in layout ok (movieVn and ViewModel)?
    private MovieVM movieVM;
    private MoviesRepository repository;
    private LiveData<Boolean> isFavourite = new MutableLiveData<>();
    private LiveData<List<ReviewAPI>> reviewsLD;

    public DetailActivityViewModel(@NonNull Application application, MovieVM movieVM) {
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

    public DetailActivityViewModel(@NonNull Application application, int listPosition, int displayedTab) {
        super(application);
        //todo transform apiResultObject to DetailActivityViewModel or object held within
        repository = MoviesRepository.getInstance(application);
        switch (displayedTab) {
            case 0:
                movieVM = repository.getPopularMoviesResponseLD().getValue().getResults().get(listPosition);
                break;
            default:
                movieVM = repository.getTopRatedMoviesResponseLD().getValue().getResults().get(listPosition);
        }

        LiveData<List<MovieVM>> list;
        isFavourite = Transformations.map(repository.getFavouriteMovieByTmdId(movieVM.getApiId()), (source) -> {
            if (source == null) {
                return false;
            } else {
                return true;
            }
        });
        reviewsLD = repository.getReviewsByMovieApiId(movieVM.getApiId());

    }

    public MovieVM getMovie() {
        return movieVM;
    }

    public LiveData<Boolean> isFavourite() {
        return isFavourite;
    }

    public void addToFavourite() {
        FavouriteMovie movie = new FavouriteMovie(movieVM.getApiId(), movieVM.getOriginalTitle());
        repository.addFavouriteMovie(movie);
    }

    public void removeFromFavourite() {
        Timber.d("removing");
        repository.removeFavouriteMovieByServerId(movieVM.getApiId());
    }

    public LiveData<List<ReviewAPI>> getReviewsList() {
        return reviewsLD;
    }
}
