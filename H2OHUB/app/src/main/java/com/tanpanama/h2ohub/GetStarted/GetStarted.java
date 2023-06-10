package com.tanpanama.h2ohub.GetStarted;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.R;

public class GetStarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        GetStarted1 gs = new GetStarted1();
        getSupportFragmentManager().beginTransaction().replace(R.id.Main_Container, gs).commit();
    }
}