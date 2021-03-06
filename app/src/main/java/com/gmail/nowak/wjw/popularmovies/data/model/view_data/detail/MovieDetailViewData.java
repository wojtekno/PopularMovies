package com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MovieDetailViewData {
    private int apiId;
    private String posterPath;
    private String originalTitle;
    private String title;
    private String overview;
    private float averageRating;
    private String releaseDate;
    private String originalLanguage;

    private LiveData<List<ReviewViewData>> reviewsLD;
    private MutableLiveData<List<VideoViewData>> videosLD;
    private LiveData<Boolean> isFavourite;


    public MutableLiveData<List<VideoViewData>> getVideosLD() {
        return videosLD;
    }

    public LiveData<List<ReviewViewData>> getReviewsLD() {
        return reviewsLD;
    }

    public MovieDetailViewData(int apiId, String posterPath, String originalTitle, String title, String overview, float averageRating, String releaseDate, String originalLanguage, LiveData<List<ReviewViewData>> reviewsLD, LiveData<List<VideoViewData>> videosLD, LiveData<Boolean> isFavourite) {
        this.apiId = apiId;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.title = title;
        this.overview = overview;
        this.averageRating = averageRating;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.reviewsLD = reviewsLD;
        this.videosLD = (MutableLiveData<List<VideoViewData>>) videosLD;
        this.isFavourite = isFavourite;
    }

    public int getApiId() {
        return apiId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public LiveData<Boolean> isFavourite() {
        return isFavourite;
    }
}
