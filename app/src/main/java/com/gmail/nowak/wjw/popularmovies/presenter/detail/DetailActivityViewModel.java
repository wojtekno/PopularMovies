package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import timber.log.Timber;

public class DetailActivityViewModel extends AndroidViewModel {

    //todo ask how to resolve this? why having MovieVN here? is having two variables in layout ok (movieVn and ViewModel)?
    private MovieVM movieVM;
    private MoviesRepository repository;
    private LiveData<Boolean> isFavourite = new MutableLiveData<>();

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
}
