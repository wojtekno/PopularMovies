package com.gmail.nowak.wjw.popularmovies.data.model.api;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieInterface;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiMovie {

    //todo change to id
    @SerializedName("id")
    private int apiId;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_title")
    private String originalTitle;
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("vote_average")
    private float averageRating;
    @SerializedName("release_date")
    private String releaseDate;
    private float popularity;
    private boolean video;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("genre_ids")
    private int[] genreIds;
    private boolean adult;

    //TODO manage to set retrofit, gson,converterFactories and ApiReviewDeserialize so it can be List<ApiReview>
    @SerializedName("reviews")
    private ApiResponseReviewObject apiResponseReviewObject;
    @SerializedName("videos")
    private ApiResponseVideoObject apiResponseVideoObject;

    public int getApiId() {
        return apiId;
    }

    public ApiMovie() {
    }

    public ApiMovie(int apiId, String posterPath, String originalTitle) {
        this.apiId = apiId;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
    }

    public List<ApiReview> getReviewList() {
        if (apiResponseReviewObject == null || apiResponseVideoObject.getResults() == null) {
            return null;
        }
        return apiResponseReviewObject.getResults();
    }

    public List<ApiVideo> getVideoList() {
        if (apiResponseVideoObject == null) {
            return null;
        }
        return apiResponseVideoObject.getResults();
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
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

    public String getTitle() {
        return title;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

}
