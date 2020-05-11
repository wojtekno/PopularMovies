package com.gmail.nowak.wjw.popularmovies.data.model;

import androidx.lifecycle.LifecycleService;

import java.util.List;

public class VideoApiResponseObject {

   private int id;
   private List<VideoAPI> results;

    public int getId() {
        return id;
    }

    public List<VideoAPI> getResults() {
        return results;
    }
}
