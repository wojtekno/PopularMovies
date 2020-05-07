package com.gmail.nowak.wjw.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewApiResponseObject {
    private int id;
    private int page;
    @SerializedName("total_pages")
    private int totalPages;
    private int totalResults;
    private List<ReviewAPI> results;

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

    public List<ReviewAPI> getResults() {
        return results;
    }
}
