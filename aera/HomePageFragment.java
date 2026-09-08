package com.example.aera;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class HomePageFragment extends Fragment {

    private CircularProgressIndicator co2ProgressRing;
    private TextView co2AmountText;
    private TextView nameText;
    private DatabaseHelper dbHelper;

    // FIXED: "AeraPrefs" to match your Aera.java file!
    private static final String PREFS_NAME = "AeraPrefs";
    private static final String KEY_USER_NAME = "user_name";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page, container, false);

        try {
            // Safely Show Bottom Nav and sync icon
            if (getActivity() instanceof Aera) {
                ((Aera) getActivity()).setBottomNavigationVisibility(true);
                ((Aera) getActivity()).updateBottomNavSelection(R.id.nav_home);
            }
        } catch (Exception e) {
            Log.e("HomePage", "Navigation update error: " + e.getMessage());
        }

        try {
            // Safely initialize database
            dbHelper = new DatabaseHelper(requireContext());
        } catch (Exception e) {
            Log.e("HomePage", "Database Error: " + e.getMessage());
        }

        co2ProgressRing = view.findViewById(R.id.co2ProgressRing);
        co2AmountText = view.findViewById(R.id.co2AmountText);
        nameText = view.findViewById(R.id.nameText);

        loadLocalData();
        loadDatabaseData();

        // SAFELY check for nameText before trying to use it!
        if (nameText != null) {
            String userName = nameText.getText().toString();
            if (!userName.isEmpty() && savedInstanceState == null) {
                Toast.makeText(requireContext(), "Welcome, " + userName + "!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("HomePage", "CRITICAL ERROR: 'nameText' is missing from home_page.xml!");
        }

        return view;
    }

    private void loadDatabaseData() {
        try {
            // Only load if the views and database actually exist
            if (dbHelper != null && co2AmountText != null && co2ProgressRing != null) {
                double totalFootprint = dbHelper.getTotalFootprint();
                co2AmountText.setText(String.format("%.1f kg", totalFootprint));

                int progressPercent = (int) Math.min((totalFootprint / 10.0) * 100, 100);
                co2ProgressRing.setProgress(progressPercent);
            }
        } catch (Exception e) {
            Log.e("HomePage", "Error loading Database data: " + e.getMessage());
        }
    }

    private void loadLocalData() {
        try {
            SharedPreferences preferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (preferences.contains(KEY_USER_NAME) && nameText != null) {
                nameText.setText(preferences.getString(KEY_USER_NAME, ""));
            }
        } catch (Exception e) {
            Log.e("HomePage", "Error loading SharedPreferences: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDatabaseData();
    }
}