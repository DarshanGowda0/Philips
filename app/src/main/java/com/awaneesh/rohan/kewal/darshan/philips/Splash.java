package com.awaneesh.rohan.kewal.darshan.philips;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;


public class Splash extends ActionBarActivity {

    Intent intent;
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences("Yes", Context.MODE_PRIVATE);
        check = preferences.getInt("Check", 0);
        new CountDownTimer(3500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (check == 0) {
                    intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }
            }

        }.start();
    }
}
