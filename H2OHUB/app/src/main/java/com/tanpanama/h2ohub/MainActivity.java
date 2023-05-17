package com.tanpanama.h2ohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Jika tidak ada data dia di database hp
                Intent intent = new Intent(MainActivity.this, GetStarted.class);
                startActivity(intent);

                //jika sudah ada data
//                Intent intent = new Intent(MainActivity.this, MainMenu.class);
//                startActivity(intent);
            }
        }, 3000);
    }
}