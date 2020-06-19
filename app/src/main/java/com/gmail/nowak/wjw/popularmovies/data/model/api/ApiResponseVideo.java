package com.gmail.nowak.wjw.popularmovies.data.model.api;

import java.util.List;

public class ApiResponseVideo {

   private int id;
   private List<ApiVideo> results;

    public int getId() {
        return id;
    }

    public List<ApiVideo> getResults() {
        return results;
    }

    public void setResults(List<ApiVideo> list){
        results = list;
    }
}
