package com.example.aera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

// 1. CHANGE THIS: Extend BaseActivity instead of AppCompatActivity
public class LoginPage extends BaseActivity {

    private EditText nameInput;
    private TextView errorText;
    private MaterialCardView statsCard;

    private static final String PREFS_NAME = "AeraPrefs";
    private static final String KEY_USER_NAME = "user_name";

    private static final int COLOR_DEFAULT_BORDER = Color.parseColor("#C2D1C7");
    private static final int COLOR_ERROR_BORDER = Color.parseColor("#B3261E");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // This super.onCreate automatically calls the log in BaseActivity!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        nameInput = findViewById(R.id.editTextTextEmailAddress2);
        Button continueButton = findViewById(R.id.button);
        errorText = findViewById(R.id.errorText);
        statsCard = findViewById(R.id.statsCard);

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearError();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        continueButton.setOnClickListener(v -> {
            String enteredName = nameInput.getText().toString().trim();

            if (isValidName(enteredName)) {
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(KEY_USER_NAME, enteredName);
                editor.apply();

                Intent intent = new Intent(LoginPage.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isValidName(String name) {
        if (name.isEmpty()) {
            showError("Please enter your name");
            return false;
        }

        if (name.length() < 2) {
            showError("Name must be at least 2 characters");
            return false;
        }

        if (!name.matches("[a-zA-Z\\s]+")) {
            showError("Name can only contain letters");
            return false;
        }

        return true;
    }

    private void showError(String errorMessage) {
        errorText.setText(errorMessage);
        errorText.setVisibility(View.VISIBLE);
        statsCard.setStrokeColor(COLOR_ERROR_BORDER);
    }

    private void clearError() {
        if (errorText.getVisibility() == View.VISIBLE) {
            errorText.setVisibility(View.GONE);
            statsCard.setStrokeColor(COLOR_DEFAULT_BORDER);
        }
    }
}