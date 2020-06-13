package com.gmail.nowak.wjw.popularmovies.data.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponseMovieList {
    public static final int RESULT_FAILURE = 999;

    private int responseCode;
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    private List<ApiMovie> results;

    public List<ApiMovie> getResults() {
        return results;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public ApiResponseMovieList(List<ApiMovie> movies, int responseCode) {
        this.results = movies;
        this.responseCode = responseCode;
    }

    public ApiResponseMovieList(List<ApiMovie> results) {
        this.results = results;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
