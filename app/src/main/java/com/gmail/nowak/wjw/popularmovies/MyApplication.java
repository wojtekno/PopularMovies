package com.gmail.nowak.wjw.popularmovies;

import android.app.Application;

import com.gmail.nowak.wjw.popularmovies.di.AppContainer;
import com.gmail.nowak.wjw.popularmovies.utils.MyTagDebugTree;

import timber.log.Timber;

public class MyApplication extends Application {

    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new MyTagDebugTree());
//        Timber.d("Timber initialized");
        }

        appContainer = new AppContainer(this);
    }
}
