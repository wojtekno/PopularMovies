package com.gmail.nowak.wjw.popularmovies.network;

import com.gmail.nowak.wjw.popularmovies.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class HttpClientFactory {

    public OkHttpClient create() {
        Timber.d("Creating OkHttpClient");
        OkHttpClient.Builder httpClientBuilder =
                new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                HttpUrl originalHttpUrl = original.url();

                HttpUrl newUrl = originalHttpUrl.newBuilder()
                        .addQueryParameter(NetworkUtils.API_KEY_PARAM, BuildConfig.TMD_API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(newUrl);

                Request newRequest = requestBuilder.build();

                return chain.proceed(newRequest);
            }
        });

        if (BuildConfig.DEBUG) {
            Timber.d("adding NetworkInterceptor");
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addNetworkInterceptor(loggingInterceptor);
        }


        return httpClientBuilder.build();
    }
}
