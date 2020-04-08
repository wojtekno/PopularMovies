package com.gmail.nowak.wjw.popularmovies;

import com.google.gson.annotations.SerializedName;

public class MovieDTO {

    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("poster_path")
    private String mImageThumbnail;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("vote_average")
    private String mAverageRating;
    @SerializedName("release_date")
    private String mReleaseDate;
    private String popularity;

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public MovieDTO(){}

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getmImageThumbnail() {
        return mImageThumbnail;
    }

    public void setmImageThumbnail(String mImageThumbnail) {
        this.mImageThumbnail = mImageThumbnail;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmAverageRating() {
        return mAverageRating;
    }

    public void setmAverageRating(String mAverageRating) {
        this.mAverageRating = mAverageRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    @Override
    public String toString() {
        return "MovieDTO{" +
                "mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mImageThumbnail='" + mImageThumbnail + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mAverageRating='" + mAverageRating + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mPopularity='" + popularity + '\'' +

                '}';
    }
}
