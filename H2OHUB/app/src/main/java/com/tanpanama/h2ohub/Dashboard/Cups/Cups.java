package com.tanpanama.h2ohub.Dashboard.Cups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tanpanama.h2ohub.Adapter.cups_recycler_adapter;
import com.tanpanama.h2ohub.Dashboard.cups_recycler_interface;
import com.tanpanama.h2ohub.Data.CupsData;
import com.tanpanama.h2ohub.Data.DrinkData;
import com.tanpanama.h2ohub.Drink.Drink;
import com.tanpanama.h2ohub.Handler.database;
import com.tanpanama.h2ohub.NewContainer.NewContainer;
import com.tanpanama.h2ohub.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cups#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cups extends Fragment implements cups_recycler_interface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    private ArrayList<CupsData> cd;
    private RecyclerView rv;
    private cups_recycler_adapter cra;
    private  database db;
    private  AlertDialog alertDialog;
    private CupsData cupsData;

    public Cups() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cups.
     */
    // TODO: Rename and change types and number of parameters
    public static Cups newInstance(String param1, String param2) {
        Cups fragment = new Cups();
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
        v = inflater.inflate(R.layout.fragment_cups, container, false);

        Button newCups = (Button) v.findViewById(R.id.btnNewCups);
        rv = v.findViewById(R.id.recycler_cups);
        setUpRecycler();

        newCups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewContainer.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void setUpRecycler(){
        db = new database(getActivity().getApplicationContext());
        cd = db.getCups();
        if(cd.size() > 0){
            cra = new cups_recycler_adapter(getContext(), cd, this, true);
            rv.setAdapter(cra);
            rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
            runRecyclerAnimation();
        }
    }

    private void runRecyclerAnimation(){
        Context ctx = rv.getContext();
        LayoutAnimationController glac = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_cups_grid_animation);
        rv.setLayoutAnimation(glac);
        rv.scheduleLayoutAnimation();
    }

    @Override
    public void onItemClicked(int position) {
        CupsData cd = this.cd.get(position);
        DrinkData dd = db.getUserDrink(db.getUserData());
        int limit = dd.getMax_drink() - dd.getTotal_drink();
        if(limit >= 10){
            Intent intent = new Intent(getContext(), Drink.class);
            intent.putExtra("empty_weight", Integer.toString(cd.getEmpty_weight()));
            intent.putExtra("full_weight", Integer.toString(cd.getFull_weight()));
            intent.putExtra("limit", Integer.toString(limit));
            startActivity(intent);
            getActivity().finish();
        }
        else{
            Toasty.error(getContext(), "Your limit is too close to the amount of water you have drunk.", Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onItemLongClicked(int position) {
        cupsData = cd.get(position);
        if(cupsData != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            View view = getLayoutInflater().inflate(R.layout.delete_cup_dialog_box, null);
            Button delete = view.findViewById(R.id.deletebtn);
            TextView cancel = view.findViewById(R.id.cancelbtn);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(db.deleteCups(cupsData)){
                        cd.remove(position);
                        cra.notifyItemRemoved(position);
                        Toasty.success(getContext(), "Successfully delete "+ cupsData.getName(), Toast.LENGTH_SHORT, true).show();
                        alertDialog.dismiss();
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
    }
}