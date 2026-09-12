package com.example.aera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Show the bottom nav and keep the Settings tab highlighted
        if (getActivity() instanceof Aera) {
            ((Aera) getActivity()).setBottomNavigationVisibility(true);
            ((Aera) getActivity()).updateBottomNavSelection(R.id.nav_settings);
        }

        // Show the stored username on the Profile row (falls back to the default)
        SharedPreferences prefs = requireActivity().getSharedPreferences("AeraPrefs", android.content.Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", getString(R.string.hev_abi));
        TextView profileSubtext = view.findViewById(R.id.profileSubtext);
        if (profileSubtext != null && userName != null && !userName.isEmpty()) {
            profileSubtext.setText(userName);
        }

        // Open the Computations screen
        View computationsRow = view.findViewById(R.id.computationsRow);
        if (computationsRow != null) {
            computationsRow.setOnClickListener(v ->
                    ((Aera) requireActivity()).switchFragment(new ComputationsFragment(), true));
        }
    }
}
