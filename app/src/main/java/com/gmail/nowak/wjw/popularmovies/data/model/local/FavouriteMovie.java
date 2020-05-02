package com.gmail.nowak.wjw.popularmovies.data.model.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieInterface;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie implements MovieInterface {

    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "tmd_id")
    private int tMDId;

    @ColumnInfo(name = "title")
    private String title;

    public FavouriteMovie(){};

    @Ignore
    public FavouriteMovie(int tMDId, String title) {
        this.tMDId = tMDId;
        this.title = title;
    }

    @Ignore
    public FavouriteMovie(int id, int tMDId, String title) {
        this.id = id;
        this.tMDId = tMDId;
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTMDId() {
        return tMDId;
    }

    public void setTMDId(int tMDId) {
        this.tMDId = tMDId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getType() {
        return MovieInterface.TYPE_MOVIE_DAO;
    }
}
