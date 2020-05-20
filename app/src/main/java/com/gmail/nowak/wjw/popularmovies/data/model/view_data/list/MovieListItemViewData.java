package com.gmail.nowak.wjw.popularmovies.data.model.view_data.list;

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
}
