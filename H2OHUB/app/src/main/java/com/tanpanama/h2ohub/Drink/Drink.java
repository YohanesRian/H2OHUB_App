package com.tanpanama.h2ohub.Drink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.tanpanama.h2ohub.NewContainer.NewContainer1;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

import es.dmoral.toasty.Toasty;

public class Drink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        String empty_weight = getIntent().getStringExtra("empty_weight");
        String full_weight = getIntent().getStringExtra("full_weight");
        String limit = getIntent().getStringExtra("limit");

        StepView fs = new StepView(4);
        getSupportFragmentManager().beginTransaction().replace(R.id.StepView_Container, fs).commit();

        Drink1 nc = new Drink1();
        Bundle bundle = new Bundle();
        bundle.putString("empty_weight", empty_weight);
        bundle.putString("full_weight", full_weight);
        bundle.putString("limit", limit);
        nc.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.Inner_Container, nc).commit();
    }
}