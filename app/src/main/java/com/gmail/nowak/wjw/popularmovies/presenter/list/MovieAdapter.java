package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieListViewDataHolder> {

    private OnMovieListItemClickListener mListener;
    private List<MovieListItemViewData> movieList = new ArrayList<>();

    public MovieAdapter(OnMovieListItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MovieListViewDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_view_data, parent, false);
        return new MovieListViewDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewDataHolder holder, int position) {
        MovieListItemViewData item = movieList.get(position);
        Uri mUri = NetworkUtils.buildTMDImageUri(item.getImagePath(), NetworkUtils.IMAGE_SIZE_SMALL);
        NetworkUtils.fetchImageAndSetToVew(mUri, holder.posterIV, true);
        if (item.getImagePath() == null || item.getImagePath().isEmpty()) {
            holder.titleTV.setVisibility(View.VISIBLE);
            holder.titleTV.setText(item.getOriginalTitle());
            holder.idTV.setVisibility(View.VISIBLE);
            holder.idTV.setText(String.valueOf(item.getApiId()));
        } else {
            holder.titleTV.setVisibility(View.GONE);
            holder.idTV.setVisibility(View.GONE);
        }

//        Timber.d("item data: TMDID: %d", item.getTMDId());
        //TODO Q? how to load more data when last position displayed
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    public List<MovieListItemViewData> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<MovieListItemViewData> movies) {
        movieList.addAll(movies);
    }

    public void clearMoviesData() {
        movieList.clear();
    }

    public class MovieListViewDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterIV;
        TextView titleTV;
        TextView idTV;

        public MovieListViewDataHolder(@NonNull View itemView) {
            super(itemView);
            posterIV = itemView.findViewById(R.id.poster_IV);
            titleTV = itemView.findViewById(R.id.master_title_tv);
            idTV = itemView.findViewById(R.id.master_api_id_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onMovieItemClicked(getAdapterPosition());
        }

    }

    interface OnMovieListItemClickListener {
        void onMovieItemClicked(int position);

    }


}
