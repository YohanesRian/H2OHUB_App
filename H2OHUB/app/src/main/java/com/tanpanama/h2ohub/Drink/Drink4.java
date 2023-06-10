package com.tanpanama.h2ohub.Drink;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tanpanama.h2ohub.Dashboard.Dashboard;
import com.tanpanama.h2ohub.Handler.database;
import com.tanpanama.h2ohub.R;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Drink4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Drink4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private int dispensed = 0;
    public Drink4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Drink4.
     */
    // TODO: Rename and change types and number of parameters
    public static Drink4 newInstance(String param1, String param2) {
        Drink4 fragment = new Drink4();
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
            dispensed = getArguments().getInt("dispensed");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_drink4, container, false);
        database db = new database(getActivity().getApplicationContext());
        Button next = v.findViewById(R.id.btnNext);
        if(db.addTodayUserDrink(dispensed)){
            Toasty.success(getContext(), "Your Drink Data Has Been Saved", Toast.LENGTH_SHORT, true).show();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Dashboard.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }
}