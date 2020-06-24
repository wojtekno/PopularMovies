package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gmail.nowak.wjw.popularmovies.MyApplication;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.databinding.FragmentDetailBinding;
import com.gmail.nowak.wjw.popularmovies.di.AppContainer;
import com.gmail.nowak.wjw.popularmovies.presenter.MainViewModel;

import timber.log.Timber;

// todo 3 Q? : make the reviews and video behave as clickable
// todo: implement YT Android Player, so users can play trailers within the app

public class DetailFragment extends Fragment implements VideoAdapter.OnVideoCLickListener {

    private FragmentDetailBinding binding;
    private DetailViewModel viewModel;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        Timber.d("onCreateView");
        binding = DataBindingUtil.inflate(inflater,  R.layout.fragment_detail, container, false);
        binding.setLifecycleOwner(this);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
        DetailViewModelAssistedFactory_Factory detailViewModelAssistedFactory_factory = appContainer.detailViewModelAssistedFactory_factory();
        mainViewModel.getSelectedApiId().observe(getViewLifecycleOwner(), (lApiId)->{

            Timber.d("mainViewModel.getSelectedApiId(): %d" , lApiId);
            viewModel = new ViewModelProvider(this, detailViewModelAssistedFactory_factory.create(lApiId)).get(DetailViewModel.class);
            binding.setViewModel(viewModel);

        });

        setUpVideoRecyclerView();
        setUpReviewRecyclerView();
//        Timber.d("onCreateView_finished");

        return binding.getRoot();
    }

    private void setUpReviewRecyclerView() {
        binding.reviewsRv.setAdapter(new ReviewAdapter());
        binding.reviewsRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setUpVideoRecyclerView() {
        binding.videoRv.setAdapter(new VideoAdapter(this));
        binding.videoRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onVideoClicked(String videoKey) {
        String ytUrlBase = "https://www.youtube.com/watch?v=";
        Uri webPage = Uri.parse(ytUrlBase.concat(videoKey));
        Timber.d("string url: %s", webPage.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
