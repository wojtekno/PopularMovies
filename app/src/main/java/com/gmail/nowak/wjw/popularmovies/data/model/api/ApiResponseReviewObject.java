package com.gmail.nowak.wjw.popularmovies.data.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponseReviewObject {
    private int id;
    private int page;
    @SerializedName("total_pages")
    private int totalPages;
    private int totalResults;
    private List<ApiReview> results;

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<ApiReview> getResults() {
        return results;
    }

    public void setResults(List<ApiReview> results) {
        this.results = results;
    }
}
