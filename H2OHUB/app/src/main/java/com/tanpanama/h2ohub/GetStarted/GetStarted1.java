package com.tanpanama.h2ohub.GetStarted;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetStarted1#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GetStarted1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View v;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetStarted1.
     */
    // TODO: Rename and change types and number of parameters
    public static GetStarted1 newInstance(String param1, String param2) {
        GetStarted1 fragment = new GetStarted1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GetStarted1() {
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
        v = inflater.inflate(R.layout.fragment_get_started1, container, false);
        Button btn = (Button) v.findViewById(R.id.GetStartedbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStarted2 gs = new GetStarted2();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.Main_Container, gs).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return v;
    }
}