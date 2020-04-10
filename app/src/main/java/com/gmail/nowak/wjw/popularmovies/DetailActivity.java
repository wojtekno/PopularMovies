package com.gmail.nowak.wjw.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.gmail.nowak.wjw.popularmovies.databinding.ActivityDetailBinding;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent invokingIntent = getIntent();
        MovieDTO movieDTO = null;
        if (invokingIntent.getExtras().containsKey(Intent.EXTRA_SUBJECT)) {
            movieDTO = (MovieDTO) invokingIntent.getExtras().get(Intent.EXTRA_SUBJECT);
        }
        binding.setMovie(movieDTO);
    //todo handle corrupted data
    }


    @BindingAdapter("app:imageUrl")
    public static void imageUrl(ImageView v, String url) {
        Picasso.get().load(NetworkUtils.fetchPosterImage(url)).error(R.drawable.no_image_available_image).into(v);
    }
}
