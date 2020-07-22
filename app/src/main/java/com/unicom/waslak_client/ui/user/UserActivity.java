package com.unicom.waslak_client.ui.user;

import android.os.Bundle;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.unicom.waslak_client.R;
import com.unicom.waslak_client.databinding.ActivityUserBinding;
import com.unicom.waslak_client.ui.BaseActivity;

public class UserActivity extends BaseActivity {
    ActivityUserBinding binding;
    private static final String TAG = "UserActivity";
    public static int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "mohammed onCreate: ");
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpNavigation();
    }

    public void setUpNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.user_nav);
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getInt("id");
            if (id == 0){
                NavigationUI.setupWithNavController(binding.bottomNavigation,
                        navHostFragment.getNavController());
                return;
            }
            NavInflater navInflater = navHostFragment.getNavController().getNavInflater();
            NavGraph navGraph = navInflater.inflate(R.navigation.user_nav_graph);
            NavController navController = navHostFragment.getNavController();
            navGraph.setStartDestination(R.id.notificationFragment);
            navController.setGraph(navGraph);
            NavigationUI.setupWithNavController(binding.bottomNavigation,
                    navController);
        }else {
            NavigationUI.setupWithNavController(binding.bottomNavigation,
                    navHostFragment.getNavController());
        }
    }
}