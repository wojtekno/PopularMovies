package com.gmail.nowak.wjw.popularmovies.data.model.api;

import java.util.List;

public class ApiResponseVideoObject {

   private int id;
   private List<ApiVideo> results;

    public int getId() {
        return id;
    }

    public List<ApiVideo> getResults() {
        return results;
    }
}
