package com.gmail.nowak.wjw.popularmovies.presenter.list;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;
import com.gmail.nowak.wjw.popularmovies.presenter.detail.DetailActivity;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityMainBinding;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.List;

import timber.log.Timber;

import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.FAVOURITE;
import static com.gmail.nowak.wjw.popularmovies.presenter.ListTag.TOP_RATED;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.OnMovieListItemClickListener {

    //todo in stage 3:
    // - implement fragments holding each list - dissect ListActivity to three fragments
    // - replace DetailActivity with DetailFragment - make it one activity app
    // - implement dagger
    // - implement javaRx
    // - enable watching videos inside the app

    public static final String DISPLAYED_LIST_TAG = "displayed_list";

    public static final String EXTRA_API_ID = "extra_api_id";
    private MovieAdapter movieAdapter;
    private ActivityMainBinding binding;

    MovieListViewModel viewModel;
    // stores the currently displayed tab's tag
    private ListTag displayedTab = ListTag.POPULAR;

    //TODO delete call & implement cancelling requests
    //TODO handle app when no internet && on failure

    RecyclerView recyclerView;
//    MyGridLayoutManager myGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /*TODO Q?  I've got 3 recyclerViews - one here and two in detailActivity.
           And the question is which one is the best? or the most appropriate one?
           And I got them configured in 3 different ways
           Here there is plain one with no data binding
         */
        /*RecyclerView */
        recyclerView = binding.rvMovies;
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new MyGridLayoutManager(this, 1));


        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        if (savedInstanceState != null) {
            int displayedTabOrdinal = savedInstanceState.getInt(DISPLAYED_LIST_TAG);
            Timber.d("savedInstanceState.DisplayedListTag: %d ", displayedTabOrdinal);
            displayedTab = ListTag.values()[displayedTabOrdinal];
        }

