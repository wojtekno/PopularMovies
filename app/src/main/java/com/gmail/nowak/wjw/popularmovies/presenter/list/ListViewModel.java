package com.gmail.nowak.wjw.popularmovies.presenter.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieListsUseCase;
import com.gmail.nowak.wjw.popularmovies.navigation.Nav;
import com.gmail.nowak.wjw.popularmovies.presenter.list.pager.ListPagerFragmentDirections;

import java.util.List;

import timber.log.Timber;

public class ListViewModel extends ViewModel {
    private GetMovieListsUseCase mGetMovieListsUseCase;
    public LiveData<PagedList<MovieListItemViewData>> pagedList;
    private LiveData<List<MovieListItemViewData>> mMovieListLd;
    private LiveData<Integer> mErrorMessageResIdLd;
    private Nav mNav;
    public MutableLiveData<Boolean> isErrMsgVisible;
    public MutableLiveData<Boolean> isProgressBarVisible = new MutableLiveData<>(true);

    public ListViewModel(GetMovieListsUseCase getMovieListsUseCase, Nav nav) {
        Timber.d("ListViewModel::newInstance");
        mNav = nav;
        mGetMovieListsUseCase = getMovieListsUseCase;
        pagedList = mGetMovieListsUseCase.pagedItems;
//        mErrorMessageResIdLd = getMovieListsUseCase.getErrorMessageResId();
//        mMovieListLd = getMovieListsUseCase.getMovieList();
        isErrMsgVisible = new MutableLiveData<>(false);
//        isErrMsgVisible = (MutableLiveData<Boolean>) Transformations.map(mErrorMessageResIdLd, (error) ->
//        {
//            if (error == null) return false;
//            else return true;
//        });
    }


    public LiveData<List<MovieListItemViewData>> getMovieList() {
        return mMovieListLd;
    }

    public LiveData<Integer> getErrorResId() {
        return mErrorMessageResIdLd;
    }

    public void movieItemClicked(int position) {
        ListPagerFragmentDirections.ActionListPagerFragmentToDetailPagerFragment action = ListPagerFragmentDirections.actionListPagerFragmentToDetailPagerFragment(getApiIdArray(), position);
        mNav.setNavDirections(action);
    }

    @Override
    protected void onCleared() {
        Timber.d("onCleared()");
        super.onCleared();
    }

    private int[] getApiIdArray() {
        int[] mIds = new int[mMovieListLd.getValue().size()];
        List<MovieListItemViewData> value = mMovieListLd.getValue();
        for (int i = 0; i < value.size(); i++) {
            MovieListItemViewData movie = value.get(i);
            mIds[i] = movie.getApiId();
        }
        return mIds;
    }

    public void refreshList() {
        Timber.d("refreshList");
        isErrMsgVisible.setValue(false);
        isProgressBarVisible.setValue(true);
        if (!mGetMovieListsUseCase.refreshList()) {
            finishedLoading();
        }
    }

    public void finishedLoading() {
        isProgressBarVisible.setValue(false);
    }

}
