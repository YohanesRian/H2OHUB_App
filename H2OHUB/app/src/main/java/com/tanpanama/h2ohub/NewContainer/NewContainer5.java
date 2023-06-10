package com.tanpanama.h2ohub.NewContainer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tanpanama.h2ohub.Dashboard.Dashboard;
import com.tanpanama.h2ohub.Data.CupsData;
import com.tanpanama.h2ohub.GetStarted.GetStarted2_1_2;
import com.tanpanama.h2ohub.Handler.database;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewContainer5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewContainer5 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private String cup_name;
    private int empty_weight = 0;
    private int full_weight = 0;
    private Uri image_cup;

    public NewContainer5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewContainer5.
     */
    // TODO: Rename and change types and number of parameters
    public static NewContainer5 newInstance(String param1, String param2) {
        NewContainer5 fragment = new NewContainer5();
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
            empty_weight = getArguments().getInt("empty_weight");
            full_weight = getArguments().getInt("full_weight");
            String imagePath = getArguments().getString("image");
            image_cup = Uri.parse(imagePath);

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_container5, container, false);

        EditText name = v.findViewById(R.id.ETName);
        Button save = v.findViewById(R.id.btnSave);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().trim().length() == 0){
                    save.setEnabled(false);
                    save.setBackgroundColor(getResources().getColor(R.color.Grey));
                    save.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    save.setEnabled(true);
                    save.setBackgroundColor(getResources().getColor(R.color.Green));
                    save.setTextColor(getResources().getColor(R.color.DarkGreen));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    save.setEnabled(false);
                    save.setBackgroundColor(getResources().getColor(R.color.Grey));
                    save.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    save.setEnabled(true);
                    save.setBackgroundColor(getResources().getColor(R.color.Green));
                    save.setTextColor(getResources().getColor(R.color.DarkGreen));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cup_name = name.getText().toString();
                database db = new database(getActivity().getApplicationContext());
                if(db.isSameNameCups(cup_name)){
                    CupsData cd = new CupsData();
                    cd.setName(cup_name);
                    cd.setEmpty_weight(empty_weight);
                    cd.setFull_weight(full_weight);
                    cd.setPicture(getContext(), image_cup);
                    if(db.addCups(cd)){
                        Toasty.success(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(getContext(), Dashboard.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
                else{
                    Toasty.error(getActivity().getApplicationContext(), cup_name + " is already used.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        return v;
    }
}