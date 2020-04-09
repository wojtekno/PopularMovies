package com.gmail.nowak.wjw.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gmail.nowak.wjw.popularmovies.utils.NetworkUtils;

import android.widget.TextView;

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

    private MovieAdapter movieAdapter;
    private TextView testTV;
    private MovieDTO[] moviesData;
    private int mTMDPage;

    private String[] mockupDataSet = {"Shrek", "Matrix", "Tangled", "Frozen", "Frozen II", "Ice Age", "The Greatest Showmen", "Avatar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alternative);

        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setHasFixedSize(true);

        testTV = findViewById(R.id.test_tv);
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(this, 1);
        recyclerView.setLayoutManager(myGridLayoutManager);

        // fetch data starting from page 1
        mTMDPage = 1;
        getResponseFromTMD(NetworkUtils.buildUrl("popular", mTMDPage));

    }

    private void getResponseFromTMD(URL url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        mTMDPage++;

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();


                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        testTV.setText("FetchDone");
                        movieAdapter.setMoviesData(TMDUtils.parseJSONToMovieDTO(myResponse));
                        movieAdapter.notifyDataSetChanged();
                        if (mTMDPage <= 3) {
                            getResponseFromTMD(NetworkUtils.buildUrl("popular", mTMDPage));
                        }

                    }
                });

            }
        });

    }
}
