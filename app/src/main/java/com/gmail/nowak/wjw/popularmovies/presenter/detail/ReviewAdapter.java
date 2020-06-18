package com.gmail.nowak.wjw.popularmovies.presenter.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail.ReviewViewData;
import com.gmail.nowak.wjw.popularmovies.databinding.ItemReviewBinding;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewWrapper> wrappedList = new ArrayList<>();

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Timber.d("onCreateViewHolder");
        ItemReviewBinding itemReviewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_review, parent, false);
        itemReviewBinding.setLifecycleOwner((LifecycleOwner) parent.getContext());
        return new ReviewViewHolder(itemReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewWrapper reviewWrapper = wrappedList.get(position);
        holder.mBinding.setWrappedReview(reviewWrapper);
        holder.mBinding.executePendingBindings();
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

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ItemReviewBinding mBinding;

        public ReviewViewHolder(@NonNull ItemReviewBinding itemReviewBinding) {
            super(itemReviewBinding.getRoot());
            this.mBinding = itemReviewBinding;
        }

    }


    public class ReviewWrapper {
        private ReviewViewData review;
        private MutableLiveData<Boolean> expanded = new MutableLiveData<>(false);

        public ReviewWrapper(ReviewViewData comment) {
            review = comment;
        }

        public ReviewViewData getReview() {
            return review;
        }

        public void setReview(ReviewViewData review) {
            this.review = review;
        }

        public LiveData<Boolean> isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded.setValue(expanded);
        }
    }


}
