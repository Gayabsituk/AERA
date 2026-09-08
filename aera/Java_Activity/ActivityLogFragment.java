package com.example.activitydasbord.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.activitydasbord.R;
import com.example.activitydasbord.database.DatabaseHelper;
import java.util.ArrayList;

public class ActivityLogFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private RecyclerView rvActivities;
    private ActivityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_log, container, false);
        
        dbHelper = new DatabaseHelper(requireContext());
        rvActivities = view.findViewById(R.id.rv_activities);
        rvActivities.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        adapter = new ActivityAdapter(new ArrayList<>());
        rvActivities.setAdapter(adapter);
        
        view.findViewById(R.id.fab_add_activity).setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LogActivityFragment())
                    .addToBackStack(null)
                    .commit();
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