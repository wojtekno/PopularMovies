package com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiReview;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiVideo;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MovieDetailViewDataFactory {

    public static MovieDetailViewData create(ApiMovie apiMovie, LiveData<Boolean> isFavourite) {
//        Timber.d("MovieDetailViewData create(apiMovie, isFavourite)");
        List<VideoViewData> videoList = new ArrayList<>();
        for (ApiVideo apiVideo : apiMovie.getVideoList()) {
            videoList.add(new VideoViewData(apiVideo.getName(), apiVideo.getKey()));
//            Timber.d("apiVideoSite = %s", apiVideo.getSite());
            if ("youtube".compareToIgnoreCase(apiVideo.getSite()) != 0) {
                Timber.e("apiVideo.getSite() is not youtube. It's: %s. You won't be able to watch this video", apiVideo.getSite());
            }
        }
        List<ReviewViewData> reviewList = new ArrayList<>();
        for (ApiReview apiReview : apiMovie.getReviewList()) {
            reviewList.add(new ReviewViewData(apiReview.getContent(), apiReview.getAuthor()));
        }
        LiveData<List<ReviewViewData>> reviewsLD = new MutableLiveData(reviewList);
        LiveData<List<VideoViewData>> videosLD = new MutableLiveData(videoList);
        return new MovieDetailViewData(apiMovie.getApiId(), apiMovie.getPosterPath(), apiMovie.getOriginalTitle(), apiMovie.getTitle(),
                apiMovie.getOverview(), apiMovie.getAverageRating(), apiMovie.getReleaseDate(), apiMovie.getOriginalLanguage(), reviewsLD, videosLD, isFavourite);
    }
}
