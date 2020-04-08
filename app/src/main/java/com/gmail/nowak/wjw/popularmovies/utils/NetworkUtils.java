package com.gmail.nowak.wjw.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Request;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String THE_MOVIE_DATABASE_API_BASE_URL = "https://api.themoviedb.org/3";
    private static final String TMD_API_DISCOVER_PATH = "discover";
    private static final String TMD_API_MOVIE_PATH = "movie";

    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATE_DESC = "vote_average.desc";


    static final String API_KEY_PARAM = "api_key";
    static final String QUERY_PARAM = "q";
    static final String SORT_BY_PARAM = "sort_by";


    public static URL buildUrl(String filter) {
        String sortByValue = POPULARITY_DESC;
        if (filter.equals("top_rated")) {
            sortByValue = RATE_DESC;
        }

        Uri mUri = Uri.parse(THE_MOVIE_DATABASE_API_BASE_URL).buildUpon()
                .appendPath(TMD_API_DISCOVER_PATH)
                .appendPath(TMD_API_MOVIE_PATH)
                .appendQueryParameter(SORT_BY_PARAM, sortByValue)
                .appendQueryParameter(API_KEY_PARAM, PrivateApiKeyUtils.TMD_API_KEY_VALUE).build();

        URL mURL = null;

        try {
            mURL = new URL(mUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "URL: " + mURL.toString());
        return mURL;
    }



    public static Request getResponseFromTMD(URL url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return request;
    }

}
