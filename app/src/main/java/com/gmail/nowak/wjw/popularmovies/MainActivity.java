package com.gmail.nowak.wjw.popularmovies;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.nowak.wjw.popularmovies.databinding.ActivityMainAlternativeBinding;
import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;
import com.gmail.nowak.wjw.popularmovies.utils.TMDUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MAX_PAGES_TO_FETCH = 3;
    private MovieAdapter movieAdapter;
    private int mTMDPage;

    ActivityMainAlternativeBinding binding;


    private String[] mockupDataSet = {"Shrek", "Matrix", "Tangled", "Frozen", "Frozen II", "Ice Age", "The Greatest Showmen", "Avatar"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alternative);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_alternative);


        RecyclerView recyclerView = binding.rvMovies;
        recyclerView.setHasFixedSize(true);


        movieAdapter = new MovieAdapter();

        recyclerView.setAdapter(movieAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(this, 1);
        recyclerView.setLayoutManager(myGridLayoutManager);

        // fetch data starting from page 1
        mTMDPage = 1;
        URL defaultURL = NetworkUtils.buildUrl(NetworkUtils.POPULARITY_TAG_TITLE, mTMDPage);
        getResponseFromTMD(defaultURL);
        binding.sortByTitleTv.setText(NetworkUtils.POPULARITY_TAG_TITLE);


    }

    private void getResponseFromTMD(URL url) {
        if (mTMDPage > MAX_PAGES_TO_FETCH) {
//            binding.sortByTitleTv.append(String.format(" (%d)", movieAdapter.getItemCount()));
            mTMDPage = 1;
            return;
        }


        if (mTMDPage == 1) {
            switchSortByTitle();
            binding.mainProgressBarr.setVisibility(View.VISIBLE);

            binding.errorMessageTv.setVisibility(View.GONE);
        }

        Log.d(LOG_TAG, "getResponseFromTMD with URL:\n " + url.toString());
        Log.d(LOG_TAG, "getResponseFromTMD mTMDPAGE: " + mTMDPage);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.mainProgressBarr.setVisibility(View.GONE);
                        binding.sortByTitleTv.setVisibility(View.GONE);
                        if (movieAdapter.getItemCount() > 0) {
                            binding.reloadErrorMessageTv.setVisibility(View.VISIBLE);
                        } else {
                            binding.errorMessageTv.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

            //TODO FIX - progressbar doesnt show up consistently, doesnt load consistenlyt;
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mTMDPage == 1) {
                            movieAdapter.clearMoviesData();
                        }
                        binding.errorMessageTv.setVisibility(View.GONE);
                        binding.mainProgressBarr.setVisibility(View.GONE);
                        binding.reloadErrorMessageTv.setVisibility(View.GONE);
                        binding.sortByTitleTv.setVisibility(View.VISIBLE);

                        movieAdapter.setMoviesData(TMDUtils.parseJSONToMovieDTO(myResponse));
                        movieAdapter.notifyDataSetChanged();
                        mTMDPage++;
                        if (mTMDPage > MAX_PAGES_TO_FETCH) {
                            mTMDPage=1;
                            return;
                        }
                        getResponseFromTMD(NetworkUtils.buildUrl(NetworkUtils.POPULARITY_TAG_TITLE, mTMDPage));
                    }
                });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle(NetworkUtils.TOP_RATED_TAG_TITLE);
        menu.findItem(R.id.sort_by_action).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String newItemTitle = null;
                String sortBy = null;
                if (item.getTitle().equals(NetworkUtils.POPULARITY_TAG_TITLE)) {
                    newItemTitle = NetworkUtils.TOP_RATED_TAG_TITLE;
                    sortBy = NetworkUtils.POPULARITY_TAG_TITLE;
                } else {
                    newItemTitle = NetworkUtils.POPULARITY_TAG_TITLE;
                    sortBy = NetworkUtils.TOP_RATED_TAG_TITLE;
                }
                getResponseFromTMD(NetworkUtils.buildUrl(sortBy, 1));
                item.setTitle(newItemTitle);
//                binding.sortByTitleTv.setText(sortBy);
                return true;
            }
        });

        return true;
    }


    private void switchSortByTitle() {
        if (binding.sortByTitleTv.getText().equals(NetworkUtils.POPULARITY_TAG_TITLE)) {
            binding.sortByTitleTv.setText(NetworkUtils.TOP_RATED_TAG_TITLE);
        } else {
            binding.sortByTitleTv.setText(NetworkUtils.POPULARITY_TAG_TITLE);
        }

    }
}
