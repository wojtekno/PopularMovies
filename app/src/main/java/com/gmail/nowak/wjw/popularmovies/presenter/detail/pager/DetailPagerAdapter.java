package com.gmail.nowak.wjw.popularmovies.presenter.detail.pager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gmail.nowak.wjw.popularmovies.presenter.detail.DetailFragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DetailPagerAdapter extends FragmentStateAdapter {
    private List<Integer> idList = new ArrayList<>();

    public DetailPagerAdapter(Fragment fragment, int[] ids) {
        super(fragment);
        for (int i : ids) idList.add(i);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Timber.d("createFragment: %s w apiId: %s", position, idList.get(position));
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt("api_id", idList.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        if (idList == null) return 0;
        else return idList.size();
    }
}
