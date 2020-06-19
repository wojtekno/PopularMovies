package com.gmail.nowak.wjw.popularmovies.presenter.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieListsUseCase;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

import java.util.List;

import timber.log.Timber;

public class ListViewModel extends ViewModel {
    private GetMovieListsUseCase mGetMovieListsUseCase;
    private LiveData<List<MovieListItemViewData>> mMovieListLd;
    private LiveData<Integer> mErrorMessageResIdLd;
    private MutableLiveData<ListTag> mListTagLd = new MutableLiveData<>(ListTag.POPULAR);
    public MutableLiveData<Boolean> isErrMsgVisible;
    public MutableLiveData<Boolean> isProgressBarVisible;


    public ListViewModel(GetMovieListsUseCase getMovieListsUseCase) {
        Timber.d("ListViewModel::newInstance");
        this.mGetMovieListsUseCase = getMovieListsUseCase;

        mErrorMessageResIdLd = Transformations.switchMap(mListTagLd, (mTag) -> {
            switch (mTag) {
                case POPULAR:
                    return mGetMovieListsUseCase.getPopularResponseCode();
                case TOP_RATED:
                    return mGetMovieListsUseCase.getTopRatedResponseCode();
                case FAVOURITE:
                    return new MutableLiveData<>(null);
                default:
                    throw new RuntimeException();
            }
        });

        mMovieListLd = Transformations.switchMap(mListTagLd, (mTag) -> {
            switch (mTag) {
                case POPULAR:
                    return getMovieListsUseCase.getPopularMovieList();
                case TOP_RATED:
                    return getMovieListsUseCase.getTopRatedMovieList();
                case FAVOURITE:
                    return getMovieListsUseCase.getFavouriteMovieList();
                default:
                    throw new RuntimeException();
            }
        });

        isErrMsgVisible = (MutableLiveData<Boolean>) Transformations.map(mErrorMessageResIdLd, (error) -> {
            if (error == null) return false;
            else return true;
        });

        isProgressBarVisible = (MutableLiveData<Boolean>) Transformations.map(mMovieListLd, (x) -> false);
        isProgressBarVisible.setValue(true);
    }


    public LiveData<List<MovieListItemViewData>> getMovieList() {
        return mMovieListLd;
    }

    public LiveData<Integer> getErrorResId() {
        return mErrorMessageResIdLd;
    }

    public void refreshList() {
//        Timber.d("refreshList");
        isErrMsgVisible.setValue(false);
        isProgressBarVisible.setValue(true);
        mGetMovieListsUseCase.refreshMovieList(mListTagLd.getValue());
    }

    public void listTagChanged(ListTag listTag) {
        isErrMsgVisible.setValue(false);
        isProgressBarVisible.setValue(true);
        mListTagLd.setValue(listTag);
        if (mErrorMessageResIdLd.getValue() != null) {
            refreshList();
        }
    }

    public LiveData<ListTag> getListTag() {
        return mListTagLd;
    }


    @Override
    protected void onCleared() {
        Timber.d("OnCleared");
        super.onCleared();
    }
}
