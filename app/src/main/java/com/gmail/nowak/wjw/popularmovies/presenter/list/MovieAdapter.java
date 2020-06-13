package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.databinding.ItemMovieBinding;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieListViewDataHolder> {

    private OnMovieListItemClickListener mListener;
    private List<MovieListItemViewData> movieList = new ArrayList<>();
    private LifecycleOwner lifecycleOwner;

    public MovieAdapter(OnMovieListItemClickListener listener) {
        mListener = listener;
    }
    public MovieAdapter(OnMovieListItemClickListener listener, LifecycleOwner lifecycleOwner) {
        mListener = listener;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public MovieListViewDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_movie, parent, false);
        itemBinding.setLifecycleOwner(lifecycleOwner);
        return new MovieListViewDataHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewDataHolder holder, int position) {
        holder.binding.setMovieItem(movieList.get(position));
        holder.binding.executePendingBindings();
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
        ItemMovieBinding binding;


        public MovieListViewDataHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
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
