package com.example.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout container;
    private View slidingIndicator;
    private TextView btnOption1, btnOption2, btnOption3;
    private int buttonWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        slidingIndicator = findViewById(R.id.slidingIndicator);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);

        container.post(() -> {
            int totalWidth = container.getWidth() - container.getPaddingLeft() - container.getPaddingRight();
            buttonWidth = totalWidth / 3;

            ViewGroup.LayoutParams params = slidingIndicator.getLayoutParams();
            params.width = buttonWidth;
            slidingIndicator.setLayoutParams(params);

            // load initial tab (DayFragment)
            if (savedInstanceState == null) {
                selectOption(0);
            }
        });

        // button click listeners
        btnOption1.setOnClickListener(v -> selectOption(0));
        btnOption2.setOnClickListener(v -> selectOption(1));
        btnOption3.setOnClickListener(v -> selectOption(2));
    }

    private void selectOption(int index) {
        if (buttonWidth == 0) return;

        // calculate slider position
        float targetX = (index * buttonWidth) + container.getPaddingLeft();

        // animate background thumb
        slidingIndicator.animate()
                .x(targetX)
                .setDuration(250)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        // update active/inactive text colors
        btnOption1.setTextColor(index == 0 ? Color.parseColor("#2D6A4F") : Color.parseColor("#6B7280"));
        btnOption2.setTextColor(index == 1 ? Color.parseColor("#2D6A4F") : Color.parseColor("#6B7280"));
        btnOption3.setTextColor(index == 2 ? Color.parseColor("#2D6A4F") : Color.parseColor("#6B7280"));

        // load the corresponding fragment into the container
        switch (index) {
            case 0:
                loadFragment(new DayFragment());
                break;
            case 1:
                loadFragment(new WeekFragment());
                break;
            case 2:
                loadFragment(new MonthFragment());
                break;
        }
    }

    // swaps fragments inside R.id.fragmentContainer
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
