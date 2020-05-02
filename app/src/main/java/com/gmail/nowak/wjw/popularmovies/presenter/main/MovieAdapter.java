package com.gmail.nowak.wjw.popularmovies.presenter.main;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.gmail.nowak.wjw.popularmovies.data.model.MovieInterface;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<MovieInterface> moviesData;
    private OnRecyclerItemClickListener mListener;

    public MovieAdapter(OnRecyclerItemClickListener listener) {
        moviesData = new ArrayList<MovieInterface>();
        mListener = listener;
    }

    public MovieAdapter(List<MovieInterface> dataSet) {
        moviesData = dataSet;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MovieInterface.TYPE_MOVIE_DTO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_dto, parent, false);
            return new MovieDTOViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_dao, parent, false);
            return new MovieDAOViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == MovieInterface.TYPE_MOVIE_DTO) {
            MovieVM item = (MovieVM) moviesData.get(position);
            if (item.getImageThumbnail() == null) {
                ((MovieDTOViewHolder) holder).imageView.setBackgroundResource(R.drawable.no_image_available_image);
            } else {
                Uri mUri = NetworkUtils.buildTMDImageUri(item.getImageThumbnail(), NetworkUtils.IMAGE_SIZE_SMALL);
                NetworkUtils.fetchImageAndSetToVew(mUri, ((MovieDTOViewHolder) holder).imageView, true);
            }
        } else {
            FavouriteMovie item = (FavouriteMovie) moviesData.get(position);
            ((MovieDAOViewHolder)holder).titleTV.setText(item.getTitle());
            ((MovieDAOViewHolder)holder).idTV.setText(String.valueOf(item.getId()));
        }

//        Timber.d("item data: TMDID: %d", item.getTMDId());
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

    public void setMoviesData(List<? extends MovieInterface> movies) {
        moviesData.addAll(movies);
    }

    public void clearMoviesData() {
        moviesData.clear();
    }

    public class MovieDTOViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public MovieDTOViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.poster_IV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onRecyclerItemClick(getAdapterPosition());
        }
    }

    public class MovieDAOViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTV;
        TextView idTV;

        public MovieDAOViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.movie_dao_title_tv);
            idTV = itemView.findViewById(R.id.movie_dao_id_tv);
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

    @Override
    public int getItemViewType(int position) {
        return moviesData.get(position).getType();
//        return super.getItemViewType(position);
    }

    public List<MovieInterface> getMoviesData() {
        return moviesData;
    }
}
