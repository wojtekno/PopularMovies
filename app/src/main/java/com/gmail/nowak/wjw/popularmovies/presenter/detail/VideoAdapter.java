package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;
import com.gmail.nowak.wjw.popularmovies.databinding.ItemVideoBinding;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoViewData> videoList = new ArrayList<>();
    private OnVideoCLickListener listener;

    public VideoAdapter(OnVideoCLickListener onVideoCLickListener) {
        listener = onVideoCLickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_video, parent, false);
        return new VideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
//        Timber.d("onBindViewHolder");
        holder.itemBinding.setVideo(videoList.get(position));
        holder.itemBinding.executePendingBindings();
        holder.itemBinding.setListener(listener);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setVideoList(List<VideoViewData> apiVideos) {
        videoList.clear();
        videoList.addAll(apiVideos);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        ItemVideoBinding itemBinding;

        public VideoViewHolder(@NonNull ItemVideoBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }
    }

    public interface OnVideoCLickListener {
        void onVideoClicked(String videoKey);
    }
}
