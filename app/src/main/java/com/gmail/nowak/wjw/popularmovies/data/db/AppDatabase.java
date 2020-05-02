package com.gmail.nowak.wjw.popularmovies.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;

import timber.log.Timber;

@Database(entities = FavouriteMovie.class, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favourite_movies";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d("Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Timber.d("Getting the database instance");
        return sInstance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("Alter TABLE movie rename to favourite_movies");
        }
    };

    public abstract MovieDao movieDao();
}
