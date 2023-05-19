package com.tanpanama.h2ohub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuhart.stepview.StepView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentStepView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStepView extends Fragment {

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

    int step_number;

    public FragmentStepView() {
        // Required empty public constructor
    }
    public FragmentStepView(int step_number) {
        this.step_number = step_number;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentStepView.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentStepView newInstance(String param1, String param2) {
        FragmentStepView fragment = new FragmentStepView();
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
        v = inflater.inflate(R.layout.fragment_step_view, container, false);
        stepView = v.findViewById(R.id.stepview_getstarted);
        stepView.getState().animationType(StepView.ANIMATION_ALL).stepsNumber(this.step_number).animationDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).commit();
        return v;
    }

    protected void nextStep() {
        if(stepIndex < this.step_number){
            stepIndex++;
            stepView.go(stepIndex, true);
        }
    }
    protected void prevStep() {
        if(stepIndex >= 0){
            stepIndex--;
            stepView.go(stepIndex, true);
        }
    }
}