package com.gmail.nowak.wjw.popularmovies.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favourite_movies")
    public LiveData<List<FavouriteMovie>> getAllMoviesLIVE();

    @Query("SELECT * FROM favourite_movies")
    public List<FavouriteMovie> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMovie(FavouriteMovie... movies);
}
