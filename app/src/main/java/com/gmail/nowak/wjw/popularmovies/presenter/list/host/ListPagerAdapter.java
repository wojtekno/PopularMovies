package com.gmail.nowak.wjw.popularmovies.presenter.list.host;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;
import com.gmail.nowak.wjw.popularmovies.presenter.list.MovieListFragment;

import timber.log.Timber;

public class ListPagerAdapter extends FragmentStateAdapter {
    public ListPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Timber.d("CreateFragment:%d", position);
        MovieListFragment listFragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putString("list_tag", ListTag.values()[position].name());
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public int getItemCount() {
        return ListTag.values().length;
    }
}
