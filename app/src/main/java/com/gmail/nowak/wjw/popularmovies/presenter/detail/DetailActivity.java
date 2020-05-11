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
import com.gmail.nowak.wjw.popularmovies.data.model.VideoAPI;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityDetailBinding;
import com.gmail.nowak.wjw.popularmovies.presenter.main.MainActivity;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.List;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.OnVideoCLickListener {

    private ActivityDetailBinding binding;
    private DetailViewModel detailViewModel;
    private RecyclerView reviewsRV;
    private ReviewAdapter reviewAdapter;
    private RecyclerView videoRV;
    private VideoAdapter videoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setLifecycleOwner(this);

        Intent invokingIntent = getIntent();
        //TODO Q? first make intent later viewModel or maybe put intent as parameter in viewmodel constructor?
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
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        binding.setMovie(detailViewModel.getMovie());
        binding.setViewModel(detailViewModel);
        videoRV = binding.videoRv;
        videoAdapter = new VideoAdapter(this);
        videoRV.setAdapter(videoAdapter);
        videoRV.setLayoutManager(new LinearLayoutManager(this));

        detailViewModel.getVideosLD().observe(this, new Observer<List<VideoAPI>>() {
            @Override
            public void onChanged(List<VideoAPI> videoAPIS) {
                videoAdapter.setVideoList(videoAPIS);
                videoAdapter.notifyDataSetChanged();
            }
        });

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

//        detailViewModel.getVideosLD().observe(this, (videos) -> {
//            Timber.d("getVideosTriggered");
//            StringBuffer buffer = new StringBuffer();
//            for(VideoAPI video : videos){
//                buffer.append(video.getKey()+"\n");
//            }
//            Timber.d("Buffer: %s", buffer.toString());
//            binding.videoTv.setText(buffer.toString());
//
//        });
    }


    @BindingAdapter("app:imageUrl")
    public static void imageUrl(ImageView v, String url) {
        Uri mUri = NetworkUtils.buildTMDImageUri(url, NetworkUtils.IMAGE_SIZE_MEDIUM);
        NetworkUtils.fetchImageAndSetToVew(mUri, v, false);

    }

    //todo Q? is this better or observing the detailViewModel.isFavourite() and setting onClickListeners on change?
    public void favouriteButtonClicked(View view) {
//        binding.videoTv.setText(detailViewModel.getVideoStrings());
        if (detailViewModel.isFavourite().getValue()) {
            detailViewModel.removeFromFavourite();
        } else {
            detailViewModel.addToFavourite();
        }
    }

    public void openVideo(String key){
//       String videoKey = detailViewModel.getVideosLD().getValue().get(0).getKey();
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

    @Override
    public void onVideoClicked(int position) {
        String key = detailViewModel.getVideosLD().getValue().get(position).getKey();
        openVideo(key);
    }
}
