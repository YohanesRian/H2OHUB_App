package com.tanpanama.h2ohub.Dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.tanpanama.h2ohub.Dashboard.Cups.Cups;
import com.tanpanama.h2ohub.Dashboard.Home.Home;
import com.tanpanama.h2ohub.Dashboard.Report.Report;
import com.tanpanama.h2ohub.Dashboard.Setting.Setting;
import com.tanpanama.h2ohub.GetStarted.GetStarted1;
import com.tanpanama.h2ohub.R;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import nl.joery.animatedbottombar.AnimatedBottomBar;

public class Dashboard extends AppCompatActivity {

    private int current_tab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        if(getIntent().hasExtra("welcome")){
            String name = getIntent().getStringExtra("welcome");
            Toasty.success(this, ("Welcome, "+ name), Toast.LENGTH_SHORT, true).show();
        }

        AnimatedBottomBar bb = findViewById(R.id.Bottom_Navbar);
        bb.selectTabAt(0, true);
        Home h = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.Main_Container, h).commit();

        bb.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                switch(i1){
                    case 0:
                        Home h = new Home();
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.Main_Container, h).commit();
                        getSupportFragmentManager().popBackStack();
                        break;
                    case 1:
                        Cups c = new Cups();
                        if(current_tab > i1){
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.Main_Container, c).commit();
                        }
                        else{
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.Main_Container, c).commit();
                        }
                        getSupportFragmentManager().popBackStack();
                        break;
                    case 2:
                        Report r = new Report((current_tab > i1));
                        if(current_tab > i1){
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.Main_Container, r).commit();
                        }
                        else{
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.Main_Container, r).commit();
                        }
                        getSupportFragmentManager().popBackStack();
                        break;
                    case 3:
                        Setting s = new Setting();
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.Main_Container, s).commit();
                        getSupportFragmentManager().popBackStack();
                        break;
                }
                current_tab = i1;
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });

    }
}