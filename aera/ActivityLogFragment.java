package com.example.aera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // <- THIS IS THE IMPORTANT LINE
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class ActivityLogFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ActivityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_log, container, false);

        // Show Bottom Nav and sync icon
        ((Aera) requireActivity()).setBottomNavigationVisibility(true);
        ((Aera) requireActivity()).updateBottomNavSelection(R.id.nav_activity);

        dbHelper = new DatabaseHelper(requireContext());
        RecyclerView rvActivities = view.findViewById(R.id.rv_activities);
        rvActivities.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ActivityAdapter(new ArrayList<>());
        rvActivities.setAdapter(adapter);

        MaterialButton fabAddActivity = view.findViewById(R.id.fab_add_activity);
        fabAddActivity.setOnClickListener(v -> {
            ((Aera) requireActivity()).switchFragment(new LogActivityFragment(), true);
        });

        loadActivities();
        return view;
    }

    private void loadActivities() {
        adapter.updateList(dbHelper.getAllActivities());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadActivities();
    }
}