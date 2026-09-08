package com.example.aera;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GetStartedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.get_started_page, container, false);

        try {
            // Hide Bottom Nav on this screen
            ((Aera) requireActivity()).setBottomNavigationVisibility(false);
        } catch (Exception e) {
            Log.e("GetStartedFragment", "Could not hide bottom nav: " + e.getMessage());
        }

        // 1. Safely handle the Button
        Button getStartedButton = view.findViewById(R.id.getStartedButton);
        if (getStartedButton != null) {
            getStartedButton.setOnClickListener(v -> {
                if (getActivity() instanceof Aera) {
                    ((Aera) getActivity()).switchFragment(new LoginFragment(), false);
                }
            });
        } else {
            Log.e("GetStartedFragment", "CRITICAL ERROR: 'getStartedButton' is missing or spelled wrong in get_started_page.xml!");
        }

        // 2. Safely handle the Animation
        View heroCard = view.findViewById(R.id.heroImageCard);
        if (heroCard != null) {
            try {
                // If pulse.xml is missing, this is what crashes the app
                Animation breathingAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse);
                heroCard.startAnimation(breathingAnimation);
            } catch (Exception e) {
                Log.e("GetStartedFragment", "CRITICAL ERROR: Missing 'pulse.xml' in res/anim folder! " + e.getMessage());
            }
        } else {
            Log.e("GetStartedFragment", "CRITICAL ERROR: 'heroImageCard' is missing or spelled wrong in get_started_page.xml!");
        }

        return view;
    }
}