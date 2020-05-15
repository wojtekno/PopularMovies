package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiReview;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.MovieDetailViewData;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityDetailBinding;
import com.gmail.nowak.wjw.popularmovies.presenter.main.MovieListActivity;

import java.util.List;
import java.util.Observable;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.OnVideoCLickListener {

    private ActivityDetailBinding binding;
    private DetailViewModel viewModel;
    private RecyclerView reviewsRV;
    private ReviewAdapter reviewAdapter;
//    private RecyclerView videoRV;
//    VideoAdapter videoAdapter;

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
            //todo do not load activity - return to Main with ToastMessage "no api id error"
        }

        DetailViewModelFactory factory = new DetailViewModelFactory(getApplication(), listPosition, displayedTab);
        viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        binding.setViewModel(viewModel);
//        binding.setMovie(viewModel.getMovieViewData());

        setUpVideoRecyclerView();
        setUpReviewRecyclerView();
        Timber.d("onCreate_finished");

    }

    private void setUpReviewRecyclerView() {
//        reviewsRV = binding.reviewsRv;
//        reviewsRV.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter();
        binding.setReviewAdapter(reviewAdapter);
//        reviewsRV.setAdapter(reviewAdapter);

        //todo Q? when I'm having a movieLD(that I have to initialize) which contains ReviewListLD I cannot see the way to observe it this way in DetailActvity
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
        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext(), this);
        binding.setVideoAdapter(videoAdapter);
    }

    @Override
    public void onVideoClicked(String videoKey) {
        String key = videoKey;
        openVideo(key);
    }

    public void openVideo(String key) {
        String ytUrl = "https://youtu.be/";
        String url2 = "https://www.youtube.com/watch?v=";
        openWebPage(url2.concat(key));
    }


    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Timber.d("string url: %s", url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
