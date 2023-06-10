package com.tanpanama.h2ohub.Dashboard.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tanpanama.h2ohub.Adapter.cups_recycler_adapter;
import com.tanpanama.h2ohub.Dashboard.cups_recycler_interface;
import com.tanpanama.h2ohub.Data.CupsData;
import com.tanpanama.h2ohub.Data.DrinkData;
import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.Drink.Drink;
import com.tanpanama.h2ohub.Handler.database;
import com.tanpanama.h2ohub.Handler.dateHandler;
import com.tanpanama.h2ohub.MainActivity;
import com.tanpanama.h2ohub.NewContainer.NewContainer;
import com.tanpanama.h2ohub.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements cups_recycler_interface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;

    private UserData ud;
    private DrinkData dd;
    private RecyclerView rv;
    private ArrayList<CupsData> cd;
    private cups_recycler_adapter cra;
    private database db;
    private CupsData cupsData;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        v = inflater.inflate(R.layout.fragment_home, container, false);
        database db = new database(getContext());

        ud = db.getUserData();
        dd = db.getUserDrink(ud);

        rv = v.findViewById(R.id.recycler_cups);
        setUpRecycler();

        TextView name = (TextView) v.findViewById(R.id.tv_name);
        TextView limit_perc = (TextView) v.findViewById(R.id.tv_limit_perc);
        TextView last_drink = (TextView) v.findViewById(R.id.tv_last_drink);
        ProgressBar drink_bar = (ProgressBar) v.findViewById(R.id.drink_bar);
        TextView limit = (TextView) v.findViewById(R.id.tv_limit);
        TextView drank = (TextView) v.findViewById(R.id.tv_drank);
        TextView target = (TextView) v.findViewById(R.id.tv_target);

        if(ud != null){
            String t[] = ud.getName().split(" ");
            name.setText(t[0] +"!");
            limit.setText(dd.getMax_drink()+"");
            drank.setText(dd.getTotal_drink()+"");
            target.setText(dd.getTarget_drink()+"");
            last_drink.setText(dd.getLast_drink());
            int x = Math.round(dd.getTotal_drink() * 100 / dd.getMax_drink());
            drink_bar.setProgress(x);
            limit_perc.setText(x+"");
        }

        return v;
    }

    private void setUpRecycler(){
        db = new database(getActivity().getApplicationContext());
        cd = db.getCups();
        if(cd.size() > 0){
            cra = new cups_recycler_adapter(getContext(), cd, this, false);
            rv.setAdapter(cra);
            rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            runRecyclerAnimation();
        }
    }

    private void runRecyclerAnimation(){
        Context ctx = rv.getContext();
        LayoutAnimationController lac = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_cups_horizontal_animation);
        rv.setLayoutAnimation(lac);
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

    }
}