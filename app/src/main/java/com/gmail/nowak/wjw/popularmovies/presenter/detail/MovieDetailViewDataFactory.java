package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiReview;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiVideo;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.ReviewViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MovieDetailViewDataFactory {

    public MovieDetailViewDataFactory() {

    }

    public static MovieDetailViewData create(FavouriteMovie favouriteMovie, LiveData<List<ApiVideo>> videoLD, LiveData<List<ApiReview>> reviewLD, LiveData<Boolean> isFavourite) {
//        return new MovieDetailViewData(favouriteMovie.getTMDId(), favouriteMovie.getTitle(), favouriteMovie.getPosterPath(), reviewLD, videoLD, isFavourite);
    return null;
    }

    public static MovieDetailViewData create(ApiMovie apiMovie, LiveData<List<ApiVideo>> videoLD, LiveData<List<ApiReview>> reviewLD, LiveData<Boolean> isFavourite) {
//        return new MovieDetailViewData(apiMovie.getApiId(), apiMovie.getPosterPath(), apiMovie.getOriginalTitle(), apiMovie.getTitle(),
//                apiMovie.getOverview(), apiMovie.getAverageRating(), apiMovie.getReleaseDate(), apiMovie.getOriginalLanguage(), reviewLD, videoLD, isFavourite);
        return null;
    }

    public static MovieDetailViewData create(ApiMovie apiMovie, LiveData<List<ApiReview>> reviewLD, LiveData<Boolean> isFavourite) {
        List<VideoViewData> list = new ArrayList<>();
        return null;
//        return new MovieDetailViewData(apiMovie.getApiId(), apiMovie.getPosterPath(), apiMovie.getOriginalTitle(), apiMovie.getTitle(),
//                apiMovie.getOverview(), apiMovie.getAverageRating(), apiMovie.getReleaseDate(), apiMovie.getOriginalLanguage(), reviewLD, apiMovie.getVideoList(), isFavourite);
    }

    public static MovieDetailViewData create(ApiMovie apiMovie, LiveData<Boolean> isFavourite) {
        Timber.d("MovieDetailViewData create(ApiMovie apiMovie,  LiveData<Boolean> isFavourite)");
        List<VideoViewData> videoList = new ArrayList<>();
        if(apiMovie.getVideoList()!=null){
            for (ApiVideo apiVideo : apiMovie.getVideoList()) {
                videoList.add(new VideoViewData(apiVideo.getName(), apiVideo.getKey()));
            }
        }
        List<ReviewViewData> reviewList = new ArrayList<>();
        if(apiMovie.getReviewList()!=null) {
            for(ApiReview apiReview : apiMovie.getReviewList()){
                reviewList.add(new ReviewViewData(apiReview.getContent(), apiReview.getAuthor()));
            }
        }
        MutableLiveData reviewsLD = new MutableLiveData();
        reviewsLD.setValue(reviewList);
        MutableLiveData videosLD = new MutableLiveData();
        videosLD.setValue(videoList);
        return new MovieDetailViewData(apiMovie.getApiId(), apiMovie.getPosterPath(), apiMovie.getOriginalTitle(), apiMovie.getTitle(),
                apiMovie.getOverview(), apiMovie.getAverageRating(), apiMovie.getReleaseDate(), apiMovie.getOriginalLanguage(), reviewsLD, videosLD, isFavourite);
    }
}
