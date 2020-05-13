package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.VideoAPI;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.List;

import timber.log.Timber;

public class DetailActivityBindingAdapters {

    private static final int VIDEOS_DISPLAYED = 2;
    private static final int MAX_VIDEOS_DISPLAYED = 10;

    @BindingAdapter("myImage")
    public static void setImageUrl(ImageView v, String url) {
//        Timber.d("xml setImageURL");
        Uri mUri = NetworkUtils.buildTMDImageUri(url, NetworkUtils.IMAGE_SIZE_MEDIUM);
        NetworkUtils.fetchImageAndSetToVew(mUri, v, false);
    }

    @BindingAdapter({"dynamicHeight", "isFolded"})
    public static void setDynamicHeight(RecyclerView view, int adSize, boolean isFolded) {
        Timber.d("xml dynamicHeight isFolded: %s  adapter.size(): %d", isFolded, adSize);
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
    public static void setVideoAdapterList(RecyclerView view,List<VideoAPI> list) {
//        Timber.d("xml updateAdapter: listSie: %d view.getAdapter: %s", list.getValue() != null ? list.getValue().size() : -1, view.getAdapter() == null ? "null" : "object");
        Timber.d("xml updateAdapter: listSie: %d view.getAdapter: %s", list!= null ? list.size() : -1, view.getAdapter() == null ? "null" : "object");

        VideoAdapter vAd = (VideoAdapter) view.getAdapter();
        if (list != null && vAd != null) {
            Timber.d("videoAdapter != null");
            vAd.setVideoList(list);
            vAd.notifyDataSetChanged();
//            setDynamicHeight(view, isFolded);
        }

    }
}