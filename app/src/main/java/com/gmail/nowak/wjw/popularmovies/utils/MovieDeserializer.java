package com.gmail.nowak.wjw.popularmovies.utils;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import timber.log.Timber;

public class MovieDeserializer {} /* implements JsonDeserializer<ApiMovie> {
    @Override
    public ApiMovie deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if(json.getAsJsonObject().has("videos")){
                    JsonElement videosElement = json.getAsJsonObject().getAsJsonObject("videos");
                    Timber.d("videosElement = %s" , videosElement==null?"null": videosElement.toString());
                }
        return null;
    }
}
*/