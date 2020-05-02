package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gmail.nowak.wjw.popularmovies.AppExecutors;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityDetailBinding;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    MoviesRepository repository;
    MovieVM movieVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Timber.d("Started");
        repository = MoviesRepository.getInstance(getApplication());

        Intent invokingIntent = getIntent();
        movieVM = null;
        if (invokingIntent.getExtras().containsKey(Intent.EXTRA_SUBJECT)) {
            movieVM = (MovieVM) invokingIntent.getExtras().get(Intent.EXTRA_SUBJECT);
        }
        binding.setMovie(movieVM);
    }


    @BindingAdapter("app:imageUrl")
    public static void imageUrl(ImageView v, String url) {
        Uri mUri = NetworkUtils.buildTMDImageUri(url, NetworkUtils.IMAGE_SIZE_MEDIUM);
        NetworkUtils.fetchImageAndSetToVew(mUri, v, false);
    }

    public void addToFavourite(View view) {
        final FavouriteMovie movie = new FavouriteMovie(movieVM.getTMDId(), movieVM.getOriginalTitle());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                repository.addFavouriteMovie(movie);

            }
        });
    }

}
