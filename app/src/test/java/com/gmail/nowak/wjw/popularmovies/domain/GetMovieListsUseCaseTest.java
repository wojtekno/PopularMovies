package com.gmail.nowak.wjw.popularmovies.domain;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.api.ApiResponseMovieList;
import com.gmail.nowak.wjw.popularmovies.data.model.local.FavouriteMovie;
import com.gmail.nowak.wjw.popularmovies.data.model.view_data.list.MovieListItemViewData;
import com.gmail.nowak.wjw.popularmovies.data.repository.MoviesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetMovieListsUseCaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public TestRule testRule = new InstantTaskExecutorRule();

    @Mock
    private MoviesRepository repository;
    @Mock
    private Observer<List<MovieListItemViewData>> mListObserver;
    @Mock
    private Observer<Integer> mResponseCodeObserver;

    private GetMovieListsUseCase useCase;

    @Before
    public void setUp() throws Exception {
        LiveData<ApiResponseMovieList> mLd = new MutableLiveData<>(new ApiResponseMovieList(mockApiData(), 200));
        when(repository.getPopularMoviesResponseLD()).thenReturn(mLd);
        useCase = new GetMovieListsUseCase(repository);
        useCase.getPopularMovieList().observeForever(mListObserver);
        useCase.getPopularResponseCode().observeForever(mResponseCodeObserver);
    }

    private void reconfigureUseCase() {
        when(repository.getPopularMoviesResponseLD()).thenReturn(new MutableLiveData<>(new ApiResponseMovieList(new ArrayList<>())));
        useCase = new GetMovieListsUseCase(repository);
        useCase.getPopularMovieList().observeForever(mListObserver);

    }

    @Test
    public void testGetMovieListWhenEmptyList() {
        when(repository.getPopularMoviesResponseLD()).thenReturn(new MutableLiveData<>(new ApiResponseMovieList(new ArrayList<>())));
        useCase = new GetMovieListsUseCase(repository);
        useCase.getPopularMovieList().observeForever(mListObserver);
        List<MovieListItemViewData> list = useCase.getPopularMovieList().getValue();
        assertEquals(0, list.size());
        verify(mListObserver).onChanged(new ArrayList<>());

    }


    @Test
    public void testGetPopularMovieList() {
        List<MovieListItemViewData> list = useCase.getPopularMovieList().getValue();
        assertEquals(25, list.size());
        for (int i = 0; i < 25; i++) {
            assertEquals(i, list.get(i).getApiId());
            assertEquals("original title of " + i, list.get(i).getOriginalTitle());
            assertEquals("posterPath#" + i, list.get(i).getImagePath());
        }
    }

    @Test
    public void testGetPopularErrorMessage() {
        int responseCode = useCase.getPopularResponseCode().getValue();
        assertEquals(200, responseCode);
        verify(mResponseCodeObserver).onChanged(200);
    }

    @Test
    public void testGetFavouriteMovieList() {
        when(repository.getFavouriteMoviesLD()).thenReturn(new MutableLiveData<>(mockDbData()));
        useCase.getFavouriteMovieList().observeForever(mListObserver);
        List<MovieListItemViewData> list = useCase.getFavouriteMovieList().getValue();
        assertNotNull(list);
        assertEquals(30, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(i, list.get(i).getApiId());
            assertEquals("original title of " + i, list.get(i).getOriginalTitle());
            assertEquals("posterPath#" + i, list.get(i).getImagePath());
        }
    }


    private List<ApiMovie> mockApiData() {
        List<ApiMovie> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            ApiMovie item = new ApiMovie(i, "posterPath#" + i, "original title of " + i);

            list.add(item);
        }
        return list;
    }

    private List<FavouriteMovie> mockDbData() {
        List<FavouriteMovie> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            FavouriteMovie item = new FavouriteMovie(i, "original title of " + i,"posterPath#" + i);

            list.add(item);
        }
        return list;
    }
}