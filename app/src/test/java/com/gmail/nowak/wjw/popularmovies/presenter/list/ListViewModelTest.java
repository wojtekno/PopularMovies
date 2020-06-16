package com.gmail.nowak.wjw.popularmovies.presenter.list;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.gmail.nowak.wjw.popularmovies.R;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.domain.GetMovieListsUseCase;
import com.gmail.nowak.wjw.popularmovies.presenter.ListTag;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListViewModelTest {

    @Rule
    public TestRule testRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private GetMovieListsUseCase useCase;
    @Mock
    private Observer<Integer> mErrorObserver;
    @Mock
    private Observer<Boolean> mIsErrMsgVisibleObserver;
    @Mock
    private Observer<Boolean> mIsProgressBarVisibleObserver;
    @Mock
    private Observer<ListTag> mListTagObserver;

    private ListViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        when(useCase.getPopularMovieList()).thenReturn(new MutableLiveData<>(popularMockData()));
        when(useCase.getPopularResponseCode()).thenReturn(new MutableLiveData<>(200));
        viewModel = new ListViewModel(useCase);
        viewModel.getErrorResId().observeForever(mErrorObserver);
        viewModel.isErrMsgVisible.observeForever(mIsErrMsgVisibleObserver);
        viewModel.isProgressBarVisible.observeForever(mIsProgressBarVisibleObserver);
        viewModel.getListTag().observeForever(mListTagObserver);
    }

    @Test
    public void testGetMovieList() {
        List<MovieListItemViewData> list = viewModel.getMovieList().getValue();
        int size = list.size();
        assertEquals(25, size);
        for (int i = 0; i < size; i++) {
            assertEquals(i, list.get(i).getApiId());
            assertEquals("popular_title_of_" + i, list.get(i).getOriginalTitle());
            assertEquals("popular_poster_path_" + i, list.get(i).getImagePath());
        }
    }

    @Test
    public void testGetMovieListAfterListTabChanged() {
        when(useCase.getTopRatedMovieList()).thenReturn(new MutableLiveData<>(topRatedMockData()));
        String lll = viewModel.getListTag().getValue().name();
        viewModel.listTagChanged(ListTag.TOP_RATED);
        List<MovieListItemViewData> list = viewModel.getMovieList().getValue();
        int size = list.size();
        assertEquals(30, size);
        for (int i = 0; i < size; i++) {
            assertEquals(i, list.get(i).getApiId());
            assertEquals("top_rated_title_of_" + i, list.get(i).getOriginalTitle());
            assertEquals("top_rated_poster_path_" + i, list.get(i).getImagePath());
        }
    }

    @Test
    public void testGetMovieListWhenPreviouslyThereWasAnErrorAfterListTabChanged() {
        when(useCase.getPopularMovieList()).thenReturn(new MutableLiveData<>(new ArrayList<>()));
        when(useCase.getPopularResponseCode()).thenReturn(new MutableLiveData<>(400));
        when(useCase.getTopRatedMovieList()).thenReturn(new MutableLiveData<>(topRatedMockData()));
        when(useCase.getTopRatedResponseCode()).thenReturn(new MutableLiveData<>(200));

        viewModel = new ListViewModel(useCase);
        viewModel.getErrorResId().observeForever(mErrorObserver);
        viewModel.isErrMsgVisible.observeForever(mIsErrMsgVisibleObserver);
        viewModel.isProgressBarVisible.observeForever(mIsProgressBarVisibleObserver);
        viewModel.getListTag().observeForever(mListTagObserver);
        List<MovieListItemViewData> list = viewModel.getMovieList().getValue();
        assertEquals(0, list.size());
        viewModel.listTagChanged(ListTag.TOP_RATED);
        list = viewModel.getMovieList().getValue();
        assertEquals(30, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(i, list.get(i).getApiId());
            assertEquals("top_rated_title_of_" + i, list.get(i).getOriginalTitle());
            assertEquals("top_rated_poster_path_" + i, list.get(i).getImagePath());
        }
        viewModel.listTagChanged(ListTag.POPULAR);
        verify(useCase).refreshMovieList(ListTag.POPULAR);
    }


    @Test
    public void testGetErrorResIdWhenCode_200() {
        assertEquals(null, viewModel.getErrorResId().getValue());
    }

    @Test
    public void testGetErrorResIdWhenCode_999() {
        when(useCase.getPopularResponseCode()).thenReturn(new MutableLiveData<>(999));
        viewModel = new ListViewModel(useCase);
        viewModel.getErrorResId().observeForever(mErrorObserver);
        assertEquals(R.string.error_cannot_connect_with_server, viewModel.getErrorResId().getValue().intValue());
    }

    @Test
    public void testGetErrorResIdWhenCode_OtherCode() {
        when(useCase.getPopularResponseCode()).thenReturn(new MutableLiveData<>(400));
        viewModel = new ListViewModel(useCase);
        viewModel.getErrorResId().observeForever(mErrorObserver);
        assertEquals(R.string.error_problem_with_response, viewModel.getErrorResId().getValue().intValue());
    }

    @Test
    public void testGetErrorResIdAfterListTagChangedWhenCode_OtherCode() {
        when(useCase.getPopularResponseCode()).thenReturn(new MutableLiveData<>(999));
        viewModel = new ListViewModel(useCase);
        viewModel.getErrorResId().observeForever(mErrorObserver);
        assertEquals(R.string.error_cannot_connect_with_server, viewModel.getErrorResId().getValue().intValue());
        when(useCase.getTopRatedResponseCode()).thenReturn(new MutableLiveData<>(409));
        viewModel.listTagChanged(ListTag.TOP_RATED);
        assertEquals(R.string.error_problem_with_response, viewModel.getErrorResId().getValue().intValue());

    }

    @Test
    public void testIsErrMsgVisibleWhenCode_200() {
        assertEquals(false, viewModel.isErrMsgVisible.getValue());
    }

    @Test
    public void testIsErrMsgVisibleWhenCodeNot_200() {
        when(useCase.getPopularResponseCode()).thenReturn(new MutableLiveData<>(999));
        viewModel = new ListViewModel(useCase);
        viewModel.isErrMsgVisible.observeForever(mIsErrMsgVisibleObserver);
        assertEquals(true, viewModel.isErrMsgVisible.getValue());
    }

    @Test
    public void testIsErrMsgVisibleWhenCodeNot_200_AfterReloading() {
        when(useCase.getPopularResponseCode()).thenReturn(new MutableLiveData<>(999));
        viewModel = new ListViewModel(useCase);
        viewModel.isErrMsgVisible.observeForever(mIsErrMsgVisibleObserver);
        assertEquals(true, viewModel.isErrMsgVisible.getValue());
        viewModel.refreshList();
        assertEquals(false, viewModel.isErrMsgVisible.getValue());
    }

    @Test
    public void testIsErrMsgVisibleWhenCodeNot_200_AfterListTagChanged() {
        when(useCase.getPopularResponseCode()).thenReturn(new MutableLiveData<>(999));
        viewModel = new ListViewModel(useCase);
        viewModel.isErrMsgVisible.observeForever(mIsErrMsgVisibleObserver);
        assertEquals(true, viewModel.isErrMsgVisible.getValue());
        viewModel.listTagChanged(ListTag.TOP_RATED);
        assertEquals(false, viewModel.isErrMsgVisible.getValue());
    }

    @Test
    public void testIsProgressBarVisibleAfterReload() {
        viewModel.refreshList();
        assertEquals(true, viewModel.isProgressBarVisible.getValue());
    }

    @Test
    public void testIsProgressBarVisibleAfterListTagChanged() {
        viewModel.listTagChanged(ListTag.TOP_RATED);
        assertEquals(true, viewModel.isProgressBarVisible.getValue());
    }

    @Test
    public void testIsProgressBarVisibleAfterListTagChangedWhenNeedToFetcgAgain() {
        viewModel.listTagChanged(ListTag.TOP_RATED);
        assertEquals(true, viewModel.isProgressBarVisible.getValue());
    }

    @Test
    public void testListTagChanged() {
        when(useCase.getTopRatedMovieList()).thenReturn(new MutableLiveData<>(topRatedMockData()));
        when(useCase.getTopRatedResponseCode()).thenReturn(new MutableLiveData<>(200));
        viewModel.listTagChanged(ListTag.TOP_RATED);
        assertEquals(ListTag.TOP_RATED, viewModel.getListTag().getValue());
    }


    private List<MovieListItemViewData> popularMockData() {
        List<MovieListItemViewData> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            MovieListItemViewData item = new MovieListItemViewData(i, "popular_title_of_" + i, "popular_poster_path_" + i);
            list.add(item);
        }
        return list;
    }

    private List<MovieListItemViewData> topRatedMockData() {
        List<MovieListItemViewData> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            MovieListItemViewData item = new MovieListItemViewData(i, "top_rated_title_of_" + i, "top_rated_poster_path_" + i);
            list.add(item);
        }
        return list;
    }
}