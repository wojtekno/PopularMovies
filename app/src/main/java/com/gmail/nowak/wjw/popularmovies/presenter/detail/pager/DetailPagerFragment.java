package com.gmail.nowak.wjw.popularmovies.presenter.detail.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.gmail.nowak.wjw.popularmovies.R;

public class DetailPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_pager, container, false);

        int[] lIds = DetailPagerFragmentArgs.fromBundle(getArguments()).getApiIdList();
        int lPosition = DetailPagerFragmentArgs.fromBundle(getArguments()).getListPosition();

        ViewPager2 viewPager = view.findViewById(R.id.detail_view_pager);
        viewPager.setAdapter(new DetailPagerAdapter(this, lIds));
        viewPager.setCurrentItem(lPosition, false);

        return view;
    }
}
