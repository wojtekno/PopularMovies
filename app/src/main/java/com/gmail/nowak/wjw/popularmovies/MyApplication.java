package com.gmail.nowak.wjw.popularmovies;

import android.app.Application;

import com.gmail.nowak.wjw.popularmovies.utils.MyTagTree;

import timber.log.Timber;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new MyTagTree());
//        Timber.d("Timber initialized");
    }
}
