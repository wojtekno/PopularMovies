package com.gmail.nowak.wjw.popularmovies.data.model.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie {

    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "tmd_id")
    private int apiId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    public FavouriteMovie() {
    }

    @Ignore
    public FavouriteMovie(int apiId, String title, String posterPath) {
        this.apiId = apiId;
        this.title = title;
        this.posterPath = posterPath;

    }

    @Ignore
    public FavouriteMovie(int id, int apiId, String title, String posterPath) {
        this.id = id;
        this.apiId = apiId;
        this.title = title;
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int tMDId) {
        this.apiId = tMDId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
