package com.example.studentplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class OnboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        //PAUSING THE APP FOR 1.5 SECONDS AND ANYTHING IN THE RUN METHOD WILL RUN
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isFirstTime();

            }
        },1500);
    }

    private  void isFirstTime() {
        //for checking if the app is running for the very first time
        //save a value to shared preferences
        SharedPreferences preferences= getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        //default value type
        if(isFirstTime){
            //IF IT IS TRUE THEN ITS FIRST TIME AND WE WILL CHANGE IT TO FALSE
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();

            //START HOME ACTIVITY
            startActivity(new Intent(OnboardActivity.this,HomeActivity.class));
            finish();

        }else{
            //START AUTH ACTIVITY
            startActivity(new Intent(OnboardActivity.this,LoginActivity.class));
            finish();
        }
    }
}
