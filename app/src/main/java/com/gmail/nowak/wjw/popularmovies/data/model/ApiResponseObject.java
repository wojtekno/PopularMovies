package com.gmail.nowak.wjw.popularmovies.data.model;

import java.util.List;

public class ApiResponseObject {
    public static final int RESULT_OK = 100;
    public static final int RESULT_NO_ITEMS = 300;
    public static final int RESULT_FAILURE = 400;


    List<MovieVM> results;
    int responseResult;

    public List<MovieVM> getResults() {
        return results;
    }

    public int getResponseResult() {
        return responseResult;
    }

    public ApiResponseObject(List<MovieVM> movies, int responseStatus) {
        this.results = movies;
        this.responseResult = responseStatus;
    }

    public ApiResponseObject(List<MovieVM> movies) {
        this.results = movies;
        this.responseResult = RESULT_FAILURE;
    }
}
