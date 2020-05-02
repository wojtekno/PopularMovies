package com.gmail.nowak.wjw.popularmovies.presenter.main;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.gmail.nowak.wjw.popularmovies.presenter.detail.DetailActivity;
import com.gmail.nowak.wjw.popularmovies.data.model.MovieInterface;
import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.databinding.ActivityMainBinding;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnRecyclerItemClickListener {

    private static final int MAX_PAGES_TO_FETCH = 3;

    private static final String POPULARITY_TAG_TITLE = NetworkUtils.POPULARITY_TAG_TITLE;
    private static final String TOP_RATED_TAG_TITLE = NetworkUtils.TOP_RATED_TAG_TITLE;
    private static final String FAVOURITE_TAG_TITLE = "Favourite";

    private static final int POPULAR_MOVIES_TAG = 0;
    private static final int TOP_RATED_MOVIES_TAG = 1;
    private static final int FAVOURITE_MOVIES_TAG = 2;
    private static final String DISPLAYED_LIST_TAG = "displayed_list";

    private MovieAdapter movieAdapter;
    private ActivityMainBinding binding;

    private Toast mToast;
    MainViewModel viewModel;
    // stores the currently displayed tab's tag
    private int displayedTab = POPULAR_MOVIES_TAG;
    //TODO delete call & implement cancelling requests
    //TODO handle app when no internet && on failure

    RecyclerView recyclerView;
    MyGridLayoutManager myGridLayoutManager;


//    private static final List<Integer> MOVIE_TAB_LIST = new ArrayList<>();
//    private static final Map<String, Integer> MOVIE_TAB_MAP = new HashMap<>();
//    private static final String POPULAR_MOVIES_TAGS = "";
//    private static final String TOP_RATED_MOVIES_TAGS = null;
//    private static final String FAVOURITE_MOVIES_TAGs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /*RecyclerView */
        recyclerView = binding.rvMovies;
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
        //todo handle layoutmanager on demand
        myGridLayoutManager = new MyGridLayoutManager(this, 1);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (savedInstanceState != null) {
            Timber.d("savedInstanceState.DisplayedListTag: %d ", savedInstanceState.getInt(DISPLAYED_LIST_TAG));
            displayedTab = savedInstanceState.getInt(DISPLAYED_LIST_TAG);
        }

        updateUIOnLoading();

        //todo check how app behaves when this switch commentedout
        switch (displayedTab) {
            case TOP_RATED_MOVIES_TAG:
                loadTopRatedMovies();
                break;
            case FAVOURITE_MOVIES_TAG:
                loadFavouriteMovies(null);
                break;
            default:
                loadPopularMovies();
        }
    }


    private void loadPopularMovies() {
        displayedTab = POPULAR_MOVIES_TAG;
        removeOldObserversAddNew(viewModel.getPopularMoviesLD(), viewModel.getPopularMoviesStatusLD());
    }

    private void loadTopRatedMovies() {
        displayedTab = TOP_RATED_MOVIES_TAG;
        removeOldObserversAddNew(viewModel.getTopRatedMoviesLD(), viewModel.getTopRatedMoviesStatusLD());
    }

    private void loadFavouriteMovies(@Nullable List<FavouriteMovie> movies) {
        displayedTab = FAVOURITE_MOVIES_TAG;
        removeOldObserversAddNew(viewModel.getFavouriteMovies(), null);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//        reloadRecyclerView(viewModel.getFavouriteMovies().getValue());
    }

    private void removeOldObserversAddNew(LiveData listLD, @Nullable LiveData statusLD) {
        //remove recent observers
        List<LiveData> list = viewModel.getLivaDataList();
        for (LiveData ld : list) {
            ld.removeObservers(this);
        }

        //observe specific data
        listLD.observe(this, new Observer<List<MovieVM>>() {
            @Override
            public void onChanged(List<MovieVM> movieVMs) {
                Timber.d("%d OnChange triggered", displayedTab);
                if (movieVMs == null || movieVMs.size() == 0) {
                    updateUIWhenNoResults(movieVMs);
                } else {
                    reloadRecyclerView(movieVMs);
                }
//                        }
            }
        });

        if (statusLD != null) {
            statusLD.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean status) {
                    if (status) {
                        Timber.d("%d status: true", displayedTab);
                    } else {
                        Timber.d("%d status: failure", displayedTab);
                        updateUIOnFailure();
                        movieAdapter.clearMoviesData();
                        movieAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private void reloadRecyclerView(List<? extends MovieInterface> movieDTOS) {
        updateUIOnLoading();
        movieAdapter.clearMoviesData();
        movieAdapter.setMoviesData(movieDTOS);
        movieAdapter.notifyDataSetChanged();
        updateUIOnResponse();
        if (movieDTOS.size() > 0) {
            binding.countTV.setVisibility(View.VISIBLE);
            binding.countTV.setText(String.format("(%d)", movieDTOS.size()));

            if (movieDTOS.get(0).getType() != MovieInterface.TYPE_MOVIE_DTO) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            } else {
                recyclerView.setLayoutManager(myGridLayoutManager);
            }
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DISPLAYED_LIST_TAG, displayedTab);
//        Timber.d("onSaveState.DisplayedListTag: %d" , displayedList);
    }

    private void updateUIOnLoading() {
        binding.mainProgressBarr.setVisibility(View.VISIBLE);
        binding.centralErrorMessageTv.setVisibility(View.GONE);
    }

    private void updateUIOnResponse() {
        updateSortByTitleTV();
        binding.centralErrorMessageTv.setVisibility(View.GONE);
        binding.mainProgressBarr.setVisibility(View.GONE);
        binding.upperErrorMessageTv.setVisibility(View.GONE);
        binding.sortByTitleTv.setVisibility(View.VISIBLE);
    }

    private void updateUIWhenNoResults(List<? extends MovieInterface> list) {
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
        updateSortByTitleTV();
    }

    private void updateUIOnFailure() {
        binding.centralErrorMessageTv.setText(getString(R.string.response_failure));
        binding.centralErrorMessageTv.setVisibility(View.VISIBLE);
        binding.mainProgressBarr.setVisibility(View.GONE);
//        binding.sortByTitleTv.setVisibility(View.GONE);
        movieAdapter.clearMoviesData();
        movieAdapter.notifyDataSetChanged();
        updateSortByTitleTV();
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
//        Timber.d("displayedIndex = %d", displayedList);
        for (int i = 0; i < menu.size(); i++) {
            boolean visible = displayedTab == i ? false : true;
            MenuItem mitem = menu.getItem(i);
            mitem.setVisible(visible);
//            Timber.d("menu item ndex: %d title %s, visible: %d", i, mitem.getTitle(), visible == true ? 1 : 0);
        }
    }

    /**
     * Set up onMenuItemClickListeners to menu's menuitems
     *
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
                        Toast.makeText(MainActivity.this, "FAvourite here to display", Toast.LENGTH_SHORT).show();
                    }
                    setMenuItemsVisibility(menu);
                    return true;
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void updateSortByTitleTV() {
        switch (displayedTab) {
            case TOP_RATED_MOVIES_TAG:
                binding.sortByTitleTv.setText(TOP_RATED_TAG_TITLE);
                break;
            case FAVOURITE_MOVIES_TAG:
                binding.sortByTitleTv.setText(FAVOURITE_TAG_TITLE);
                break;
            default:
                binding.sortByTitleTv.setText(POPULARITY_TAG_TITLE);
        }

    }

    @Override
    public void onRecyclerItemClick(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_SUBJECT, (MovieVM) movieAdapter.getMoviesData().get(position));
        startActivity(intent);
    }

    private void showBusyToast() {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(MainActivity.this, "Downloading data in progress, try again later", Toast.LENGTH_SHORT);
        mToast.show();
    }
}
