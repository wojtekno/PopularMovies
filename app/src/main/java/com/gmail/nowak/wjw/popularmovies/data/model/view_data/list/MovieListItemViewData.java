package com.gmail.nowak.wjw.popularmovies.data.model.view_data.list;

import androidx.annotation.Nullable;

public class MovieListItemViewData {

    private int apiId;
    private String originalTitle;
    private String imagePath;

    public MovieListItemViewData() {
    }

    public MovieListItemViewData(int apiId, String originalTitle, String imagePath) {
        this.imagePath = imagePath;
        this.apiId = apiId;
        this.originalTitle = originalTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getApiId() {
        return apiId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result * apiId;
        result = 31 * result * originalTitle.hashCode();
        result = 31 * result * imagePath.hashCode();

        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MovieListItemViewData)) {
            return false;
        }

        MovieListItemViewData movie = (MovieListItemViewData) obj;
        return apiId == movie.apiId && imagePath.equals(movie.imagePath) && originalTitle.equals(movie.originalTitle);
    }
}
