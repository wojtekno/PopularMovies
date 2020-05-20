package com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail;

public class VideoViewData {
    private String title;
    private String videoKey;

    public VideoViewData(String title, String videoKey) {
        this.title = title;
        this.videoKey = videoKey;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoKey() {
        return videoKey;
    }
}
