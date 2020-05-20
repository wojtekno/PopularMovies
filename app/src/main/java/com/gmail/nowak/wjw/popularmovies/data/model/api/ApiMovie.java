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
    //    private List<ApiReview> reviewList;
    @SerializedName("videos")
    private ApiResponseVideoObject apiResponseVideoObject;
//    private List<ApiVideo> videoList;

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int mTMDId) {
        this.apiId = mTMDId;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public ApiMovie() {
    }

    public List<ApiReview> getReviewList() {
        if (apiResponseReviewObject == null || apiResponseVideoObject.getResults() == null) {
            return null;
        }
        return apiResponseReviewObject.getResults();
    }

    public void setReviewList(List<ApiReview> reviews) {
        if (apiResponseReviewObject == null) {
            apiResponseReviewObject = new ApiResponseReviewObject();
        }
//        if (apiResponseVideoObject.getResults() == null) {
//            apiResponseReviewObject.setResults(new ArrayList<>());
//        }

        apiResponseReviewObject.setResults(reviews);

    }

    public void setVideoList(List<ApiVideo> reviews) {
        if (apiResponseVideoObject == null) {
            apiResponseVideoObject = new ApiResponseVideoObject();
        }
//        if (apiResponseVideoObject.getResults() == null) {
//            apiResponseReviewObject.setResults(new ArrayList<>());
//        }

        apiResponseVideoObject.setResults(reviews);

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

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "MovieDTO{" +
                "mOriginalTitle='" + originalTitle + '\'' +
                ", mImageThumbnail='" + posterPath + '\'' +
                ", mOverview='" + overview + '\'' +
                ", mAverageRating='" + averageRating + '\'' +
                ", mReleaseDate='" + releaseDate + '\'' +
                ", mPopularity='" + popularity + '\'' +
                ", mTMDId='" + apiId + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

}
