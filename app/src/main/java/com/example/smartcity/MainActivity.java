package com.example.smartcity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.smartcity.view.activities.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcity.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_event, R.id.nav_message, R.id.nav_search)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (String.valueOf(item.getTitle()).equals(getString(R.string.menu_search_event))
                    || String.valueOf(item.getTitle()).equals(getString(R.string.menu_event))
                    || String.valueOf(item.getTitle()).equals(getString(R.string.menu_message))) {
                NavigationUI.onNavDestinationSelected(item, navController);
                drawer.close();
            }
            else if (String.valueOf(item.getTitle()).equals(getString(R.string.log_out))) {
                SharedPreferences.Editor sharedPref = getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE).edit();

                sharedPref.remove("JSONWEBTOKEN");
                sharedPref.apply();

                this.finish();
                startActivity(new Intent(this, LoginActivity.class));
            }

            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}