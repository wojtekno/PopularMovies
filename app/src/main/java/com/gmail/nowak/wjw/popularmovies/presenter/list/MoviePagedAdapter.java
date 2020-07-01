package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.databinding.ItemMovieBinding;

public class MoviePagedAdapter extends PagedListAdapter<MovieListItemViewData, MoviePagedAdapter.MovieListViewDataHolder> {

    private MovieAdapter.OnMovieListItemClickListener mListener;
    private LifecycleOwner mLifecycleOwner;


    protected MoviePagedAdapter(MovieAdapter.OnMovieListItemClickListener listener, LifecycleOwner lifecycleOwner) {
        super(diffCallback);
        mListener = listener;
        mLifecycleOwner = lifecycleOwner;

    }

    @NonNull
    @Override
    public MovieListViewDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_movie, parent, false);
        itemBinding.setLifecycleOwner(mLifecycleOwner);
        return new MovieListViewDataHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewDataHolder holder, int position) {
        holder.binding.setMovieItem(getItem(position));
        holder.binding.executePendingBindings();
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

    private static final DiffUtil.ItemCallback<MovieListItemViewData> diffCallback = new DiffUtil.ItemCallback<MovieListItemViewData>() {
        @Override
        public boolean areItemsTheSame(@NonNull MovieListItemViewData oldItem, @NonNull MovieListItemViewData newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieListItemViewData oldItem, @NonNull MovieListItemViewData newItem) {
            return oldItem.equals(newItem);
        }
    };
}
