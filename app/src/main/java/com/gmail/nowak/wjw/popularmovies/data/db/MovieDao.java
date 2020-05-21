package com.gmail.nowak.wjw.popularmovies.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavouriteMovie>> getAllMoviesLIVE();

    @Query("SELECT * FROM favourite_movies")
    List<FavouriteMovie> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(FavouriteMovie... movies);

    @Query("SELECT * FROM favourite_movies where tmd_id =:tmdId")
    LiveData<FavouriteMovie> selectByTmdId(int tmdId);

    @Query("SELECT * FROM favourite_movies where tmd_id =:tmdId")
    FavouriteMovie selectFMByTmdId(int tmdId);

    @Query("SELECT * FROM favourite_movies where _id =:dbId")
    FavouriteMovie selectDirectlyById(int dbId);

    @Delete()
    void removeMovie(FavouriteMovie... movies);

    @Query("DELETE FROM favourite_movies where tmd_id =:tmdid")
    void removeMovieById(int tmdid);
}
