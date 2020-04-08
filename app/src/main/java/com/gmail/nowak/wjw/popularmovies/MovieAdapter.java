package com.gmail.nowak.wjw.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    String[] mDataSet;
    private MovieDTO[] moviesData;

    public MovieAdapter() {
    }

    public MovieAdapter(MovieDTO[] dataSet) {
//        mDataSet = dataSet;
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
        MovieDTO item = moviesData[position];
        holder.movieTitleTV.setText(item.getmOriginalTitle());
        holder.imgUrl.setText(item.getmImageThumbnail());
        Picasso.get().load(NetworkUtils.fetchPosterImage(item.getmImageThumbnail())).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (moviesData == null) {
            return 0;
        }
        return moviesData.length;
    }

    public void setMoviesData(MovieDTO[] movies) {
        moviesData = movies;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView movieTitleTV;
        TextView imgUrl;
        ImageView imageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitleTV = itemView.findViewById(R.id.movie_title_tv);
            imgUrl = itemView.findViewById(R.id.image_url_tv);
            imageView = itemView.findViewById(R.id.poster_IV);
        }
    }
}
