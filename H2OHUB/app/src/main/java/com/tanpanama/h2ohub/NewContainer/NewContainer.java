package com.tanpanama.h2ohub.NewContainer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

import es.dmoral.toasty.Toasty;

public class NewContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_container);
        StepView fs = new StepView(5);
        getSupportFragmentManager().beginTransaction().replace(R.id.StepView_Container, fs).commit();

        NewContainer1 nc = new NewContainer1();
        getSupportFragmentManager().beginTransaction().replace(R.id.Inner_Container, nc).commit();
    }
}