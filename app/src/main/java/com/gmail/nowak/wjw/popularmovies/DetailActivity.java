package com.gmail.nowak.wjw.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.gmail.nowak.wjw.popularmovies.databinding.ActivityDetailBinding;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Timber.d("Started");

        Intent invokingIntent = getIntent();
        MovieDTO movieDTO = null;
        if (invokingIntent.getExtras().containsKey(Intent.EXTRA_SUBJECT)) {
            movieDTO = (MovieDTO) invokingIntent.getExtras().get(Intent.EXTRA_SUBJECT);
        }
        binding.setMovie(movieDTO);
    }


    @BindingAdapter("app:imageUrl")
    public static void imageUrl(ImageView v, String url) {
        Uri mUri = NetworkUtils.buildTMDImageUri(url, NetworkUtils.IMAGE_SIZE_MEDIUM);
        NetworkUtils.fetchImageAndSetToVew(mUri, v, false);
    }
}
