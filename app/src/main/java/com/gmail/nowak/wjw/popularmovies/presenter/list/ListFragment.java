package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.gmail.nowak.wjw.popularmovies.presenter.MainActivity;
import com.gmail.nowak.wjw.popularmovies.presenter.MainViewModel;

import java.util.List;

import timber.log.Timber;

import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.FAVOURITE;
import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.POPULAR;
import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.TOP_RATED;

public class ListFragment extends Fragment implements MovieAdapter.OnMovieListItemClickListener {


    public static final String EXTRA_API_ID = "extra_api_id";
    public static final int DETAIL_ACTIVITY_REQUEST_CODE = 123;
    private MovieAdapter movieAdapter;
    private FragmentListBinding binding;
    private Toast mToast;

    ListViewModel listViewModel;
    MainViewModel mainViewModel;
    // stores the currently displayed tab's tag
    private ListTag displayedTab;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        binding.setLifecycleOwner(this);

        /*RecyclerView */
        //todo Q? should i create factory and inject it(MovieAdapterFactory) from AppContainer or leave it like this?
        movieAdapter = new MovieAdapter(this, this);
        binding.moviesRecyclerView.setHasFixedSize(true);
        binding.moviesRecyclerView.setAdapter(movieAdapter);
        binding.moviesRecyclerView.setLayoutManager(new MyGridLayoutManager(getContext(), 1));

        //requireActivity()
        AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
        listViewModel = new ViewModelProvider(this, appContainer.listViewModelFactory()).get(ListViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.setViewModel(listViewModel);

        setLiveDataObservers();
        setHasOptionsMenu(true);
        return binding.getRoot();

//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setLiveDataObservers() {
        listViewModel.getListTag().observe(getViewLifecycleOwner(), new Observer<ListTag>() {
            @Override
            public void onChanged(ListTag listTag) {
//                Timber.d("getListTag().onChange triggered %s", listTag);
                displayedTab = listTag;
                updateCategoryTitle(listTag);
            }
        });

        listViewModel.getMovieList().observe(getViewLifecycleOwner(), new Observer<List<MovieListItemViewData>>() {
            @Override
            public void onChanged(List<MovieListItemViewData> movieList) {
//                Timber.d("getMovieList().onChange %s triggered %s", displayedTab, movieList.size());
                reloadRecyclerView(movieList);

                //for debugging purposes todo remove the block below
                updateCategoryTitle(displayedTab);
                binding.categoryTitleTv.append(String.format(" (%d)", movieList.size()));
            }
        });

    }

    private void reloadRecyclerView(List<MovieListItemViewData> movieList) {
        movieAdapter.clearMoviesData();
        movieAdapter.setMovieList(movieList);
        movieAdapter.notifyDataSetChanged();
    }

    private void updateCategoryTitle(ListTag tag) {
        switch (tag) {
            case TOP_RATED:
                binding.categoryTitleTv.setText(getString(R.string.top_rate_tag_title));
                break;
            case FAVOURITE:
                binding.categoryTitleTv.setText(getString(R.string.favourite_tag_title));
                break;
            case POPULAR:
                binding.categoryTitleTv.setText(getString(R.string.popular_tag_title));
                break;
            default:
                // TODO: 10.06.20 customize exception
                throw new RuntimeException();
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        setMenuItemsVisibility(menu);
        setUpOnMenuItemClickListeners(menu);
    }

    /**
     * Set visibility of menuItems
     *
     * @param menu
     */
    private void setMenuItemsVisibility(Menu menu) {
//        Timber.d("setMenuItemsVisibility");
        //todo Q? how to get rid of displayedTab?
        //by default set all menuItems visible...
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mitem = menu.getItem(i);
            mitem.setVisible(true);
        }
        //... and depending on displayedTab make some invisible
        MenuItem invisibleItem;
        switch (displayedTab) {
            case TOP_RATED:
                invisibleItem = menu.findItem(R.id.action_show_top_rated);
                break;
            case FAVOURITE:
                invisibleItem = menu.findItem(R.id.action_show_favourite);
                menu.findItem(R.id.action_refresh).setVisible(false);
                break;
            case POPULAR:
                invisibleItem = menu.findItem(R.id.action_show_popular);
                break;
            default:
                // TODO: 11.06.20 customize exception
                throw new RuntimeException();
        }
        invisibleItem.setVisible(false);
    }

    /**
     * Set up onMenuItemClickListeners to menu's menuItems
     *
     * @param menu
     */
    public void setUpOnMenuItemClickListeners(final Menu menu) {
//        Timber.d("setUpOnMenuItemClickListeners");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem current = menu.getItem(i);
            current.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int itemId = item.getItemId();
                    switch (itemId) {
                        case R.id.action_refresh:
                            reloadClicked();
                            break;
                        case R.id.action_show_top_rated:
                            changeTabClicked(TOP_RATED);
                            break;
                        case R.id.action_show_favourite:
                            changeTabClicked(FAVOURITE);
                            break;
                        case R.id.action_show_popular:
                            changeTabClicked(POPULAR);
                            break;
                        default:
                            // TODO: 10.06.20 customize exception
                            throw new RuntimeException();
                    }
                    if (itemId != R.id.action_refresh) {
                        setMenuItemsVisibility(menu);
                    }
                    return true;
                }
            });
        }

    }

    private void changeTabClicked(ListTag listTag) {
        listViewModel.listTagChanged(listTag);
    }

    private void reloadClicked() {
        listViewModel.refreshList();
    }

    @Override
    public void onMovieItemClicked(int position) {
        int apiId = movieAdapter.getMovieList().get(position).getApiId();
        mainViewModel.setSelectedApiId(apiId);
        ((MainActivity) requireActivity()).navController.navigate(R.id.action_listFragment_to_detailFragment);
    }
}
