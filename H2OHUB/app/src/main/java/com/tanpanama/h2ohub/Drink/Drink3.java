package com.tanpanama.h2ohub.Drink;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tanpanama.h2ohub.Dashboard.Dashboard;
import com.tanpanama.h2ohub.Handler.bluetoothHandler;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

import java.io.InputStream;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Drink3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Drink3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private bluetoothHandler bh;
    private InputStream inputStream;
    private RxThread rxThread;
    private String RxResult = "";
    private int dispensed = 0;

    public Drink3() {
        // Required empty public constructor
    }

    public Drink3(bluetoothHandler bh, InputStream inputStream) {
        this.bh = bh;
        this.inputStream = inputStream;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Drink3.
     */
    // TODO: Rename and change types and number of parameters
    public static Drink3 newInstance(String param1, String param2) {
        Drink3 fragment = new Drink3();
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
        v = inflater.inflate(R.layout.fragment_drink3, container, false);

        rxThread = new RxThread();
        rxThread.start();

        return v;
    }

    class RxThread extends Thread{
        boolean isRunning;
        RxThread(){
            isRunning = true;
        }

        @Override
        public void run() {
            while(isRunning){
                try {
                    if(inputStream.available() > 0){
                        byte[] rx = new byte[30];
                        inputStream.read(rx);
                        for(int i = 0; i < rx.length; i++){
                            char x = (char) rx[i];
                            if((x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z') || (x >= '0' && x <= '9')){
                                RxResult += x;
                            }
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!RxResult.isEmpty()){
                                if(RxResult.equalsIgnoreCase("byebye")){
                                    isRunning = false;
                                    Toasty.success(getActivity().getApplicationContext(), "Dispenser is Finished", Toast.LENGTH_SHORT, true).show();
                                    StepView fs = (StepView) getFragmentManager().findFragmentById(R.id.StepView_Container);
                                    fs.nextStep();
                                    bh.disabledBT();

                                    Drink4 d = new Drink4();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("dispensed", dispensed);
                                    d.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.Inner_Container, d).commit();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                                else {
                                    char[] chars = RxResult.toCharArray();
                                    StringBuilder sb = new StringBuilder();
                                    for(char c : chars){
                                        if(Character.isDigit(c)){
                                            sb.append(c);
                                        }
                                    }
                                    dispensed = Integer.parseInt(sb.toString());
                                }
                                RxResult = "";
                            }
                        }
                    });
                    Thread.sleep(10);
                }catch (Exception E){}
            }
        }
    }
}