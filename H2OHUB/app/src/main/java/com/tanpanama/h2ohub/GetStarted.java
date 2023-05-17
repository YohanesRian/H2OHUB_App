package com.tanpanama.h2ohub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GetStarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        FragmentGetStarted1 fgs = new FragmentGetStarted1();
        getSupportFragmentManager().beginTransaction().replace(R.id.Container_GetStarted, fgs).commit();
    }
}