package com.example.aera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class HomePage extends BaseActivity {

    private CircularProgressIndicator co2ProgressRing;
    private TextView co2AmountText;
    private TextView nameText;

    // Local Storage File Names
    private static final String PREFS_NAME = "AeraPrefs";
    private static final String KEY_CO2_VALUE = "co2_value";
    private static final String KEY_PROGRESS = "co2_progress";
    private static final String KEY_USER_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        co2ProgressRing = findViewById(R.id.co2ProgressRing);
        co2AmountText = findViewById(R.id.co2AmountText);
        nameText = findViewById(R.id.nameText);

        loadLocalData();

        String userName = nameText.getText().toString();
        if (!userName.isEmpty()) {
            Toast.makeText(this, "Welcome, " + userName + "!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        Log.d("HomePage", "Running custom onPause logic: saving data...");

        try {
            String currentName = nameText.getText().toString();
            float currentCo2 = Float.parseFloat(co2AmountText.getText().toString());
            int currentProgress = co2ProgressRing.getProgress();
            saveLocalData(currentName, currentCo2, currentProgress);
        } catch (NumberFormatException e) {
            Log.e("HomePage", "Error parsing CO2 value", e);
        }
    }

    private void saveLocalData(String name, float co2Value, int progressPercent) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(KEY_USER_NAME, name);
        editor.putFloat(KEY_CO2_VALUE, co2Value);
        editor.putInt(KEY_PROGRESS, progressPercent);

        editor.apply();
    }

    // Local data storage
    private void loadLocalData() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        if (preferences.contains(KEY_USER_NAME)) {
            nameText.setText(preferences.getString(KEY_USER_NAME, ""));
        }

        if (preferences.contains(KEY_CO2_VALUE)) {
            co2AmountText.setText(String.valueOf(preferences.getFloat(KEY_CO2_VALUE, 0.0f)));
        }

        if (preferences.contains(KEY_PROGRESS)) {
            co2ProgressRing.setProgress(preferences.getInt(KEY_PROGRESS, 0));
        }
    }
}