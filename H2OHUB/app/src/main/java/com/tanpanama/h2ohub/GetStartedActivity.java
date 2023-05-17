package com.tanpanama.h2ohub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);
        Fragment_GetStarted1 gs = new Fragment_GetStarted1();
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_GetStarted, gs).commit();
    }
}