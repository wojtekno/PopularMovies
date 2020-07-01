package com.gmail.nowak.wjw.popularmovies.data.model.api;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDataBaseOrgAPI;

public class MovieListDataSourceFactory extends DataSource.Factory<Integer, ApiMovie> {

    private TheMovieDataBaseOrgAPI mMovieDataBaseOrgAPI;

    public MovieListDataSourceFactory(TheMovieDataBaseOrgAPI movieDataBaseOrgAPI) {
        this.mMovieDataBaseOrgAPI = movieDataBaseOrgAPI;
    }

    @NonNull
    @Override
    public DataSource<Integer, ApiMovie> create() {
        return new MovieListDataSource(mMovieDataBaseOrgAPI);
    }
}
