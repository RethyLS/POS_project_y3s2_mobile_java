package com.example.pos_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pos_project.R;
import com.example.pos_project.activity.SalesActivity;
import com.example.pos_project.auth.AuthManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Navigate based on authentication status after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                AuthManager authManager = AuthManager.getInstance(this);
                Intent intent;
                
                if (authManager.isLoggedIn()) {
                    intent = new Intent(SplashActivity.this, SalesActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                
                startActivity(intent);
                finish();
            } catch (Exception e) {
                // Fallback to LoginActivity in case of any errors
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}