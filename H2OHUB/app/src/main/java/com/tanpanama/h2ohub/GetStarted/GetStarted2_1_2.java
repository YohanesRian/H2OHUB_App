package com.tanpanama.h2ohub.GetStarted;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetStarted2_1_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetStarted2_1_2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    private UserData ud = new UserData();
    private boolean animcheck = false;

    public GetStarted2_1_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetStarted2_1_2.
     */
    // TODO: Rename and change types and number of parameters
    public static GetStarted2_1_2 newInstance(String param1, String param2) {
        GetStarted2_1_2 fragment = new GetStarted2_1_2();
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
        v = inflater.inflate(R.layout.fragment_get_started2_1_2, container, false);

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            ud.setName(name);
        }
        Button next = (Button) v.findViewById(R.id.btnNext);
        Button back = (Button) v.findViewById(R.id.btnBack);

        RadioButton m = (RadioButton) v.findViewById(R.id.Malerb);
        RadioButton f = (RadioButton) v.findViewById(R.id.Femalerb);

        m.setBackgroundColor(getResources().getColor(R.color.Radio1));
        f.setBackgroundColor(getResources().getColor(R.color.Radio1));

        m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(animcheck){
                        ObjectAnimator colorFade1 = ObjectAnimator.ofObject(m, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.Radio1), getResources().getColor(R.color.Green));
                        ObjectAnimator colorFade2 = ObjectAnimator.ofObject(f, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.Green), getResources().getColor(R.color.Radio1));
                        colorFade1.setDuration(150);
                        colorFade2.setDuration(150);
                        colorFade1.start();
                        colorFade2.start();
                    }
                    else{
                        ObjectAnimator colorFade1 = ObjectAnimator.ofObject(m, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.Radio1), getResources().getColor(R.color.Green));
                        colorFade1.setDuration(150);
                        colorFade1.start();
                    }


                    next.setEnabled(true);
                    if(!animcheck){
                        ObjectAnimator colorFade1 = ObjectAnimator.ofObject(next, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.Grey), getResources().getColor(R.color.Green));
                        ObjectAnimator colorFade2 = ObjectAnimator.ofObject(next, "textColor", new ArgbEvaluator(), getResources().getColor(R.color.DarkGrey), getResources().getColor(R.color.DarkGreen));
                        colorFade1.setDuration(150);
                        colorFade2.setDuration(150);
                        colorFade1.start();
                        colorFade2.start();
                        animcheck = true;
                    }
                }
            }
        });

        f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(animcheck){
                        ObjectAnimator colorFade1 = ObjectAnimator.ofObject(f, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.Radio1), getResources().getColor(R.color.Green));
                        ObjectAnimator colorFade2 = ObjectAnimator.ofObject(m, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.Green), getResources().getColor(R.color.Radio1));
                        colorFade1.setDuration(150);
                        colorFade2.setDuration(150);
                        colorFade1.start();
                        colorFade2.start();
                    }
                    else{
                        ObjectAnimator colorFade1 = ObjectAnimator.ofObject(f, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.Radio1), getResources().getColor(R.color.Green));
                        colorFade1.setDuration(150);
                        colorFade1.start();
                    }

                    next.setEnabled(true);
                    if(!animcheck){
                        ObjectAnimator colorFade1 = ObjectAnimator.ofObject(next, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.Grey), getResources().getColor(R.color.Green));
                        ObjectAnimator colorFade2 = ObjectAnimator.ofObject(next, "textColor", new ArgbEvaluator(), getResources().getColor(R.color.DarkGrey), getResources().getColor(R.color.DarkGreen));
                        colorFade1.setDuration(150);
                        colorFade2.setDuration(150);
                        colorFade1.start();
                        colorFade2.start();
                        animcheck = true;
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepView fs = (StepView) getFragmentManager().findFragmentById(R.id.StepView_Container);
                fs.prevStep();

                GetStarted2_1_1 gs = new GetStarted2_1_1();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.GetData_Container, gs).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepView fs = (StepView) getFragmentManager().findFragmentById(R.id.StepView_Container);
                fs.nextStep();
                if(m.isChecked()){
                    ud.setGender("male");
                }
                else{
                    ud.setGender("female");
                }

                Bundle bundle = new Bundle();
                bundle.putString("name", ud.getName());
                bundle.putString("gender", ud.getGender());


                GetStarted2_1_3 gs = new GetStarted2_1_3();
                gs.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.GetData_Container, gs).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return v;
    }
}