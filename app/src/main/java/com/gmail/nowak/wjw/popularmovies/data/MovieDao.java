package com.gmail.nowak.wjw.popularmovies.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    public List<Movie> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertMovie(Movie... movies);
}
