package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
    private DetailActivityViewModel detailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setLifecycleOwner(this);
        Timber.d("Started");
//        repository = MoviesRepository.getInstance(getApplication());

        Intent invokingIntent = getIntent();
        //TODO first make intent later viewModel or maybe put intent as parameter in viewmodel constructor?
        MovieVM movieVM = null;
        if (invokingIntent.getExtras().containsKey(Intent.EXTRA_SUBJECT)) {
            movieVM = (MovieVM) invokingIntent.getExtras().get(Intent.EXTRA_SUBJECT);
        }

        DetailViewModelFactory factory = new DetailViewModelFactory(getApplication(), movieVM);
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);

        binding.setMovie(detailViewModel.getMovie());
        binding.setViewModel(detailViewModel);
    }


    @BindingAdapter("app:imageUrl")
    public static void imageUrl(ImageView v, String url) {
        Uri mUri = NetworkUtils.buildTMDImageUri(url, NetworkUtils.IMAGE_SIZE_MEDIUM);
        NetworkUtils.fetchImageAndSetToVew(mUri, v, false);
    }

    //todo is this better or observing the detailViewModel.isFavourite() and setting onClickListeners on change?
    public void favouriteButtonClicked(View view) {
        if (detailViewModel.isFavourite().getValue()) {
            detailViewModel.removeFromFavourite();
        } else {
            detailViewModel.addToFavourite();
        }
    }

}
