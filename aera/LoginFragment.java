package com.example.aera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;

public class LoginFragment extends Fragment {

    private EditText nameInput;
    private TextView errorText;
    private MaterialCardView statsCard;

    private static final String PREFS_NAME = "AeraPrefs";
    private static final String KEY_USER_NAME = "user_name";
    private static final int COLOR_DEFAULT_BORDER = Color.parseColor("#C2D1C7");
    private static final int COLOR_ERROR_BORDER = Color.parseColor("#B3261E");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_page, container, false);

        // Hide Bottom Nav on this screen
        ((Aera) requireActivity()).setBottomNavigationVisibility(false);

        nameInput = view.findViewById(R.id.editTextTextEmailAddress2);
        Button continueButton = view.findViewById(R.id.button);
        errorText = view.findViewById(R.id.errorText);
        statsCard = view.findViewById(R.id.statsCard);

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { clearError(); }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        continueButton.setOnClickListener(v -> {
            String enteredName = nameInput.getText().toString().trim();
            if (isValidName(enteredName)) {
                SharedPreferences preferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                preferences.edit().putString(KEY_USER_NAME, enteredName).apply();

                ((Aera) requireActivity()).switchFragment(new HomePageFragment(), false);
            }
        });

        return view;
    }

    private boolean isValidName(String name) {
        if (name.isEmpty()) { showError("Please enter your name"); return false; }
        if (name.length() < 2) { showError("Name must be at least 2 characters"); return false; }
        if (!name.matches("[a-zA-Z\\s]+")) { showError("Name can only contain letters"); return false; }
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