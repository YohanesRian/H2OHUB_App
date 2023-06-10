package com.tanpanama.h2ohub.Dashboard.Report;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.broooapps.graphview.CurveGraphConfig;
import com.broooapps.graphview.CurveGraphView;
import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.PointMap;
import com.tanpanama.h2ohub.Adapter.drink_data_recycler_adapter;
import com.tanpanama.h2ohub.Data.DrinkData;
import com.tanpanama.h2ohub.Handler.database;
import com.tanpanama.h2ohub.Handler.dateHandler;
import com.tanpanama.h2ohub.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Report#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Report extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;

    private PointMap drink_limit;
    private PointMap drink_target;
    private PointMap drank;
    private CurveGraphView curveGraphView;
    private GraphData gd_limit;
    private GraphData gd_target;
    private GraphData gd_drank;

    private TextSwitcher my;

    private int size;
    private int max_value;

    private int month;
    private int year;
    private RecyclerView rv;
    private database db;
    private ArrayList<DrinkData> aldd;
    private drink_data_recycler_adapter ddra;
    private boolean isNextMonth;
    private String[] month_name = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public Report() {
        // Required empty public constructor
    }
    public Report(boolean isNextMonth) {
        this.isNextMonth = isNextMonth;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Report.
     */
    // TODO: Rename and change types and number of parameters
    public static Report newInstance(String param1, String param2) {
        Report fragment = new Report();
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
        v = inflater.inflate(R.layout.fragment_report, container, false);
        db = new database(getActivity().getApplicationContext());
        ImageView left = (ImageView) v.findViewById(R.id.left);
        ImageView right = (ImageView) v.findViewById(R.id.right);
        curveGraphView = (CurveGraphView) v.findViewById(R.id.reportGraph);
        my = (TextSwitcher) v.findViewById(R.id.Month_Year);
        rv = v.findViewById(R.id.recycler_drink);

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        right.setVisibility(View.INVISIBLE);

        setText();
        setData(getContext(), year, month);
        setContainer();


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(right.getVisibility() == View.INVISIBLE){
                    right.setVisibility(View.VISIBLE);
                }
                isNextMonth = false;
                prev_month();
                setData(getContext(), year, month);
                updateGraph(getContext());
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_month();
                if(month - 1 == Calendar.getInstance().get(Calendar.MONTH) && year == Calendar.getInstance().get(Calendar.YEAR)){
                    right.setVisibility(View.INVISIBLE);
                }
                isNextMonth = true;
                setData(getContext(), year, month);
                updateGraph(getContext());
            }
        });

        return v;
    }

    void prev_month(){
        if(month == 0){
            month = 11;
            year--;
        }
        else{
            month--;
        }
        my.setInAnimation(getContext(), R.anim.slide_in_left_to_right);
        my.setOutAnimation(getContext(), R.anim.slide_out_left_to_right);
        setText();
    }

    void next_month(){
        if(month == 11){
            month = 0;
            year++;
        }
        else{
            month++;
        }

        my.setInAnimation(getContext(), R.anim.slide_in_right_to_left);
        my.setOutAnimation(getContext(), R.anim.slide_out_right_to_left);
        setText();
    }

    private void setText(){
        String result = month_name[month] + " " + year;
        my.setText(result);
    }

    private void setData(Context ctx, int year, int month){
        drink_limit = new PointMap();
        drink_target = new PointMap();
        drank = new PointMap();
        size = 0;
        max_value = 0;
        dateHandler dh = new dateHandler();

        aldd = db.getReportUserDrink(month, year);
        for(int i = aldd.size() - 1; i >= 0; i--){
            dh.setDate(aldd.get(i).getDate());
            size = dh.getDay() - 1;
            drink_limit.addPoint(size, aldd.get(i).getMax_drink());
            drink_target.addPoint(size, aldd.get(i).getTarget_drink());
            drank.addPoint(size, aldd.get(i).getTotal_drink());
            if(max_value < aldd.get(i).getMax_drink()){
                max_value = aldd.get(i).getMax_drink() + 10;
            }
        }
        updateGraph(ctx);
        setContainer();
    }

    private void setContainer(){
        list_recycler_drink_data lrdd = new list_recycler_drink_data(aldd);
        if(isNextMonth){
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.drink_data_container, lrdd).commit();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        else{
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.drink_data_container, lrdd).commit();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
    private void updateGraph(Context ctx){
        curveGraphView.configure(
                new CurveGraphConfig.Builder(ctx)
                        .setAxisColor(R.color.Logo)                                             // Set X and Y axis line color stroke.
                        .setGuidelineColor(R.color.Background1)                                 // Set color of the visible guidelines.
                        .setNoDataMsg(" No Data ")                                              // Message when no data is provided to the view.
                        .setxAxisScaleTextColor(R.color.Background1)                                  // Set X axis scale text color.
                        .setyAxisScaleTextColor(R.color.Background1)                                  // Set Y axis scale text color
                        .setAnimationDuration(2000)                                             // Set animation duration to be used after set data.
                        .build()
        );

        gd_limit = GraphData.builder(ctx)
                .setPointMap(drink_limit)
                .setGraphStroke(R.color.Red)
                .setPointColor(R.color.Red)
                .setStraightLine(false)
                .animateLine(true)
                .setPointRadius(0)
                .build();

        gd_target = GraphData.builder(ctx)
                .setPointMap(drink_target)
                .setGraphStroke(R.color.Blue)
                .setPointColor(R.color.Blue)
                .setStraightLine(false)
                .animateLine(true)
                .setPointRadius(0)
                .build();

        gd_drank = GraphData.builder(getContext())
                .setPointMap(drank)
                .setGraphStroke(R.color.DarkGreen)
                .setPointColor(R.color.DarkGreen)
                .setGraphGradient(R.color.DarkGreen, R.color.Transparent)
                .setStraightLine(false)
                .animateLine(true)
                .setPointRadius(8)
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                curveGraphView.setData(size, max_value, gd_limit, gd_target, gd_drank);
            }
        }, 100);
    }

}