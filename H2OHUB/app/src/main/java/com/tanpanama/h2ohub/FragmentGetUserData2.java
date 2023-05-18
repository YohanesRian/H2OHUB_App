package com.tanpanama.h2ohub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tanpanama.h2ohub.Data.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGetUserData2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGetUserData2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserData ud;
    View v;


    public FragmentGetUserData2() {
        // Required empty public constructor
    }

    public FragmentGetUserData2(UserData ud) {
        this.ud = ud;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGetUserData2.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGetUserData2 newInstance(String param1, String param2) {
        FragmentGetUserData2 fragment = new FragmentGetUserData2();
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
        v = inflater.inflate(R.layout.fragment_get_user_data2, container, false);
        Button back = (Button) v.findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentStepViewGetStarted fs = (FragmentStepViewGetStarted) getFragmentManager().findFragmentById(R.id.container_StepView);
                fs.prevStep();

                FragmentGetUserData1 fgud = new FragmentGetUserData1(ud);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.container_GetUserData, fgud).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return v;
    }
}