package com.example.aera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Aera extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aera);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Attach the safe listener
        setupBottomNavListener();

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("AeraPrefs", MODE_PRIVATE);
            if (prefs.contains("user_name")) {
                switchFragment(new HomePageFragment(), false);
            } else {
                switchFragment(new GetStartedFragment(), false);
            }
        }
    }

    // This method contains the fix! It checks what fragment is currently open to prevent loops.
    private void setupBottomNavListener() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();

                // Find out which fragment is currently on the screen
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if (id == R.id.nav_home) {
                    // Only switch if we are NOT already on the HomePage
                    if (!(currentFragment instanceof HomePageFragment)) {
                        switchFragment(new HomePageFragment(), false);
                    }
                    return true;
                } else if (id == R.id.nav_activity) {
                    // Only switch if we are NOT already on the ActivityLog
                    if (!(currentFragment instanceof ActivityLogFragment)) {
                        switchFragment(new ActivityLogFragment(), false);
                    }
                    return true;
                }
                return false;
            });
        }
    }

    public void switchFragment(Fragment fragment, boolean addToBackStack) {
        try {
            androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (Exception e) {
            Log.e("AeraApp", "Error switching fragment: " + e.getMessage());
        }
    }

    public void setBottomNavigationVisibility(boolean isVisible) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void updateBottomNavSelection(int itemId) {
        if (bottomNavigationView != null) {
            // Temporarily remove the listener so it doesn't trigger a click
            bottomNavigationView.setOnItemSelectedListener(null);

            // Highlight the correct tab
            bottomNavigationView.setSelectedItemId(itemId);

            // Put the safe listener back on
            setupBottomNavListener();
        }
    }
}