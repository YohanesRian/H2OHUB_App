package com.tanpanama.h2ohub.Dashboard.Setting;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tanpanama.h2ohub.Adapter.setting_recycler_adapter;
import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.Handler.database;
import com.tanpanama.h2ohub.Handler.dateHandler;
import com.tanpanama.h2ohub.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends Fragment{

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

    public Setting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Setting.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting newInstance(String param1, String param2) {
        Setting fragment = new Setting();
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
        v = inflater.inflate(R.layout.fragment_setting, container, false);
        Button btn = v.findViewById(R.id.btnReset);
        db = new database(getActivity().getApplicationContext());

        loadData();
        load_container();

        if(data.get(6).equals("0")){
            btn.setEnabled(false);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_limit();
            }
        });
        return v;
    }

    private void reset_limit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.reset_limit_dialog_box, null);
        Button yes = view.findViewById(R.id.yesbtn);
        TextView cancel = view.findViewById(R.id.cancelbtn);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ud.setLimit(0);
                db.updateUserData(ud.getName(), ud);
                db.updateDrinkDataTarget(ud.getTargetDrinkWater());
                db.updateDrinkDataLimit(ud.getMaxDrinkWater());
                loadData();
                load_container();
                Toasty.success(getContext(), "Reset Limit Success", Toast.LENGTH_SHORT, true).show();
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

    private void load_container(){
        Recycler_Report rr = new Recycler_Report(title, data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, rr).commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void loadData(){
        ud = db.getUserData();
        title = new ArrayList<>();
        data = new ArrayList<>();

        title.add("Name");
        title.add("Date of Birth");
        title.add("Gender");
        title.add("Height");
        title.add("Weight");
        title.add("Activity Level");
        title.add("Drink Limit");
        data.add(ud.getName()+"");
        dateHandler dh = new dateHandler();
        dh.setDate(ud.getDate_of_birth());
        data.add(dh.getDayName() + ", " + dh.getFancyDate());
        data.add(ud.getGender().substring(0, 1).toUpperCase() + ud.getGender().substring(1));
        data.add(ud.getHeight()+" cm");
        data.add(ud.getWeight()+" kg");
        data.add(ud.getActivityLevel()+"");
        data.add(ud.getLimit()+" mL");
    }
}