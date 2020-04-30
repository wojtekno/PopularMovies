package com.gmail.nowak.wjw.popularmovies;

import android.content.Intent;
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
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDatabaseAPI;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnRecyclerItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MAX_PAGES_TO_FETCH = 3;

    private static final String POPULARITY_TAG_TITLE = NetworkUtils.POPULARITY_TAG_TITLE;
    private static final String TOP_RATED_TAG_TITLE = NetworkUtils.TOP_RATED_TAG_TITLE;
    private static final int POPULAR_MOVIES_TAG = 0;
    private static final int TOP_RATED_MOVIES_TAG = 1;
    private static final String DISPLAYED_LIST_TAG = "displayed_list";

    private MovieAdapter movieAdapter;
    private boolean isFetchingData;

    private ActivityMainBinding binding;

    private OkHttpClient client;
    private Request request;
    private Toast mToast;
    Retrofit retrofit;
    Call<TMDResponse> call;
    TheMovieDatabaseAPI theMovieDatabaseAPI;
    MainViewModel viewModel;
    private int displayedList = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        Timber.d("OnCreate");

        RecyclerView recyclerView = binding.rvMovies;
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(this, 1);
        recyclerView.setLayoutManager(myGridLayoutManager);

//        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory(getApplication());
//        viewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.setOnResponseListener(this);
        if (savedInstanceState != null) {
            Timber.d("savedInstanceState.DisplayedListTag: %d " , savedInstanceState.getInt(DISPLAYED_LIST_TAG));
            displayedList = savedInstanceState.getInt(DISPLAYED_LIST_TAG);
        }
        switch (displayedList) {
            case TOP_RATED_MOVIES_TAG:
                loadTopRatedMovies();
                break;
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
        viewModel.loadTopRatedMovies();
        displayedList = TOP_RATED_MOVIES_TAG;
    }

    private void loadPopularMovies() {
        viewModel.loadPopularMovies();
        displayedList = POPULAR_MOVIES_TAG;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DISPLAYED_LIST_TAG, displayedList);
//        Timber.d("onSaveState.DisplayedListTag: %d" , displayedList);
    }

    /**
     * get data from THE MOVIE DATABASE and update UI accordingly
     *
     * @param sortByTag choose between popular and top rated movies
     */
    private void getDataAndUpdateUI(String sortByTag) {
        if (!isFetchingData) {
            updateUIOnLoading();
            if (sortByTag.equals(POPULARITY_TAG_TITLE)) {
                loadPopularMovies();
            } else {
                loadTopRatedMovies();
            }
        } else {
            showBusyToast();
            return;
        }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_main, menu);
        Timber.d("onCreateOptionsMenu()");
        if(displayedList==TOP_RATED_MOVIES_TAG){
            menu.getItem(0).setTitle(POPULARITY_TAG_TITLE);
        } else {
            menu.getItem(0).setTitle(TOP_RATED_TAG_TITLE);
        }
        menu.findItem(R.id.sort_by_action).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //if fetching data already, don't do anything
                if (isFetchingData) {
                    showBusyToast();
                    return true;
                }

                String newItemTitle = null;
                String sortBy = null;
                if (displayedList == TOP_RATED_MOVIES_TAG) {
                    newItemTitle = TOP_RATED_TAG_TITLE;
                    sortBy = POPULARITY_TAG_TITLE;
                } else {
                    newItemTitle = POPULARITY_TAG_TITLE;
                    sortBy = TOP_RATED_TAG_TITLE;
                }
                getDataAndUpdateUI(sortBy);
                item.setTitle(newItemTitle);
                return true;
            }
        });

        return true;
    }

    private void showBusyToast() {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(MainActivity.this, "Downloading data in progress, try again later", Toast.LENGTH_SHORT);
        mToast.show();
    }


    private void updateSortByTitleTV() {
        if (displayedList== TOP_RATED_MOVIES_TAG) {
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

    public void cancelRequest(View view) {
        if (call != null) {
            call.cancel();
            updateUIOnFailure();
            Toast.makeText(this, "Request cancelled", Toast.LENGTH_LONG).show();
        }
    }

}
