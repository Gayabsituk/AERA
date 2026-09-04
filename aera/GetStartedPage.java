package com.example.aera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class GetStartedPage extends BaseActivity {

    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started_page);

        getStartedButton = findViewById(R.id.getStartedButton);

        // Find your circular card background
        View heroCard = findViewById(R.id.heroImageCard);

        // Load the breathing animation and start it
        Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        heroCard.startAnimation(breathingAnimation);

        // Navigate to the Login Page when clicked
        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedPage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });
    }
}