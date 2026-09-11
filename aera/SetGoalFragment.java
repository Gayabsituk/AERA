package com.example.activitydasbord.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.activitydasbord.R;
import com.example.activitydasbord.database.DatabaseHelper;

public class SetGoalFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private Spinner spinnerPeriod, spinnerCategory;
    private EditText etTarget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_goal, container, false);
        dbHelper = new DatabaseHelper(requireContext());

        spinnerPeriod = view.findViewById(R.id.spinner_goal_period);
        spinnerCategory = view.findViewById(R.id.spinner_goal_category);
        etTarget = view.findViewById(R.id.et_target_reduction);

        String[] periods = {"Weekly", "Monthly", "Yearly"};
        String[] categories = {"Overall Footprint", "Transport", "Energy", "Food", "Shopping", "Waste"};

        spinnerPeriod.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, periods));
        spinnerCategory.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories));

        view.findViewById(R.id.btn_back).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        view.findViewById(R.id.btn_save_goal).setOnClickListener(v -> saveGoal());

        return view;
    }

    private void saveGoal() {
        String targetStr = etTarget.getText().toString();
        if (targetStr.isEmpty()) {
            etTarget.setError("Required");
            return;
        }

        int target = Integer.parseInt(targetStr);
        String period = spinnerPeriod.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String desc = "Reduce " + category.toLowerCase() + " by " + target + "% this " + period.toLowerCase().replace("ly", "");

        dbHelper.addGoal(desc, target, period, category);
        Toast.makeText(requireContext(), "Goal Set!", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }
}