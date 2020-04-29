package com.gmail.nowak.wjw.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.databinding.ActivityMainBinding;
import com.gmail.nowak.wjw.popularmovies.network.TMDResponse;
import com.gmail.nowak.wjw.popularmovies.network.TheMovieDatabaseAPI;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;
import com.gmail.nowak.wjw.popularmovies.utils.TMDUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnRecyclerItemClickListener, MainViewModel.OnResponseListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MAX_PAGES_TO_FETCH = 3;

    private static final String POPULARITY_TAG_TITLE = NetworkUtils.POPULARITY_TAG_TITLE;
    private static final String TOP_RATED_TAG_TITLE = NetworkUtils.TOP_RATED_TAG_TITLE;

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

        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory(getApplication(), this);

        viewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);
//        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.setOnResponseListener(this);
        viewModel.getPopularMovies().observe(this, new Observer<List<MovieDTO>>() {
            @Override
            public void onChanged(List<MovieDTO> movieDTOS) {
                Log.d("MainActivity", "LiveData onChange");
                movieAdapter.clearMoviesData();
                movieAdapter.setMoviesData(movieDTOS);
                movieAdapter.notifyDataSetChanged();
//                updateUIOnResponse();

            }
        });
    }

    /**
     * get data from THE MOVIE DATABASE and update UI accordingly
     *
     * @param sortByTag choose between popular and top rated movies
     */
    private void getDataAndUpdateUI(String sortByTag) {
        if (!isFetchingData) {
            updateUIOnLoading();
//            viewModel.fetchDataWithRetrofit(sortByTag,null);
//            fetchDataFromTMD(sortByTag, null);
//            fetchDataWithRetrofit(sortByTag, null);
            if (sortByTag.equals(POPULARITY_TAG_TITLE)) {
                viewModel.getPopularMovies();
            } else {
                viewModel.getTopRatedMovies();
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
        menu.getItem(0).setTitle(TOP_RATED_TAG_TITLE);
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
                if (item.getTitle().equals(POPULARITY_TAG_TITLE)) {
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
        if (binding.sortByTitleTv.getText().equals(POPULARITY_TAG_TITLE)) {
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

    @Override
    public void onResponse(boolean isResponseOK, int size) {
        if (isResponseOK) {
            Log.d("MainActivity.onResponse", "onResponseTriggered");
            binding.countTV.append(String.format("(%d)", size));
            updateUIOnResponse();
        } else {
            updateUIOnFailure();
        }
    }
}
