package com.tanpanama.h2ohub.Dashboard.Setting;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tanpanama.h2ohub.Adapter.level_recyclerview_adapter;
import com.tanpanama.h2ohub.Adapter.setting_recycler_adapter;
import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.GetStarted.level_serial;
import com.tanpanama.h2ohub.Handler.database;
import com.tanpanama.h2ohub.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recycler_Report#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recycler_Report extends Fragment implements setting_recycler_interface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private ArrayList<String> title;
    private ArrayList<String> data;
    private database db;
    private UserData ud;
    private AlertDialog alertDialog;

    public Recycler_Report() {
        // Required empty public constructor
    }
    public Recycler_Report(ArrayList<String> title, ArrayList<String> data) {
        this.data = data;
        this.title = title;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recycler_Report.
     */
    // TODO: Rename and change types and number of parameters
    public static Recycler_Report newInstance(String param1, String param2) {
        Recycler_Report fragment = new Recycler_Report();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler__report, container, false);
        db = new database(getActivity().getApplicationContext());

        RecyclerView rv = v.findViewById(R.id.recycler_setting);
        setting_recycler_adapter sra = new setting_recycler_adapter(getContext(), title, data, this);
        rv.setAdapter(sra);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onItemClicked(int position) {
        switch(position){
            case 3:
                change_height(position);
                break;
            case 4:
                change_weight(position);
                break;
            case 5:
                change_activity_level(position);
                break;
            case 6:
                change_limit(position);
                break;
        }
    }

    private void change_activity_level(int position){
        ud = db.getUserData();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.activity_level_dialog_box, null);
        Button yes = view.findViewById(R.id.yesbtn);
        TextView cancel = view.findViewById(R.id.cancelbtn);
        RecyclerView rv = view.findViewById(R.id.recycler_level);

        ArrayList<level_serial> als = new ArrayList<>();

        level_recyclerview_adapter adapter = new level_recyclerview_adapter(getContext(), als);
        rv.setHasFixedSize(true);
        for(int i = 0; i < ud.getLevel().length ;i++){
            level_serial ls = new level_serial();
            ls.setLevel_desc(ud.getLevel_description()[i]);
            ls.setLevel(ud.getLevel()[i]);
            als.add(ls);
        }
        adapter.SetLevel(als);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String level = adapter.getSelected().getLevel();
                ud.setActivity_level(level);
                db.updateUserData(ud.getName(), ud);
                if(ud.getLimit() > 0){
                    db.updateDrinkDataLimit(ud.getLimit());
                    db.updateDrinkDataTarget(ud.getLimit());
                }
                else{
                    db.updateDrinkDataTarget(ud.getTargetDrinkWater());
                    db.updateDrinkDataLimit(ud.getMaxDrinkWater());
                }
                data.set(position, ud.getActivityLevel());
                Toasty.success(getContext(), "Set Activity Level Success", Toast.LENGTH_SHORT, true).show();
                reload_container();
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }
    private void change_weight(int position){
        ud = db.getUserData();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.weight_dialog_box, null);
        Button yes = view.findViewById(R.id.yesbtn);
        TextView cancel = view.findViewById(R.id.cancelbtn);
        EditText et = view.findViewById(R.id.et_limit);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().trim().length() == 0){
                    yes.setEnabled(false);
                    yes.setBackgroundColor(getResources().getColor(R.color.Grey));
                    yes.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    yes.setEnabled(true);
                    yes.setBackgroundColor(getResources().getColor(R.color.Green));
                    yes.setTextColor(getResources().getColor(R.color.DarkGreen));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    yes.setEnabled(false);
                    yes.setBackgroundColor(getResources().getColor(R.color.Grey));
                    yes.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    yes.setEnabled(true);
                    yes.setBackgroundColor(getResources().getColor(R.color.Green));
                    yes.setTextColor(getResources().getColor(R.color.DarkGreen));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight = Integer.parseInt(et.getText().toString().trim());
                if(weight >= 20 && weight <= 300){
                    ud.setWeight(weight);
                    db.updateUserData(ud.getName(), ud);
                    if(ud.getLimit() > 0){
                        db.updateDrinkDataLimit(ud.getLimit());
                        db.updateDrinkDataTarget(ud.getLimit());
                    }
                    else{
                        db.updateDrinkDataTarget(ud.getTargetDrinkWater());
                        db.updateDrinkDataLimit(ud.getMaxDrinkWater());
                    }
                    data.set(position, ud.getWeight()+" kg");
                    Toasty.success(getContext(), "Set Height Success", Toast.LENGTH_SHORT, true).show();
                    reload_container();
                    alertDialog.dismiss();
                }
                else{
                    Toasty.error(getContext(), "Invalid value", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }
    private void change_height(int position){
        ud = db.getUserData();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.height_dialog_box, null);
        Button yes = view.findViewById(R.id.yesbtn);
        TextView cancel = view.findViewById(R.id.cancelbtn);
        EditText et = view.findViewById(R.id.et_limit);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().trim().length() == 0){
                    yes.setEnabled(false);
                    yes.setBackgroundColor(getResources().getColor(R.color.Grey));
                    yes.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    yes.setEnabled(true);
                    yes.setBackgroundColor(getResources().getColor(R.color.Green));
                    yes.setTextColor(getResources().getColor(R.color.DarkGreen));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    yes.setEnabled(false);
                    yes.setBackgroundColor(getResources().getColor(R.color.Grey));
                    yes.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    yes.setEnabled(true);
                    yes.setBackgroundColor(getResources().getColor(R.color.Green));
                    yes.setTextColor(getResources().getColor(R.color.DarkGreen));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = Integer.parseInt(et.getText().toString().trim());
                if(height >= 60 && height <= 290){
                    ud.setHeight(height);
                    db.updateUserData(ud.getName(), ud);
                    if(ud.getLimit() > 0){
                        db.updateDrinkDataLimit(ud.getLimit());
                        db.updateDrinkDataTarget(ud.getLimit());
                    }
                    else{
                        db.updateDrinkDataTarget(ud.getTargetDrinkWater());
                        db.updateDrinkDataLimit(ud.getMaxDrinkWater());
                    }
                    data.set(position, ud.getHeight()+" cm");
                    Toasty.success(getContext(), "Set Height Success", Toast.LENGTH_SHORT, true).show();
                    reload_container();
                    alertDialog.dismiss();
                }
                else{
                    Toasty.error(getContext(), "Invalid value", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }
    private void change_limit(int position){
        ud = db.getUserData();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.limit_dialog_box, null);
        Button yes = view.findViewById(R.id.yesbtn);
        TextView cancel = view.findViewById(R.id.cancelbtn);
        EditText et = view.findViewById(R.id.et_limit);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().trim().length() == 0){
                    yes.setEnabled(false);
                    yes.setBackgroundColor(getResources().getColor(R.color.Grey));
                    yes.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    yes.setEnabled(true);
                    yes.setBackgroundColor(getResources().getColor(R.color.Green));
                    yes.setTextColor(getResources().getColor(R.color.DarkGreen));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    yes.setEnabled(false);
                    yes.setBackgroundColor(getResources().getColor(R.color.Grey));
                    yes.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    yes.setEnabled(true);
                    yes.setBackgroundColor(getResources().getColor(R.color.Green));
                    yes.setTextColor(getResources().getColor(R.color.DarkGreen));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int limit = Integer.parseInt(et.getText().toString().trim());
                if(limit > 40 && limit < 5000){
                    ud.setLimit(limit);
                    db.updateUserData(ud.getName(), ud);
                    db.updateDrinkDataLimit(ud.getLimit());
                    db.updateDrinkDataTarget(ud.getLimit());
                    data.set(position, ud.getLimit()+" mL");
                    Toasty.success(getContext(), "Set Limit Success", Toast.LENGTH_SHORT, true).show();
                    reload_container();
                    alertDialog.dismiss();
                }
                else{
                    Toasty.error(getContext(), "Invalid value of limit", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }
    private void reload_container(){
        Recycler_Report rr = new Recycler_Report(title, data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, rr).commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}