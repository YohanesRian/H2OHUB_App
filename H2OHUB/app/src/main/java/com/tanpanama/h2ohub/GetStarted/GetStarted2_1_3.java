package com.tanpanama.h2ohub.GetStarted;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.Handler.dateHandler;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

import java.time.Year;
import java.time.YearMonth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetStarted2_1_3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetStarted2_1_3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;

    private UserData ud = new UserData();

    public GetStarted2_1_3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetStarted2_1_3.
     */
    // TODO: Rename and change types and number of parameters
    public static GetStarted2_1_3 newInstance(String param1, String param2) {
        GetStarted2_1_3 fragment = new GetStarted2_1_3();
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
        v = inflater.inflate(R.layout.fragment_get_started2_1_3, container, false);

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            ud.setName(name);
            String gender = getArguments().getString("gender");
            ud.setGender(gender);
        }

        Button next = (Button) v.findViewById(R.id.btnNext);
        Button back = (Button) v.findViewById(R.id.btnBack);

        NumberPicker year = (NumberPicker) v.findViewById(R.id.Yearnp);
        NumberPicker month = (NumberPicker) v.findViewById(R.id.Monthnp);
        NumberPicker day = (NumberPicker) v.findViewById(R.id.Daynp);

        String[] list_month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        year.setMinValue(Year.now().getValue() - 110);
        year.setMaxValue(Year.now().getValue() - 8);
        year.setValue(year.getMaxValue());
        year.setWrapSelectorWheel(false);

        month.setMinValue(1);
        month.setMaxValue(12);
        month.setDisplayedValues(list_month);
        month.setWrapSelectorWheel(false);

        day.setMinValue(1);
        day.setMaxValue(YearMonth.of(year.getValue(), month.getValue()).lengthOfMonth());
        day.setValue(day.getMinValue());
        day.setWrapSelectorWheel(false);

        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day.setMaxValue(YearMonth.of(year.getValue(), month.getValue()).lengthOfMonth());
            }
        });

        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day.setMaxValue(YearMonth.of(year.getValue(), month.getValue()).lengthOfMonth());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepView fs = (StepView) getFragmentManager().findFragmentById(R.id.StepView_Container);
                fs.prevStep();

                Bundle bundle = new Bundle();
                bundle.putString("name",ud.getName());

                GetStarted2_1_2 gs = new GetStarted2_1_2();
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
                String dob = new String();
                if(month.getValue() > 10){
                    dob = year.getValue() + "-0" + month.getValue() + "-"+ day.getValue();
                }
                else{
                    dob = year.getValue() + "-" + month.getValue() + "-"+ day.getValue();
                }
                dateHandler dh = new dateHandler();
                dh.setDateY_M_D(dob);
                ud.setDate_of_birth(dh.getIntDate());

                Bundle bundle = new Bundle();
                bundle.putString("name", ud.getName());
                bundle.putString("gender", ud.getGender());
                bundle.putInt("dob", ud.getDate_of_birth());


                GetStarted2_1_4 gs = new GetStarted2_1_4();
                gs.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.GetData_Container, gs).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return v;
    }
}