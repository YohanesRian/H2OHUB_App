package com.tanpanama.h2ohub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuhart.stepview.StepView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentStepViewGetStarted#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStepViewGetStarted extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View v;
    int stepIndex = 0;
    StepView stepView;

    public FragmentStepViewGetStarted() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentStepViewGetStarted.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentStepViewGetStarted newInstance(String param1, String param2) {
        FragmentStepViewGetStarted fragment = new FragmentStepViewGetStarted();
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
        v = inflater.inflate(R.layout.fragment_step_view_get_started, container, false);
        stepView = v.findViewById(R.id.stepview_getstarted);
        stepView.getState().animationType(StepView.ANIMATION_ALL).stepsNumber(5).animationDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).commit();
        return v;
    }

    protected void nextStep() {
        stepIndex++;
        if(stepIndex < 5){
            stepView.go(stepIndex, true);
        }
    }
}