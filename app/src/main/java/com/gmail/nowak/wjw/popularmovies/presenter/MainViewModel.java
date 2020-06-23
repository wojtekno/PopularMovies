package com.gmail.nowak.wjw.popularmovies.presenter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import timber.log.Timber;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Integer> selectedApiId = new MutableLiveData<>();

    public MainViewModel() {
        Timber.d("MainViewModel::newInstance");
    }

    public LiveData<Integer> getSelectedApiId() {
        return selectedApiId;
    }

    public void setSelectedApiId(int apiId) {
        selectedApiId.setValue(apiId);
    }
}
