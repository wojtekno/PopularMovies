package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiReview;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.ReviewViewData;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewWrapper> wrappedList = new ArrayList<>();

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView author;
        View mRootView;
        boolean isOpen;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
//            Timber.d("constructing ViewHolder");
            content = itemView.findViewById(R.id.review_tv);
            author = itemView.findViewById(R.id.review_author_tv);
            mRootView = itemView;
            isOpen = false;
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Timber.d("onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        int a = view.getMeasuredHeight();
        int b = view.getHeight();
        int c = view.getLayoutParams().height;
//        Timber.d("oncreate veiw holder a: %d  b: %d  c:%d", a, b, c);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewWrapper object = wrappedList.get(position);
        if (!object.isExpanded()) {
            holder.content.setMaxLines(3);
        } else {
            holder.content.setMaxLines(Integer.MAX_VALUE);
        }
        holder.content.setText(object.getReview().getContent());
        holder.author.setText(object.getReview().getAuthor());
        holder.mRootView.setOnClickListener((view) -> {
            if (!object.isExpanded()) {
                holder.content.setMaxLines(Integer.MAX_VALUE);
                object.setExpanded(true);
            } else {
                object.setExpanded(false);
                holder.content.setMaxLines(3);
            }
                }
        );
        int a = holder.mRootView.getMeasuredHeight();
        int b = holder.mRootView.getHeight();
        int c = holder.mRootView.getLayoutParams().height;
//        Timber.d("onBindVeiw holder a: %d  b: %d  c:%d", a, b, c);
    }


    @Override
    public int getItemCount() {
        if (wrappedList != null) {
            return wrappedList.size();
        }

        return 0;
    }

    public void setReviewList(List<ReviewViewData> reviewList) {
        wrappedList.clear();
        for (ReviewViewData review : reviewList) {
            wrappedList.add(new ReviewWrapper(review));
        }
    }



    private class ReviewWrapper {
        private ReviewViewData review;
        private boolean expanded;

        public ReviewWrapper(ReviewViewData comment) {
            review = comment;
        }

        public ReviewViewData getReview() {
            return review;
        }

        public void setReview(ReviewViewData review) {
            this.review = review;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }
    }


}
