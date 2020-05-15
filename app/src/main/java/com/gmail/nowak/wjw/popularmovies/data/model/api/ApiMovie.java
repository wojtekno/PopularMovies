package com.gmail.nowak.wjw.popularmovies.data.model.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieInterface;
import com.google.gson.annotations.SerializedName;

public class ApiMovie implements Parcelable, MovieInterface {

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

    private ApiMovie(Parcel in) {
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        averageRating = in.readFloat();
        releaseDate = in.readString();
        popularity = in.readFloat();
        apiId = in.readInt();
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

    public static final Parcelable.Creator<ApiMovie> CREATOR
            = new Parcelable.Creator<ApiMovie>() {
        @Override
        public ApiMovie createFromParcel(Parcel source) {
            return new ApiMovie(source);
        }

        @Override
        public ApiMovie[] newArray(int size) {
            return new ApiMovie[size];
        }

    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeFloat(averageRating);
        dest.writeString(releaseDate);
        dest.writeFloat(popularity);
        dest.writeInt(apiId);

    }

    @Override
    public int getType() {
        return MovieInterface.TYPE_MOVIE_DTO;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }
}
