package com.gmail.nowak.wjw.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.databinding.ActivityMainBinding;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;
import com.gmail.nowak.wjw.popularmovies.utils.TMDUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnRecyclerItemClickListener {

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

        //fetch data
        getDataAndUpdateUI(POPULARITY_TAG_TITLE);
    }

    /**
     * get data from THE MOVIE DATABASE and update UI accordingly
     *
     * @param sortByTag choose between popular and top rated movies
     */
    private void getDataAndUpdateUI(String sortByTag) {
        if (!isFetchingData) {
            binding.mainProgressBarr.setVisibility(View.VISIBLE);
            binding.errorMessageTv.setVisibility(View.GONE);
            fetchDataFromTMD(sortByTag, null);
        } else {
            showBusyToast();
        }

    }

    /**
     * Fetch data from THE MOVIE DATABASE, and update UI
     *
     * @param sortByTag choose between popular and top rated movies
     * @param page      page to be fetched
     */
    private void fetchDataFromTMD(final String sortByTag, @Nullable Integer page) {
        isFetchingData = true;
        final int[] mCounter = {0};
        if (page != null) {
            mCounter[0] = page;
        }
        int mPage = mCounter[0] + 1;

        if (client == null) {
            client = new OkHttpClient();
        }

        URL mURL = NetworkUtils.buildTMDApiUrl(sortByTag, mPage);
        request = new Request.Builder()
                .url(mURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUIOnFailure();
                        isFetchingData = false;
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCounter[0] == 0) {
                            movieAdapter.clearMoviesData();
                            updateSortByTitleTV();
                        }
                        movieAdapter.setMoviesData(TMDUtils.parseJSONToMovieDTO(myResponse));
                        movieAdapter.notifyDataSetChanged();
                        updateUIOnResponse();
                        mCounter[0]++;
                        if (mCounter[0] < MAX_PAGES_TO_FETCH) {
                            fetchDataFromTMD(sortByTag, mCounter[0]);
                        } else {
                            isFetchingData = false;
                        }
                    }
                });

            }
        });
    }

    private void updateUIOnResponse() {
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
}
