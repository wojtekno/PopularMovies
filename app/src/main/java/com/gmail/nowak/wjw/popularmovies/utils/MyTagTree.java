package com.gmail.nowak.wjw.popularmovies.utils;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class MyTagTree extends Timber.DebugTree {

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        String myTag = "MY_TAG_".concat(tag);
        super.log(priority, myTag, message, t);
    }
}
