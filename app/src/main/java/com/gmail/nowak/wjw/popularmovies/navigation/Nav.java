package com.gmail.nowak.wjw.popularmovies.navigation;

import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDirections;

public class Nav {

    private MutableLiveData<Event<NavDirections>> mNavDirectionsEvent = new MutableLiveData<>();

    public MutableLiveData<Event<NavDirections>> getNavDirectionsEvent() {
        return mNavDirectionsEvent;
    }

    public void setNavDirections(NavDirections navDirections) {
        this.mNavDirectionsEvent.setValue(new Event<>(navDirections));
    }

}
