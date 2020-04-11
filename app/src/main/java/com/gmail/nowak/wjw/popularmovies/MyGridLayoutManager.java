package com.gmail.nowak.wjw.popularmovies;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyGridLayoutManager extends GridLayoutManager {
    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        autoAdjustSpanCount();
    }


    private void autoAdjustSpanCount() {
        int totalSpace;
        if (getOrientation() == VERTICAL) {
            totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
        } else {
            totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
        }
        if (totalSpace / getSpanCount() > 300) {
            setSpanCount(getSpanCount() + 1);
            autoAdjustSpanCount();
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        autoAdjustSpanCount();
        super.onLayoutChildren(recycler, state);
    }
}
