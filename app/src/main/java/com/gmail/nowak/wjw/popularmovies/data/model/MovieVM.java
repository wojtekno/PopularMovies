package com.gmail.nowak.wjw.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieVM implements Parcelable, MovieInterface{

    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String imageThumbnail;
    @SerializedName("overview")
    private String overview;
    @SerializedName("vote_average")
    private String averageRating;
    @SerializedName("release_date")
    private String releaseDate;
    private String popularity;
    @SerializedName("id")
    private int tMDId;

    public int getTMDId() {
        return tMDId;
    }

    public void setTMDId(int mTMDId) {
        this.tMDId = mTMDId;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public MovieVM() {
    }

    private MovieVM(Parcel in) {
        originalTitle = in.readString();
        imageThumbnail = in.readString();
        overview = in.readString();
        averageRating = in.readString();
        releaseDate = in.readString();
        popularity = in.readString();
        tMDId = in.readInt();
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
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
                ", mImageThumbnail='" + imageThumbnail + '\'' +
                ", mOverview='" + overview + '\'' +
                ", mAverageRating='" + averageRating + '\'' +
                ", mReleaseDate='" + releaseDate + '\'' +
                ", mPopularity='" + popularity + '\'' +
                ", mTMDId='" + tMDId + '\'' +
                '}';
    }

    public static final Parcelable.Creator<MovieVM> CREATOR
            = new Parcelable.Creator<MovieVM>() {
        @Override
        public MovieVM createFromParcel(Parcel source) {
            return new MovieVM(source);
        }

        @Override
        public MovieVM[] newArray(int size) {
            return new MovieVM[size];
        }

    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(imageThumbnail);
        dest.writeString(overview);
        dest.writeString(averageRating);
        dest.writeString(releaseDate);
        dest.writeString(popularity);
        dest.writeInt(tMDId);

    }

    @Override
    public int getType() {
        return MovieInterface.TYPE_MOVIE_DTO;
    }
}
