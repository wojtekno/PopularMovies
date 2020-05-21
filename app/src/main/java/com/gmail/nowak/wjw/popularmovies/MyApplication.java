package com.gmail.nowak.wjw.popularmovies;

import android.app.Application;

import com.gmail.nowak.wjw.popularmovies.utils.MyTagDebugTree;

import timber.log.Timber;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //todo implement prod and debug logging
        Timber.plant(new MyTagDebugTree());
//        Timber.d("Timber initialized");
    }
}
