package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.MovieDTO;

import java.util.List;

public class TMDResponse {
    List<MovieDTO> results;

    public List<MovieDTO> getResults() {
        return results;
    }
}
