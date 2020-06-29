package com.gmail.nowak.wjw.popularmovies.presenter.list.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import timber.log.Timber;


/**
 * Only purpose of this class is to hold a view pager
 */
public class ListPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        View view = inflater.inflate(R.layout.fragment_list_pager, container, false);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ListPagerAdapter(this));

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    ListTag lTag = ListTag.values()[position];
                    int resId;
                    switch (lTag) {
                        case POPULAR:
                            resId = R.string.popular_tag_title;
                            break;
                        case TOP_RATED:
                            resId = R.string.top_rate_tag_title;
                            break;
                        case FAVOURITE:
                            resId = R.string.favourite_tag_title;
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                    tab.setText(resId);
                }
        ).attach();
        return view;
    }
}
