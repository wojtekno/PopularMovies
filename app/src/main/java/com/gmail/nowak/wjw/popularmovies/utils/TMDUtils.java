package com.gmail.nowak.wjw.popularmovies.utils;

import com.gmail.nowak.wjw.popularmovies.data.model.MovieVM;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class TMDUtils {

    private static final String LOG_TAG = TMDUtils.class.getSimpleName();

    public static List<MovieVM> parseJSONToMovieDTO(String jsonResponse) {
        Gson gson = new Gson();
        JSONObject mJSONResponse;
        MovieVM[] moviesArray = null;
        try {
            mJSONResponse = new JSONObject(jsonResponse);
            JSONArray jsonArray = mJSONResponse.optJSONArray("results");
            if (jsonArray != null && jsonArray.length() != 0) {
                moviesArray = gson.fromJson(jsonArray.toString(), MovieVM[].class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Arrays.asList(moviesArray);
    }
}
