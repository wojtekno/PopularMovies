package com.gmail.nowak.wjw.popularmovies.presenter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.gmail.nowak.wjw.popularmovies.MyApplication;
import com.gmail.nowak.wjw.popularmovies.R;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.d("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.my_nav_host_fragment_container);
        NavController navController = navHostFragment.getNavController();

        ((MyApplication) getApplication()).appContainer.nav.getNavDirectionsEvent().observe(this, (navDirEvent) -> {
            Timber.d("navDirEvent: %s : %d", navDirEvent.peekContent().getActionId(), navDirEvent.peekContent().getArguments().getInt("list_position"));
            if (!navDirEvent.hasBeenHandled()) {
                navController.navigate(navDirEvent.getContentIfNotHandled());
            }
        });

    }
}
