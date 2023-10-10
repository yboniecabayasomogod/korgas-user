package com.example.korgas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_SCREEN_DISPLAY_LENGTH = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainActivity = new Intent(SplashScreen.this, PetrolStationListRegistered.class);
                startActivity(mainActivity);
                finish();

                overridePendingTransition(0,0);
            }
        }, SPLASH_SCREEN_DISPLAY_LENGTH);

    }
}