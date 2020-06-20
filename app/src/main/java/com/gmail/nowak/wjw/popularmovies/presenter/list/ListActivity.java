package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.nowak.wjw.popularmovies.MyApplication;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityListBinding;
import com.gmail.nowak.wjw.popularmovies.di.AppContainer;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;
import com.gmail.nowak.wjw.popularmovies.presenter.detail.DetailActivity;

import java.util.List;

import timber.log.Timber;

import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.FAVOURITE;
import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.POPULAR;
import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.TOP_RATED;

public class ListActivity extends AppCompatActivity implements MovieAdapter.OnMovieListItemClickListener {

    //todo in stage 3:
    // - implement fragments holding each list - dissect ListActivity to three fragments
    // - replace DetailActivity with DetailFragment - make it one activity app
    // - implement dagger
    // - implement javaRx
    // - enable watching videos inside the app

    public static final String EXTRA_API_ID = "extra_api_id";
    private MovieAdapter movieAdapter;
    private ActivityListBinding binding;

    ListViewModel viewModel;
    // stores the currently displayed tab's tag
    private ListTag displayedTab;

    // TODO handle case when a list is already cached and there is no internet now -> display error, but keep the cached list
    // TODO: 11.06.20 handle system shutting down teh app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        binding.setLifecycleOwner(this);

        /*RecyclerView */
        //todo Q? should i create factory and inject it(MovieAdapterFactory) from AppContainer or leave it like this?
        movieAdapter = new MovieAdapter(this, this);
        binding.moviesRecyclerView.setHasFixedSize(true);
        binding.moviesRecyclerView.setAdapter(movieAdapter);
        binding.moviesRecyclerView.setLayoutManager(new MyGridLayoutManager(this, 1));

        AppContainer appContainer = ((MyApplication)getApplication()).appContainer;
        viewModel = new ViewModelProvider(this, appContainer.listViewModelFactory()).get(ListViewModel.class);
        binding.setViewModel(viewModel);

        setLiveDataObservers();
    }

    private void setLiveDataObservers() {
        viewModel.getListTag().observe(this, new Observer<ListTag>() {
            @Override
            public void onChanged(ListTag listTag) {
//                Timber.d("getListTag().onChange triggered %s", listTag);
                displayedTab = listTag;
                updateCategoryTitle(listTag);
            }
        });

        viewModel.getMovieList().observe(this, new Observer<List<MovieListItemViewData>>() {
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

    private void changeTabClicked(ListTag listTag) {
        viewModel.listTagChanged(listTag);
    }

    private void reloadClicked() {
        viewModel.refreshList();
    }

    /**
     * Reload recyclerview with provided data. Remove old data held in adapter and set new one.
     *
     * @param movieList list of items to be displayed
     */
    private void reloadRecyclerView(List<MovieListItemViewData> movieList) {
        movieAdapter.clearMoviesData();
        movieAdapter.setMovieList(movieList);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_main, menu);
//        Timber.d("onCreateOptionsMenu()");
        setMenuItemsVisibility(menu);
        setUpOnMenuItemClickListeners(menu);
        return true;
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
                menu.findItem(R.id.refreshAction).setVisible(false);
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
                        case R.id.refreshAction:
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
                    if (itemId != R.id.refreshAction) {
                        setMenuItemsVisibility(menu);
                    }
                    return true;
                }
            });
        }

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
    public void onMovieItemClicked(int position) {
        Timber.d("go to Detail clicked %d", position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_API_ID, movieAdapter.getMovieList().get(position).getApiId());
        startActivity(intent);
    }
}
