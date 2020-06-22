package com.gmail.nowak.wjw.popularmovies.data.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiMovie {

    public static final int RESULT_FAILURE = 999;
    private int responseCode;

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
    private ApiResponseReview apiResponseReview;
    @SerializedName("videos")
    private ApiResponseVideo apiResponseVideo;

    public int getApiId() {
        return apiId;
    }

    public ApiMovie() {
    }

    public ApiMovie(int responseCode) {
        this.responseCode = responseCode;
    }

    public ApiMovie(int apiId, String posterPath, String originalTitle) {
        this.apiId = apiId;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
    }

    public List<ApiReview> getReviewList() {
        if (apiResponseReview == null || apiResponseVideo.getResults() == null) {
            return new ArrayList<>();
        }
        return apiResponseReview.getResults();
    }

    public List<ApiVideo> getVideoList() {
        if (apiResponseVideo == null || apiResponseVideo.getResults() == null) {
            return new ArrayList<>();
        }
        return apiResponseVideo.getResults();
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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
