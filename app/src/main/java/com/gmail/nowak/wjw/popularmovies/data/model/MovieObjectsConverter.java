package com.gmail.nowak.wjw.popularmovies.data.model;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.MovieListItemViewData;

import java.util.ArrayList;
import java.util.List;

public class MovieObjectsConverter {

    public static MovieListItemViewData convertToMovieMasterListViewData(ApiMovie apiMovie) {
        MovieListItemViewData uiMovie = new MovieListItemViewData(apiMovie.getApiId(), apiMovie.getOriginalTitle(), apiMovie.getPosterPath());
        return uiMovie;
    }

    public static MovieListItemViewData convertToMovieMasterListViewData(FavouriteMovie favouriteMovie) {
        MovieListItemViewData uiMovie = new MovieListItemViewData(favouriteMovie.getTMDId(), favouriteMovie.getTitle(), null);
        return uiMovie;
    }

    public static List<MovieListItemViewData> convertToMovieMasterListViewData(List<ApiMovie> apiMovies) {
        List<MovieListItemViewData> movieList = new ArrayList<>();
        for (ApiMovie apiMovie: apiMovies) {
            movieList.add(convertToMovieMasterListViewData(apiMovie));
        }
        return movieList;
    }

    public static List<MovieListItemViewData> convertToMovieMasterListViewData1(List<FavouriteMovie> movies) {
        List<MovieListItemViewData> movieList = new ArrayList<>();
        for (FavouriteMovie fMovie: movies) {
            movieList.add(convertToMovieMasterListViewData(fMovie));
        }
        return movieList;
    }

}
