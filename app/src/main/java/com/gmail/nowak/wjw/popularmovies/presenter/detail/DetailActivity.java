package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityDetailBinding;
import com.gmail.nowak.wjw.popularmovies.presenter.list.MovieListActivity;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.OnVideoCLickListener {

    private ActivityDetailBinding binding;
    private DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setLifecycleOwner(this);

        Intent invokingIntent = getIntent();
        //TODO Q? first make intent later viewModel or maybe put intent as parameter in viewmodel constructor?
        int listPosition = 0;
        int displayedTab = 0;
        if (invokingIntent.getExtras().containsKey(MovieListActivity.EXTRA_API_ID)) {
            listPosition = invokingIntent.getExtras().getInt(MovieListActivity.EXTRA_API_ID);
            displayedTab = invokingIntent.getExtras().getInt(MovieListActivity.DISPLAYED_LIST_TAG);
        } else {
            //todo Q? do not load activity - return to Main with ToastMessage "no api id error" how to do that?
        }

        DetailViewModelFactory factory = new DetailViewModelFactory(getApplication(), listPosition, displayedTab);
        viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        binding.setViewModel(viewModel);
        //todo Q? is it better if I bind MutableLiveData<MovieDetailViewData> in activity_detail and my DetailViewModel, or maybe just DetailViewModel and get the movie using viewModel(how I did it now)?
//        binding.setMovie(viewModel.getMovieViewData());

        setUpVideoRecyclerView();
        setUpReviewRecyclerView();
        Timber.d("onCreate_finished");
        binding.detailActivityProgressBarr.setVisibility(View.VISIBLE);

    }

    private void setUpReviewRecyclerView() {
        //todo Q? here I use databinging on the activity_detail level only - meaning I bind adapter in the activity.xml but I don't bind item_review.xml
        ReviewAdapter reviewAdapter = new ReviewAdapter();
        binding.setReviewAdapter(reviewAdapter);
//        reviewsRV = binding.reviewsRv;
//        reviewsRV.setLayoutManager(new LinearLayoutManager(this));
//        reviewsRV.setAdapter(reviewAdapter);

        //todo Q? but before I bound it with data binding I had the movie object - movieLD(that I have to initialize) which contains ReviewListLD.
        // I couldn't see the way to observe it this(below way) way in DetailActvity
//        Timber.d("Obserwing reviews list");
//        viewModel.getReviewsList().observe(this, new Observer<List<ApiReview>>() {
//            @Override
//            public void onChanged(List<ApiReview> apiReviews) {
//                Timber.d("reviewList onchanged");
//                reviewAdapter.setReviewList(apiReviews);
//                reviewAdapter.notifyDataSetChanged();
//            }
//        });
    }

    private void setUpVideoRecyclerView() {
        //todo Q? here I bind both adapter in activity_detail and also item_video. and that way I like the most :)
        // And the question is which one is the best? or the most appropriate one?
        VideoAdapter videoAdapter = new VideoAdapter(this);
        binding.setVideoAdapter(videoAdapter);
    }

    @Override
    public void onVideoClicked(String videoKey) {
        String ytUrlBase = "https://www.youtube.com/watch?v=";
        Uri webpage = Uri.parse(ytUrlBase.concat(videoKey));
        Timber.d("string url: %s", webpage.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
