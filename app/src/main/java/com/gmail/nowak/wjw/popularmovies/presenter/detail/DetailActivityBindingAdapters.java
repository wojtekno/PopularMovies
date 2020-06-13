package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiVideo;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.ReviewViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;
import com.gmail.nowak.wjw.popularmovies.network.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DetailActivityBindingAdapters {

    private static final int VIDEOS_DISPLAYED = 2;
    private static final int MAX_VIDEOS_DISPLAYED = 10;

    @BindingAdapter("myImage")
    public static void setImageUrl(ImageView v, String url) {
//        Timber.d("xml setImageURL");
        //todo Q? should i use it with retrofit? or maybe build the url with retrofit interface?
        Uri mUri = NetworkUtils.buildTMDImageUri(url, NetworkUtils.IMAGE_SIZE_MEDIUM);
        NetworkUtils.fetchImageAndSetToView(mUri, v, false);
    }


    @BindingAdapter({"dynamicHeight", "isFolded"})
    public static void setDynamicHeight(RecyclerView view, int adSize, boolean isFolded) {
//        Timber.d("xml dynamicHeight isFolded: %s  adapter.size(): %d", isFolded, adSize);
        float fl = Resources.getSystem().getDisplayMetrics().density;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (isFolded) {
            adSize = adSize < VIDEOS_DISPLAYED ? adSize : VIDEOS_DISPLAYED;
        } else {
            adSize = adSize > MAX_VIDEOS_DISPLAYED ? MAX_VIDEOS_DISPLAYED : adSize;
        }
        params.height = (int) (49 * fl * adSize);
//        view.setLayoutParams(params);
    }

    @BindingAdapter({"adapterList"})
    public static void setVideoAdapterList(RecyclerView view, List<VideoViewData> list) {
//        Timber.d("xml updateAdapter: listSie: %d view.getAdapter: %s", list.getValue() != null ? list.getValue().size() : -1, view.getAdapter() == null ? "null" : "object");
//        Timber.d("setVideoAdapterList movie = %s", movie==null?"null":"notNull");
//        Timber.d("xml updateAdapter: listSie: %d view.getAdapter: %s", list != null ? list.size() : -1, view.getAdapter() == null ? "null" : "object");

        VideoAdapter vAd = (VideoAdapter) view.getAdapter();
        if (list != null && vAd != null) {
//            Timber.d("videoAdapter != null");
            vAd.setVideoList(list);
            vAd.notifyDataSetChanged();
        }

    }

    @BindingAdapter("adapterList")
    public static void setReviewAdapterList(RecyclerView view, List<ReviewViewData> list) {
//        Timber.d("setting reviewAdapter");
        ReviewAdapter adapter = (ReviewAdapter) view.getAdapter();
        List<ReviewViewData> trimmedList = new ArrayList<>();

        if (list != null && adapter != null) {
            int trimmedSize = list.size()>10?10:list.size();

            for(int i=0; i<trimmedSize; i++){
                trimmedList.add(list.get(i));
            }
            adapter.setReviewList(trimmedList);
            adapter.notifyDataSetChanged();
        }
    }

    //
    //
    //
    // Debuging methods
    //todo delete when submitting

    @BindingAdapter("mVisibility")
    public static void setMVisibility(View view, int size) {
        Timber.d("myVisibility size: %d", size);
    }

    @BindingAdapter("mVisibility2")
    public static void setMVisibility(View view, MovieDetailViewData movie) {
        Timber.d("myVisibility2 movie: %s", movie == null ? "null" : "notnul");
        if (movie != null) {
            Timber.d("myVisibility2 movie.videosLD: %s", movie.getVideosLD() == null ? "null" : "notnul");
            if (movie.getVideosLD() != null) {
                Timber.d("myVisibility2 movie.videosLD.getvalue: %s", movie.getVideosLD().getValue() == null ? "null" : "notnul");
                if (movie.getVideosLD().getValue() != null) {
                    Timber.d("myVisibility2 movie.videosLD.getvalue.list.size: %s", movie.getVideosLD().getValue().size());
                }
            }

        }
    }

    @BindingAdapter("mVisibility3")
    public static void setMVisibility(View view, LiveData<List<ApiVideo>> liveData) {
        Timber.d("myVisibility3 liveData: %s", liveData == null ? "null" : "notnul");
        if (liveData != null) {
            Timber.d("myVisibility3 liveData.getValue: %s", liveData.getValue() == null ? "null" : "notnul");
            if (liveData.getValue() != null) {
                Timber.d("myVisibility3 liveData.videosLD.getvalue.list.size: %s", liveData.getValue().size());
            }

        }
    }
}
