package com.example.aera;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    // Automatically gets the name of the Activity you are currently in
    private String getTag() {
        return "Lifecycle_" + this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getTag(), "onCreate: Activity is being created.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getTag(), "onStart: Activity is now visible to the user.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getTag(), "onResume: Activity is in the foreground and ready for interaction.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(getTag(), "onPause: Activity is losing focus (e.g., going to background).");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getTag(), "onStop: Activity is no longer visible.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(getTag(), "onRestart: Activity is coming back from stopped state.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getTag(), "onDestroy: Activity is destroyed and memory cleared.");
    }
}