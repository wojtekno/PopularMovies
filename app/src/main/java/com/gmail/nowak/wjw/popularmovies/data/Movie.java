package com.gmail.nowak.wjw.popularmovies.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class Movie {

    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "tmd_id")
    private int mTMDId;

    @ColumnInfo(name = "title")
    private String mTitle;
}
