package com.tanpanama.h2ohub.GetStarted;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetStarted2_1_5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetStarted2_1_5 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    private UserData ud =  new UserData();

    public GetStarted2_1_5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetStarted2_1_5.
     */
    // TODO: Rename and change types and number of parameters
    public static GetStarted2_1_5 newInstance(String param1, String param2) {
        GetStarted2_1_5 fragment = new GetStarted2_1_5();
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
        v = inflater.inflate(R.layout.fragment_get_started2_1_5, container, false);

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            ud.setName(name);
            String gender = getArguments().getString("gender");
            ud.setGender(gender);
            int dob = getArguments().getInt("dob");
            ud.setDate_of_birth(dob);
            int height = getArguments().getInt("height");
            ud.setHeight(height);
        }

        Button next = (Button) v.findViewById(R.id.btnNext);
        Button back = (Button) v.findViewById(R.id.btnBack);

        NumberPicker weight = (NumberPicker) v.findViewById(R.id.Weightnp);
        weight.setMinValue(20);
        weight.setMaxValue(300);
        weight.setValue(70);
        weight.setWrapSelectorWheel(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepView fs = (StepView) getFragmentManager().findFragmentById(R.id.StepView_Container);
                fs.prevStep();

                Bundle bundle = new Bundle();
                bundle.putString("name", ud.getName());
                bundle.putString("gender", ud.getGender());
                bundle.putInt("dob", ud.getDate_of_birth());

                GetStarted2_1_4 gs = new GetStarted2_1_4();
                gs.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.GetData_Container, gs).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepView fs = (StepView) getFragmentManager().findFragmentById(R.id.StepView_Container);
                fs.nextStep();
                ud.setWeight(weight.getValue());

                Bundle bundle = new Bundle();
                bundle.putString("name", ud.getName());
                bundle.putString("gender", ud.getGender());
                bundle.putInt("dob", ud.getDate_of_birth());
                bundle.putInt("height", ud.getHeight());
                bundle.putInt("weight", ud.getWeight());

                GetStarted2_2 gs = new GetStarted2_2();
                gs.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.Inner_Container, gs).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return v;
    }
}