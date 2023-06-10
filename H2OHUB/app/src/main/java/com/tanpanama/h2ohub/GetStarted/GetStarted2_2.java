package com.tanpanama.h2ohub.GetStarted;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tanpanama.h2ohub.Adapter.level_recyclerview_adapter;
import com.tanpanama.h2ohub.Dashboard.Dashboard;
import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.Handler.database;
import com.tanpanama.h2ohub.MainActivity;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetStarted2_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetStarted2_2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    private RecyclerView rv;
    private UserData ud = new UserData();

    public GetStarted2_2() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetStarted2_2.
     */
    // TODO: Rename and change types and number of parameters
    public static GetStarted2_2 newInstance(String param1, String param2) {
        GetStarted2_2 fragment = new GetStarted2_2();
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
        v = inflater.inflate(R.layout.fragment_get_started2_2, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recycler_level);

        ArrayList<level_serial> als = new ArrayList<>();

        level_recyclerview_adapter adapter = new level_recyclerview_adapter(getContext(), als);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);


        UserData ud = new UserData();
        for(int i = 0; i < ud.getLevel().length ;i++){
            level_serial ls = new level_serial();
            ls.setLevel_desc(ud.getLevel_description()[i]);
            ls.setLevel(ud.getLevel()[i]);
            als.add(ls);
        }
        adapter.SetLevel(als);

        Button save = (Button) v.findViewById(R.id.btnSave);

        if(adapter.getSelected() != null){
            save.setEnabled(true);
            save.setBackgroundColor(getResources().getColor(R.color.Green));
            save.setTextColor(getResources().getColor(R.color.DarkGreen));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getSelected() != null){
                    String level = adapter.getSelected().getLevel();
                    if (getArguments() != null) {
                        String name = getArguments().getString("name");
                        ud.setName(name);
                        String gender = getArguments().getString("gender");
                        ud.setGender(gender);
                        int dob = getArguments().getInt("dob");
                        ud.setDate_of_birth(dob);
                        int height = getArguments().getInt("height");
                        ud.setHeight(height);
                        int weight = getArguments().getInt("weight");
                        ud.setWeight(weight);
                        ud.setActivity_level(level);
                    }
                    database db = new database(getContext());
                    if(db.addUserData(ud)){
                        Intent intent = new Intent(getContext(), Dashboard.class);
                        intent.putExtra("welcome", ud.getName());
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else{
                        Toasty.error(v.getContext(), "Failed", Toast.LENGTH_SHORT, true).show();
                    }
                }
                else{
                    Toasty.warning(v.getContext(), "Please select from these options!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        return v;
    }
}