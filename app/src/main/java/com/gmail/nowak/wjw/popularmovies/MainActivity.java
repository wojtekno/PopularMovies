package com.gmail.nowak.wjw.popularmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.databinding.ActivityMainBinding;
import com.gmail.nowak.wjw.popularmovies.network.TMDResponse;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnRecyclerItemClickListener {

    private static final int MAX_PAGES_TO_FETCH = 3;

    private static final String POPULARITY_TAG_TITLE = NetworkUtils.POPULARITY_TAG_TITLE;
    private static final String TOP_RATED_TAG_TITLE = NetworkUtils.TOP_RATED_TAG_TITLE;

    private static final int POPULAR_MOVIES_TAG = 0;
    private static final int TOP_RATED_MOVIES_TAG = 1;
    private static final int FAVOURITE_MOVIES_TAG = 2;
    private static final String DISPLAYED_LIST_TAG = "displayed_list";

    private MovieAdapter movieAdapter;
    private ActivityMainBinding binding;

    private Toast mToast;
    MainViewModel viewModel;
    // stores the currently displayed tab's tag
    private int displayedTab = -1;
    //TODO delete call & implement cancelling requests
    Call<TMDResponse> call;
    //TODO handle app when no internet && on failure


//    private static final List<Integer> MOVIE_TAB_LIST = new ArrayList<>();
//    private static final Map<String, Integer> MOVIE_TAB_MAP = new HashMap<>();
//    private static final String POPULAR_MOVIES_TAGS = "";
//    private static final String TOP_RATED_MOVIES_TAGS = null;
//    private static final String FAVOURITE_MOVIES_TAGs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        RecyclerView recyclerView = binding.rvMovies;
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(this, 1);
        recyclerView.setLayoutManager(myGridLayoutManager);

//        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory(getApplication());
//        viewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        setUpTagMap();
        if (savedInstanceState != null) {
            Timber.d("savedInstanceState.DisplayedListTag: %d ", savedInstanceState.getInt(DISPLAYED_LIST_TAG));
            displayedTab = savedInstanceState.getInt(DISPLAYED_LIST_TAG);
        }
        switch (displayedTab) {
            case TOP_RATED_MOVIES_TAG:
                loadTopRatedMovies();
                break;
            case FAVOURITE_MOVIES_TAG:
                Toast.makeText(this, "Favourite movies tab - work in progress", Toast.LENGTH_LONG);
            default:
                loadPopularMovies();
        }

        viewModel.getMoviesData().observe(this, new Observer<List<MovieDTO>>() {
            @Override
            public void onChanged(List<MovieDTO> movieDTOS) {
                Log.d("MainActivity", "LiveData onChange");
                movieAdapter.clearMoviesData();
                movieAdapter.setMoviesData(movieDTOS);
                movieAdapter.notifyDataSetChanged();
                updateUIOnResponse();

            }
        });
    }

    private void loadTopRatedMovies() {
        updateUIOnLoading();
        viewModel.loadTopRatedMovies();
        displayedTab = TOP_RATED_MOVIES_TAG;
    }

    private void loadPopularMovies() {
        updateUIOnLoading();
        viewModel.loadPopularMovies();
        displayedTab = POPULAR_MOVIES_TAG;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DISPLAYED_LIST_TAG, displayedTab);
//        Timber.d("onSaveState.DisplayedListTag: %d" , displayedList);
    }

    private void updateUIOnLoading() {
        binding.mainProgressBarr.setVisibility(View.VISIBLE);
        binding.errorMessageTv.setVisibility(View.GONE);
    }

    private void updateUIOnResponse() {
        updateSortByTitleTV();
        binding.errorMessageTv.setVisibility(View.GONE);
        binding.mainProgressBarr.setVisibility(View.GONE);
        binding.reloadErrorMessageTv.setVisibility(View.GONE);
        binding.sortByTitleTv.setVisibility(View.VISIBLE);
    }

    private void updateUIOnFailure() {
        binding.mainProgressBarr.setVisibility(View.GONE);
        binding.sortByTitleTv.setVisibility(View.GONE);
        if (movieAdapter.getItemCount() > 0) {
            binding.reloadErrorMessageTv.setVisibility(View.VISIBLE);
        } else {
            binding.errorMessageTv.setVisibility(View.VISIBLE);
        }
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
        if (displayedTab == TOP_RATED_MOVIES_TAG) {
            binding.sortByTitleTv.setText(TOP_RATED_TAG_TITLE);
        } else {
            binding.sortByTitleTv.setText(POPULARITY_TAG_TITLE);
        }

    }

    @Override
    public void onRecyclerItemClick(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_SUBJECT, movieAdapter.getMoviesData().get(position));
        startActivity(intent);
    }


    //
    //
    //
    // Trash section, let's see if we need this
    ///////////////////////////////////////////
    //
    //


    public void cancelRequest(View view) {
        if (call != null) {
            call.cancel();
            updateUIOnFailure();
            Toast.makeText(this, "Request cancelled", Toast.LENGTH_LONG).show();
        }
    }
    /*
    private void setUpTagMap() {
        Resources res = getResources();
        int[] values = res.getIntArray(R.array.movie_tabs_values);
        String[] keys = res.getStringArray(R.array.movie_tabs_keys);
        for (int i = 0; i < values.length; i++) {
            MOVIE_TAB_LIST.add(values[i]);
            MOVIE_TAB_MAP.put(keys[i], values[i]);
        }
        POPULAR_MOVIES_TAGS.concat(res.getString(R.string.popular_tab_key));

    }

     */

    private void showBusyToast() {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(MainActivity.this, "Downloading data in progress, try again later", Toast.LENGTH_SHORT);
        mToast.show();
    }
}
