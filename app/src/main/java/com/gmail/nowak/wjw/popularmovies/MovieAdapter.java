package com.gmail.nowak.wjw.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    String[] mDataSet;
    public MovieAdapter(String[] dataSet){
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.movieTitleTV.setText("my possess: "+ position);

    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView movieTitleTV;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitleTV = itemView.findViewById(R.id.movie_title_tv);

        }
    }
}
