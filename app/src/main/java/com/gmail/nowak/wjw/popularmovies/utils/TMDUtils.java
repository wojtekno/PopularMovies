package com.gmail.nowak.wjw.popularmovies.utils;

import android.util.Log;

import com.gmail.nowak.wjw.popularmovies.MovieDTO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class TMDUtils {

    private static final String LOG_TAG = TMDUtils.class.getSimpleName();

    public static List<MovieDTO> parseJSONToMovieDTO(String jsonResponse) {
        Gson gson = new Gson();
        MovieDTO movieDTO;
        JSONObject mJSONResponse;
        MovieDTO[] moviesArray = null;
        try {
            mJSONResponse = new JSONObject(jsonResponse);
            JSONArray jsonArray = mJSONResponse.optJSONArray("results");
            if(jsonArray!= null && jsonArray.length() != 0){
                moviesArray = gson.fromJson(jsonArray.toString(), MovieDTO[].class);
                //TODO delete below line
//                Log.d(LOG_TAG, moviesArray[3].toString());
            }

//            String movieString = jsonArray.get(0).toString();
//            movieDTO = gson.fromJson(movieString, MovieDTO.class);
//            Log.d(LOG_TAG, movieString);
//
//            Log.d(LOG_TAG, movieDTO.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Arrays.asList(moviesArray);
    }

    public static String[] getPostersFromJSONResponse(String jsonResponse) {

        return null;
    }
}
