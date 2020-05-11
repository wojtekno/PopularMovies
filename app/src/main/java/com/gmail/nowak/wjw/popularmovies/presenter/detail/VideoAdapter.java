package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.VideoAPI;

import java.util.ArrayList;
import java.util.List;

class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<VideoAPI> videoList = new ArrayList<>();
    OnVideoCLickListener listener;

    public VideoAdapter(OnVideoCLickListener onVideoCLickListener){
        listener = onVideoCLickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.videoTitleTV.setText(videoList.get(position).getName());
//        holder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {

        return videoList.size();
    }

    public void setVideoList(List<VideoAPI> videoAPIS) {
        videoList.clear();
        videoList.addAll(videoAPIS);
    }


    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView videoTitleTV;
        View rootView;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTitleTV = itemView.findViewById(R.id.video_title_tv);
            rootView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                listener.onVideoClicked(getAdapterPosition());
        }
    }

    interface OnVideoCLickListener{
        void onVideoClicked(int position);
    }
}
