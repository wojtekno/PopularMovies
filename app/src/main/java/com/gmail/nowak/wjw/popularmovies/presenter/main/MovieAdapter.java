package com.gmail.nowak.wjw.popularmovies.presenter.main;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.data.model.MovieInterface;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieListViewDataHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<MovieInterface> moviesData;
    private OnRecyclerItemClickListener mListener;
    private List<MovieListItemViewData> newData = new ArrayList<>();

    public MovieAdapter(OnRecyclerItemClickListener listener) {
        moviesData = new ArrayList<MovieInterface>();
        mListener = listener;
    }


    public MovieAdapter(List<MovieInterface> dataSet) {
        moviesData = dataSet;
    }

    @NonNull
    @Override
    public MovieListViewDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_view_data, parent, false);
        return new MovieListViewDataHolder(view);

//        if (viewType == MovieInterface.TYPE_MOVIE_DTO) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_dto, parent, false);
//            return new MovieDTOViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_dao, parent, false);
//            return new MovieDAOViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewDataHolder holder, int position) {
        MovieListItemViewData item = newData.get(position);
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

        //        if (getItemViewType(position) == MovieInterface.TYPE_MOVIE_DTO) {
//            if (item.getImageThumbnail() == null) {
//                ((MovieDTOViewHolder) holder).imageView.setBackgroundResource(R.drawable.no_image_available_image);
//            } else {
//                Uri mUri = NetworkUtils.buildTMDImageUri(item.getImageThumbnail(), NetworkUtils.IMAGE_SIZE_SMALL);
//                NetworkUtils.fetchImageAndSetToVew(mUri, ((MovieDTOViewHolder) holder).imageView, true);
//            }
//        } else {
//            FavouriteMovie item = (FavouriteMovie) moviesData.get(position);
//            ((MovieDAOViewHolder) holder).titleTV.setText(item.getTitle());
//            ((MovieDAOViewHolder) holder).idTV.setText(String.valueOf(item.getTMDId()));
//            ((MovieDAOViewHolder) holder).dbIdTV.setText(String.valueOf(item.getId()));
//        }

//        Timber.d("item data: TMDID: %d", item.getTMDId());
        //TODO Q? how to load more data when last position displayed
    }

    @Override
    public int getItemCount() {
        if (newData == null) {
            return 0;
        }
        return newData.size();
//        return 18;
    }

    //    public void setMoviesData(List<? extends MovieInterface> movies) {
//        moviesData.addAll(movies);
//    }

    public void setMoviesData(List<MovieListItemViewData> movies) {
        newData.addAll(movies);
    }

    public void clearMoviesData() {
        newData.clear();
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
            mListener.onRecyclerItemClick(getAdapterPosition());
        }
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
        TextView dbIdTV;

        public MovieDAOViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.movie_dao_title_tv);
            idTV = itemView.findViewById(R.id.movie_dao_id_tv);
            dbIdTV = itemView.findViewById(R.id.movie_dao_db_id);
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

//    @Override
//    public int getItemViewType(int position) {
//        return moviesData.get(position).getType();
////        return super.getItemViewType(position);
//    }

    public List<MovieInterface> getMoviesData() {
        return moviesData;
    }

    public List<MovieListItemViewData> getNewData() {
        return newData;
    }


}
