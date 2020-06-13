package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.network.NetworkUtils;

import timber.log.Timber;

public class ListActivityBindingAdapters {

    /**
     * Set imageView background depending on the posterPath - if it's null or empty, set placeHolder
     *
     * @param imageView
     * @param posterPath
     */
    @BindingAdapter("android:background")
    public static void setImage(ImageView imageView, String posterPath) {
        if (posterPath == null || posterPath.isEmpty()) {
            imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(R.drawable.no_image_available_image));
        } else {
            Uri mUri = NetworkUtils.buildTMDImageUri(posterPath, NetworkUtils.IMAGE_SIZE_SMALL);
            NetworkUtils.fetchImageAndSetToView(mUri, imageView, true);
        }
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, String posterPath) {
        if (posterPath == null || posterPath.isEmpty()) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("android:text")
    public static void setText(TextView view, Integer errResId) {
//        Timber.d("taking getErrorMessage");
        if (errResId != null) view.setText(view.getResources().getString(errResId));
    }

}
