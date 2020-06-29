package com.gmail.nowak.wjw.popularmovies.navigation;

import timber.log.Timber;

public class Event<T> {

    private boolean hasBeenHandled = false;
    private T mContent;

    public Event(T content) {
        mContent = content;
    }

    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            Timber.d("handled before");
            return null;
        } else {
//            Timber.d("handled for the first time");
            hasBeenHandled = true;
            return mContent;
        }
    }

    public T peekContent() {
        return mContent;
    }

    public boolean hasBeenHandled() {
        return hasBeenHandled;
    }
}
