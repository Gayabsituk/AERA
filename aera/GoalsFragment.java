package com.example.activitydasbord.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.activitydasbord.R;
import com.example.activitydasbord.database.DatabaseHelper;
import com.example.activitydasbord.models.Goal;
import java.util.ArrayList;
import java.util.Locale;

public class GoalsFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private RecyclerView rvCompleted;
    private GoalAdapter adapter;
    private TextView tvActiveDesc, tvActiveProgress;
    private ProgressBar progressBar;
    private View cardActive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        
        dbHelper = new DatabaseHelper(requireContext());
        rvCompleted = view.findViewById(R.id.rv_completed_goals);
        rvCompleted.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        adapter = new GoalAdapter(new ArrayList<>());
        rvCompleted.setAdapter(adapter);

        cardActive = view.findViewById(R.id.card_active_goal);
        tvActiveDesc = view.findViewById(R.id.tv_active_goal_desc);
        tvActiveProgress = view.findViewById(R.id.tv_active_goal_progress);
        progressBar = view.findViewById(R.id.progress_goal);

        view.findViewById(R.id.btn_edit_goal).setOnClickListener(v -> openSetGoal());
        
        loadGoals();
        
        return view;
    }

    private void loadGoals() {
        Goal active = dbHelper.getActiveGoal();
        if (active != null) {
            cardActive.setVisibility(View.VISIBLE);
            tvActiveDesc.setText(active.getDescription());
            progressBar.setProgress(active.getProgressPercentage());
            tvActiveProgress.setText(String.format(Locale.getDefault(), "%d%% completed", active.getProgressPercentage()));
        } else {
            cardActive.setVisibility(View.GONE);
        }

        adapter.updateList(dbHelper.getCompletedGoals());
    }

    private void openSetGoal() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SetGoalFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGoals();
    }
}