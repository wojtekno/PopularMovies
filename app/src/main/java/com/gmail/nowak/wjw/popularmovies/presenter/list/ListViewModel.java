package com.gmail.nowak.wjw.popularmovies.presenter.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieListsUseCase;
import com.gmail.nowak.wjw.popularmovies.navigation.Nav;
import com.gmail.nowak.wjw.popularmovies.presenter.list.pager.ListPagerFragmentDirections;

import java.util.List;

import timber.log.Timber;

public class ListViewModel extends ViewModel {
    private GetMovieListsUseCase mGetMovieListsUseCase;
    private LiveData<List<MovieListItemViewData>> mMovieListLd;
    private LiveData<Integer> mErrorMessageResIdLd;
    private Nav mNav;
    public MutableLiveData<Boolean> isErrMsgVisible;


    public MutableLiveData<Boolean> isProgressBarVisible;

    public ListViewModel(GetMovieListsUseCase getMovieListsUseCase, Nav nav) {
        Timber.d("ListViewModel::newInstance");
        mNav = nav;
        mGetMovieListsUseCase = getMovieListsUseCase;
        mErrorMessageResIdLd = getMovieListsUseCase.getErrorMessageResId();
        mMovieListLd = getMovieListsUseCase.getMovieList();

        isErrMsgVisible = (MutableLiveData<Boolean>) Transformations.map(mErrorMessageResIdLd, (error) ->
        {
            if (error == null) return false;
            else return true;
        });

        // TODO: 6 Q? I modify it here in viewModel, but I would like to call ie loadingFinished() from fragment - what is better/cleaner?
        isProgressBarVisible = (MutableLiveData<Boolean>) Transformations.map(mMovieListLd, (x) -> false);
        isProgressBarVisible.setValue(true);
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


//    //todo remove used only in tests
//    public void refreshList() {
////        Timber.d("refreshList");
//        isErrMsgVisible.setValue(false);
//        isProgressBarVisible.setValue(true);
//        mGetMovieListsUseCase.refreshMovieList(mListTagLd.getValue());
//    }
//
//    //todo remove used only in tests
//    public void listTagChanged(ListTag listTag) {
//        isErrMsgVisible.setValue(false);
//        isProgressBarVisible.setValue(true);
//        mListTagLd.setValue(listTag);
//        if (mErrorMessageResIdLd.getValue() != null) {
//            refreshList();
//        }
//    }
//
//    //todo remove used only in tests
//    public LiveData<ListTag> getListTag() {
//        return mListTagLd;
//    }

}