//        viewModel.loadMovieList(displayedTab);

        switch (displayedTab) {
            case TOP_RATED:
                loadTopRatedMovies();
                break;
            case FAVOURITE:
                loadFavouriteMovies(null);
                break;
            default:
                loadPopularMovies();
        }
        Timber.d("onCreate-finished - displayedTab: %s", displayedTab.name());
    }


    private void loadPopularMovies() {
        updateUIOnLoading();
        displayedTab = ListTag.POPULAR;
        removeOldObserversAddNew(viewModel.getPopularMoviesLD(), viewModel.getPopularMoviesStatusLD());
    }

    private void loadTopRatedMovies() {
        updateUIOnLoading();
        displayedTab = TOP_RATED;
        removeOldObserversAddNew(viewModel.getTopRatedMoviesLD(), viewModel.getTopRatedMoviesStatusLD());
    }

    private void loadFavouriteMovies(@Nullable List<FavouriteMovie> movies) {
        updateUIOnLoading();
        displayedTab = FAVOURITE;
        removeOldObserversAddNew(viewModel.getFavouriteMovies(), null);
    }

    /**
     * Remove observers from exposed LiveData objects.  Register new ones for specific list.
     * @param listLD LiveData holding List<MovieListItemViewData>
     * @param statusLD LiveData holding Boolean
     */
    private void removeOldObserversAddNew(LiveData listLD, @Nullable LiveData statusLD) {
        //remove recent observers
        List<LiveData> list = viewModel.getLiveDataList();
        for (LiveData ld : list) {
            ld.removeObservers(this);
//            Timber.d("ld has observers: %s  ative: %s", ld.hasObservers(), ld.hasActiveObservers());
        }

        //observe specific data
        listLD.observe(this, new Observer<List<MovieListItemViewData>>() {
            @Override
            public void onChanged(List<MovieListItemViewData> apiMovies) {
                Timber.d("%s OnChange triggered", displayedTab.name());
                if (apiMovies == null || apiMovies.size() == 0) {
                    updateUIWhenNoResults(apiMovies);
                } else {
                    reloadRecyclerView(apiMovies);
                }
            }
        });
//        Timber.d("ListLd has observers %s  acive: %s", listLD.hasObservers(), listLD.hasActiveObservers());

        if (statusLD != null) {
            statusLD.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean status) {
                    if (status) {
                        Timber.d("%s status: true", displayedTab.name());
                    } else {
                        Timber.d("%s status: failure", displayedTab.name());
                        updateUIOnFailure();
                        movieAdapter.clearMoviesData();
                        movieAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    /**
     * Reload recyclerview with provided data. Remove old data held in adapter and set new one.
     * @param movieList list of items to be displayed
     */
    private void reloadRecyclerView(List<MovieListItemViewData> movieList) {
        movieAdapter.clearMoviesData();
        movieAdapter.setMovieList(movieList);
        movieAdapter.notifyDataSetChanged();
        updateUIOnResponse();
        //todo remove the block below
        if (movieList.size() > 0) {
            binding.countTV.setVisibility(View.VISIBLE);
            binding.countTV.setText(String.format("(%d)", movieList.size()));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DISPLAYED_LIST_TAG, displayedTab.ordinal());
//        Timber.d("onSaveState.DisplayedListTag: %d" , displayedList);
    }

    private void updateUIOnLoading() {
        binding.mainProgressBarr.setVisibility(View.VISIBLE);
        binding.centralErrorMessageTv.setVisibility(View.GONE);
    }

    private void updateUIOnResponse() {
        updateCategoryTitle();
        binding.centralErrorMessageTv.setVisibility(View.GONE);
        binding.mainProgressBarr.setVisibility(View.GONE);
        binding.upperErrorMessageTv.setVisibility(View.GONE);
        binding.categoryTitleTv.setVisibility(View.VISIBLE);
    }

    /**
     * This method should handle UI behaviour when I got response from API, but for some reason couldn't read the results.
     * @param list
     */
    //todo Q?
    private void updateUIWhenNoResults(List<MovieListItemViewData> list) {
        if (list == null) {
            binding.centralErrorMessageTv.setText(getString(R.string.couldnt_read_results));

        } else if (list.size() == 0) {
            binding.centralErrorMessageTv.setText(getString(R.string.no_items_found));
        }
        binding.centralErrorMessageTv.setVisibility(View.VISIBLE);
        binding.mainProgressBarr.setVisibility(View.GONE);
//        binding.sortByTitleTv.setVisibility(View.GONE);
        movieAdapter.clearMoviesData();
        movieAdapter.notifyDataSetChanged();
        updateCategoryTitle();
    }

    /**
     * Handle UI when API request onFailure or no internet connection
     */
    private void updateUIOnFailure() {
        binding.centralErrorMessageTv.setText(getString(R.string.response_failure));
        binding.centralErrorMessageTv.setVisibility(View.VISIBLE);
        binding.mainProgressBarr.setVisibility(View.GONE);
//        binding.sortByTitleTv.setVisibility(View.GONE);
        movieAdapter.clearMoviesData();
        movieAdapter.notifyDataSetChanged();
        updateCategoryTitle();
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
     * @param menu
     */
    private void setMenuItemsVisibility(Menu menu) {
//        Timber.d("displayedIndex = %d", displayedList);
        for (int i = 0; i < menu.size(); i++) {
            boolean visible = displayedTab.ordinal() == i ? false : true;
            MenuItem mitem = menu.getItem(i);
            mitem.setVisible(visible);
//            Timber.d("menu item ndex: %d title %s, visible: %d", i, mitem.getTitle(), visible == true ? 1 : 0);
        }
    }

    /**
     * Set up onMenuItemClickListeners to menu's menuItems
     * @param menu
     */
    public void setUpOnMenuItemClickListeners(final Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem current = menu.getItem(i);
            current.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Resources res = getResources();
                    CharSequence title = item.getTitle();
                    if (res.getString(R.string.popular_tag_title).equals(title)) {
                        loadPopularMovies();
                    } else if (res.getString(R.string.top_rate_tag_title).equals(title)) {
                        loadTopRatedMovies();
                    } else {
                        loadFavouriteMovies(null);
                        Toast.makeText(MovieListActivity.this, "FAvourite here to display", Toast.LENGTH_SHORT).show();
                    }
                    setMenuItemsVisibility(menu);
                    return true;
                }
            });
        }

    }

    private void updateCategoryTitle() {
        switch (displayedTab) {
            case TOP_RATED:
                binding.categoryTitleTv.setText(getString(R.string.top_rate_tag_title));
                break;
            case FAVOURITE:
                binding.categoryTitleTv.setText(getString(R.string.favourite_tag_title));
                break;
            default:
                binding.categoryTitleTv.setText(getString(R.string.popular_tag_title));
        }

    }

    @Override
    public void onMovieItemClicked(int position) {
        Timber.d("go to Detail clicked %d", position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_API_ID, movieAdapter.getMovieList().get(position).getApiId());
        intent.putExtra(DISPLAYED_LIST_TAG, displayedTab);
        startActivity(intent);
    }

}
