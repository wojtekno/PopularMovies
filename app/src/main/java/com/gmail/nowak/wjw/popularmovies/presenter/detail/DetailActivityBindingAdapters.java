package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.res.Resources;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.ReviewViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;
import com.gmail.nowak.wjw.popularmovies.network.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

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
        params.height = (int) (72 * fl * adSize);
    }

    @BindingAdapter({"adapterList"})
    public static void setVideoAdapterList(RecyclerView view, List<VideoViewData> list) {
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
            int trimmedSize = list.size() > 10 ? 10 : list.size();

            for (int i = 0; i < trimmedSize; i++) {
                trimmedList.add(list.get(i));
            }
            adapter.setReviewList(trimmedList);
            adapter.notifyDataSetChanged();
        }
    }

}
