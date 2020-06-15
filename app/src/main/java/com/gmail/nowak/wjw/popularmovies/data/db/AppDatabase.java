package com.gmail.nowak.wjw.popularmovies.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;

@Database(entities = FavouriteMovie.class, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
