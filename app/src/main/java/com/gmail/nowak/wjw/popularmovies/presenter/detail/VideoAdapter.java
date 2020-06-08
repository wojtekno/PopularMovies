package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.VideoViewData;
import com.gmail.nowak.wjw.popularmovies.databinding.ItemVideoBinding;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<VideoViewData> videoList = new ArrayList<>();
    private OnVideoCLickListener listener;
    Context context;

    public VideoAdapter(OnVideoCLickListener onVideoCLickListener) {
        listener = onVideoCLickListener;
    }

    public VideoAdapter(Context context, OnVideoCLickListener lst) {
        this.context = context;
        listener = lst;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_video, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        Timber.d("onCreateViewHolder");

        return new VideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.itemBinding.setVideo(videoList.get(position));
        holder.itemBinding.executePendingBindings();
        holder.itemBinding.setListener(listener);
        Timber.d("onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setVideoList(List<VideoViewData> apiVideos) {
        videoList.clear();
        videoList.addAll(apiVideos);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener {
        ItemVideoBinding itemBinding;
        TextView videoTitleTV;
        View rootView;

        public VideoViewHolder(@NonNull ItemVideoBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
//            videoTitleTV = itemView.findViewById(R.id.video_title_tv);
//            rootView = itemView;
//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//                listener.onVideoClicked(getAdapterPosition());
//        }
    }

    public interface OnVideoCLickListener {
        public void onVideoClicked(String videoKey);
    }
}
