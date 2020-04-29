package com.gmail.nowak.wjw.popularmovies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MainViewModel.OnResponseListener mOnResponseListener;
    private Application mApplication;
    public MainViewModelFactory(Application application, MainViewModel.OnResponseListener onResponseListener){
        mOnResponseListener = onResponseListener;
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return super.create(modelClass);
        return (T) new MainViewModel(mApplication, mOnResponseListener);
    }
//    public <T extends ViewModel> T create
}
