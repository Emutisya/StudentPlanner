package com.example.studentplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import Fragments.SignInFragment;

public class auth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SignInFragment()).commit();
    }
}
