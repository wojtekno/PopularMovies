package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gmail.nowak.wjw.popularmovies.MyApplication;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityDetailBinding;
import com.gmail.nowak.wjw.popularmovies.di.AppContainer;
import com.gmail.nowak.wjw.popularmovies.presenter.list.ListActivity;

import timber.log.Timber;

// todo: make the reviews and video behave as clickable
// todo: implement YT Android Player, so users can play trailers within the app


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
        int lApiId = 0;
        if (invokingIntent.getExtras().containsKey(ListActivity.EXTRA_API_ID)) {
            lApiId = invokingIntent.getExtras().getInt(ListActivity.EXTRA_API_ID);
        } else {
            //todo Q? do not load activity - return to Main with ToastMessage "no api id error" how to do that?
        }
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        DetailViewModelAssistedFactory_Factory detailViewModelAssistedFactory_factory = appContainer.detailViewModelAssistedFactory_factory();
        viewModel = new ViewModelProvider(this, detailViewModelAssistedFactory_factory.create(lApiId)).get(DetailViewModel.class);

        binding.setViewModel(viewModel);

        setUpVideoRecyclerView();
        setUpReviewRecyclerView();
        Timber.d("onCreate_finished");
//        binding.detailActivityProgressBarr.setVisibility(View.VISIBLE);

    }

    private void setUpReviewRecyclerView() {
        binding.reviewsRv.setAdapter(new ReviewAdapter());
        binding.reviewsRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpVideoRecyclerView() {
        binding.videoRv.setAdapter(new VideoAdapter(this));
        binding.videoRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onVideoClicked(String videoKey) {
        String ytUrlBase = "https://www.youtube.com/watch?v=";
        Uri webPage = Uri.parse(ytUrlBase.concat(videoKey));
        Timber.d("string url: %s", webPage.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
