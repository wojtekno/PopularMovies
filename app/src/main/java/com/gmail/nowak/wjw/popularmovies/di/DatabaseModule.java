package com.gmail.nowak.wjw.popularmovies.di;

import android.app.Application;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gmail.nowak.wjw.popularmovies.data.db.AppDatabase;

import timber.log.Timber;

public class DatabaseModule {

    private static final String DATABASE_NAME = "favourite_movies";
    public AppDatabase appDatabase;

    public DatabaseModule(Application application) {
        Timber.d("DatabaseModule::newInstance");
        appDatabase = Room.databaseBuilder(application,
                AppDatabase.class, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build();
    }


    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("Alter TABLE movie rename to favourite_movies");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE favourite_movies ADD poster_path varchar(255);");
        }
    };
}
