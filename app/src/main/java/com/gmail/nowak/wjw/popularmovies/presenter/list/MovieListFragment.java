package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.MyApplication;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.databinding.FragmentListBinding;
import com.gmail.nowak.wjw.popularmovies.di.AppContainer;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

import java.util.List;

import timber.log.Timber;

import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.POPULAR;


//todo in stage 3:
// -implement paging
// - implement javaRx
// - enable watching videos inside the app
// - implement dagger


// TODO handle case when a list is already cached and there is no internet now -> display error, but keep the cached list
// TODO: 11.06.20 handle system shutting down teh app
public class MovieListFragment extends Fragment implements MovieAdapter.OnMovieListItemClickListener {

    private MovieAdapter movieAdapter;
    private FragmentListBinding binding;

    ListViewModel listViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        binding.setLifecycleOwner(this);

        /*RecyclerView */
        movieAdapter = new MovieAdapter(this, this);
        binding.moviesRecyclerView.setHasFixedSize(true);
        binding.moviesRecyclerView.setAdapter(movieAdapter);
        binding.moviesRecyclerView.setLayoutManager(new MyGridLayoutManager(getContext(), 1));


        ListTag mTab = POPULAR;
        if (getArguments() != null) {
            mTab = ListTag.valueOf(getArguments().getString("list_tag"));
            Timber.d("onCreateView:getArguments(): %s", mTab);
        } else Timber.d("getArguments(): noArguments");

        AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
        ListViewModelFactory_Factory listViewModelFactory_factory = appContainer.listViewModeFactory_factory();
        listViewModel = new ViewModelProvider(this, listViewModelFactory_factory.create(mTab)).get(ListViewModel.class);
        binding.setViewModel(listViewModel);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            Timber.d("refreshingList");
            listViewModel.refreshList();
        });
        setLiveDataObservers();
        return binding.getRoot();
    }

    private void setLiveDataObservers() {

        listViewModel.getMovieList().observe(getViewLifecycleOwner(), new Observer<List<MovieListItemViewData>>() {
            @Override
            public void onChanged(List<MovieListItemViewData> movieList) {
//                Timber.d("getMovieList().onChange %s triggered %s", displayedTab, movieList.size());
                reloadRecyclerView(movieList);
                listViewModel.finishedLoading();
            }
        });

    }

    private void reloadRecyclerView(List<MovieListItemViewData> movieList) {
        movieAdapter.clearMoviesData();
        movieAdapter.setMovieList(movieList);
        movieAdapter.notifyDataSetChanged();
    }


    @Override
    public void onMovieItemClicked(int position) {
        listViewModel.movieItemClicked(position);
    }
}
