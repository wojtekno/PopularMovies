package com.gmail.nowak.wjw.popularmovies.utils;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class TMDUtils {

    private static final String LOG_TAG = TMDUtils.class.getSimpleName();

    public static List<ApiMovie> parseJSONToMovieDTO(String jsonResponse) {
        Gson gson = new Gson();
        JSONObject mJSONResponse;
        ApiMovie[] moviesArray = null;
        try {
            mJSONResponse = new JSONObject(jsonResponse);
            JSONArray jsonArray = mJSONResponse.optJSONArray("results");
            if (jsonArray != null && jsonArray.length() != 0) {
                moviesArray = gson.fromJson(jsonArray.toString(), ApiMovie[].class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Arrays.asList(moviesArray);
    }
}
