package com.gmail.nowak.wjw.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
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

    MovieAdapter movieAdapter;
    String[] mockupDataSet = {"Shrek", "Matrix", "Tangled", "Frozen", "Frozen II", "Ice Age", "The Greatest Showmen", "Avatar"};
    TextView testTV;
    MovieDTO[] moviesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alternative);

//        mockupDataSet


        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setHasFixedSize(true);

        testTV = findViewById(R.id.test_tv);
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NetworkUtils.buildUrl("popular");
        getResponseFromTMD(NetworkUtils.buildUrl("popular"));

    }

    private void getResponseFromTMD(URL url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

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

                    }
                });

            }
        });

    }
}
