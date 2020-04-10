package com.gmail.nowak.wjw.popularmovies;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    List<String> mDataSet;
    private List<MovieDTO> moviesData;

    public MovieAdapter() {
        moviesData = new ArrayList<>();
    }

    public MovieAdapter(List<MovieDTO> dataSet) {
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
        MovieDTO item = moviesData.get(position);
//        holder.movieTitleTV.setText(item.getmOriginalTitle());
//        holder.imgUrl.setText(item.getmImageThumbnail());
        if(item.getmImageThumbnail()==null){
            holder.imageView.setBackgroundResource(R.drawable.no_image_available_image);
        } else {
            Picasso.get().load(NetworkUtils.fetchPosterImage(item.getmImageThumbnail())).error(R.drawable.no_image_available_image).into(holder.imageView);
        }

        //TODO load ore data when last position displayed

    }

    @Override
    public int getItemCount() {
        if (moviesData == null) {
            return 0;
        }
        return moviesData.size();
    }

    public void setMoviesData(List<MovieDTO> movies) {
        moviesData.addAll(movies);
//        Log.d(LOG_TAG, "setMovieData, movies: " + movies.size());
    }

    public void clearMoviesData(){
        moviesData.clear();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView movieTitleTV;
        TextView imgUrl;
        ImageView imageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
//            movieTitleTV = itemView.findViewById(R.id.movie_title_tv);
//            imgUrl = itemView.findViewById(R.id.image_url_tv);
            imageView = itemView.findViewById(R.id.poster_IV);
        }

    }
}
