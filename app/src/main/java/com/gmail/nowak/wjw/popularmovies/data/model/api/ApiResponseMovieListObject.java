package com.gmail.nowak.wjw.popularmovies.data.model.api;

import java.util.List;

public class ApiResponseMovieListObject {
    public static final int RESULT_OK = 100;
    public static final int RESULT_NO_ITEMS = 300;
    public static final int RESULT_FAILURE = 400;

    List<ApiMovie> results;
    int responseResult;

    public List<ApiMovie> getResults() {
        return results;
    }

    public int getResponseResult() {
        return responseResult;
    }

    public ApiResponseMovieListObject(List<ApiMovie> movies, int responseStatus) {
        this.results = movies;
        this.responseResult = responseStatus;
    }

    public ApiResponseMovieListObject(List<ApiMovie> movies) {
        this.results = movies;
        this.responseResult = RESULT_FAILURE;
    }
}
