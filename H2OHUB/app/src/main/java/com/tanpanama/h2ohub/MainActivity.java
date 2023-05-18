package com.tanpanama.h2ohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.tanpanama.h2ohub.LocalHelper.database;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                database db = new database(MainActivity.this);
                if(db.isNotRegistered()){
                    Intent intent = new Intent(MainActivity.this, GetStarted.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}