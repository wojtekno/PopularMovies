package com.gmail.nowak.wjw.popularmovies;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<MovieDTO> moviesData;
    private OnRecyclerItemClickListener mListener;

    public MovieAdapter(OnRecyclerItemClickListener listener) {
        moviesData = new ArrayList<>();
        mListener = listener;
    }

    public MovieAdapter(List<MovieDTO> dataSet) {
        moviesData = dataSet;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieDTO item = moviesData.get(position);
        if (item.getImageThumbnail() == null) {
            holder.imageView.setBackgroundResource(R.drawable.no_image_available_image);
        } else {
            Uri mUri = NetworkUtils.buildTMDImageUri(item.getImageThumbnail(), NetworkUtils.IMAGE_SIZE_SMALL);
            NetworkUtils.fetchImageAndSetToVew(mUri, holder.imageView, true);
        }

        Timber.d("item data: TMDID: %d", item.getTMDId());
        //TODO load more data when last position displayed
    }

    @Override
    public int getItemCount() {
        if (moviesData == null) {
            return 0;
        }
        return moviesData.size();
//        return 18;
    }

    public void setMoviesData(List<MovieDTO> movies) {
        moviesData.addAll(movies);
    }

    public void clearMoviesData() {
        moviesData.clear();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.poster_IV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onRecyclerItemClick(getAdapterPosition());
        }
    }

    interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(int position);
    }

    public List<MovieDTO> getMoviesData() {
        return moviesData;
    }
}
