package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.ReviewAPI;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityDetailBinding;
import com.gmail.nowak.wjw.popularmovies.presenter.main.MainActivity;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private DetailActivityViewModel detailViewModel;
    private RecyclerView reviewsRV;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setLifecycleOwner(this);

        Intent invokingIntent = getIntent();
        //TODO first make intent later viewModel or maybe put intent as parameter in viewmodel constructor?
        int listPosition = 0;
        int displayedTab = 0;
        if (invokingIntent.getExtras().containsKey(MainActivity.EXTRA_API_ID)) {
            listPosition = invokingIntent.getExtras().getInt(MainActivity.EXTRA_API_ID);
            displayedTab = invokingIntent.getExtras().getInt(MainActivity.DISPLAYED_LIST_TAG);
        } else {
            //todo do not load activity - return to Main with ToastMessage "no api id error"
        }


        //todo remove
//        MovieVM movieVM = null;
//        if (invokingIntent.getExtras().containsKey(Intent.EXTRA_SUBJECT)) {
//            movieVM = (MovieVM) invokingIntent.getExtras().get(Intent.EXTRA_SUBJECT);
//        }

        DetailViewModelFactory factory = new DetailViewModelFactory(getApplication(), listPosition, displayedTab);
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);

        binding.setMovie(detailViewModel.getMovie());
        binding.setViewModel(detailViewModel);
        reviewsRV = binding.reviewsRv;
        reviewsRV.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter();
        reviewsRV.setAdapter(reviewAdapter);
        detailViewModel.getReviewsList().observe(this, new Observer<List<ReviewAPI>>() {
            @Override
            public void onChanged(List<ReviewAPI> reviewAPIS) {
                reviewAdapter.setReviewList(reviewAPIS);
                reviewAdapter.notifyDataSetChanged();
            }
        });

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
