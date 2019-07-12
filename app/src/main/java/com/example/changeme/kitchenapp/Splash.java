package com.example.changeme.kitchenapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;

public class Splash extends AppCompatActivity {
private  static int SPLASH_TIME_OUT = 4000;
private TextView timeofDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        timeofDay = findViewById(R.id.timeofday);

        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);
        setTime(time);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainintent = new Intent(Splash.this, MainActivity.class);
                mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainintent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_oute);


            }
        }, SPLASH_TIME_OUT);

    }

    private void setTime(int time) {
        if(time >=0 && time<12){
            timeofDay.setText("Good Morning");
        }
        else if (time >=12 && time<16){
            timeofDay.setText("Good Afternoon");
        }
        else if(time >=17 && time<=23){
            timeofDay.setText("Good Evening");
        }
    }
}
