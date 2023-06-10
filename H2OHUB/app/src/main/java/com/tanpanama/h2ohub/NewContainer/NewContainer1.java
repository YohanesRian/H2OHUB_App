package com.tanpanama.h2ohub.NewContainer;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.registerReceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tanpanama.h2ohub.Dashboard.Dashboard;
import com.tanpanama.h2ohub.Drink.Drink1;
import com.tanpanama.h2ohub.Handler.bluetoothHandler;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewContainer1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewContainer1 extends Fragment {

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
    private Button connect;

    public NewContainer1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewContainer1.
     */
    // TODO: Rename and change types and number of parameters
    public static NewContainer1 newInstance(String param1, String param2) {
        NewContainer1 fragment = new NewContainer1();
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
        v = inflater.inflate(R.layout.fragment_new_container1, container, false);

        connect = v.findViewById(R.id.btnNext);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButton(connect, false);
                bh = new bluetoothHandler(getActivity(), getContext());
                bluetoothConnect();
            }
        });
        onBackPresssed();
        return v;
    }

    private void onBackPresssed(){
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isEnabled()){
                    Intent intent = new Intent(getActivity(), Dashboard.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }

    private void bluetoothConnect() {
        if (!bh.haveBluetooth()) {
            Toasty.error(getContext(), "This device doesn't have Bluetooth!", Toast.LENGTH_SHORT, true).show();
            Intent intent = new Intent(getActivity(), Dashboard.class);
            startActivity(intent);
            getActivity().finish();
        } else if (!bh.isEnabled()) {
            try {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 10);
            } catch (Exception E) {
                Toasty.error(getContext(), "Please Give Permission to Nearby Devices in Application Permission.", Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                setButton(connect, true);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            startCommunication();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 10){
            if(resultCode == getActivity().RESULT_OK){
                startCommunication();
            }
            else{
                Toasty.error(getActivity().getApplicationContext(), "Please turn on the Bluetooth to use the dispenser.", Toast.LENGTH_SHORT, true).show();
                setButton(connect, true);
            }
        }
    }

    public void startCommunication(){
        try {
            boolean isAvailable = bh.searchConnection();
            if(isAvailable){
                inputStream = bh.getInputStream();
                rxThread = new RxThread();
                rxThread.start();
                bh.sendData("new");
            }
            else{
                setButton(connect, true);
            }
        } catch (Exception E) {
            setButton(connect, true);
            Toasty.error(getContext(), "Please Give Permission to Nearby Devices in Application Permission.", Toast.LENGTH_SHORT, true).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
            setButton(connect, true);
            intent.setData(uri);
            startActivity(intent);
        }
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
                                if(RxResult.equalsIgnoreCase("next")){
                                    Toasty.success(getActivity().getApplicationContext(), "Success to Connect H2OHUB_Dispenser", Toast.LENGTH_SHORT, true).show();

                                    StepView fs = (StepView) getFragmentManager().findFragmentById(R.id.StepView_Container);
                                    fs.nextStep();

                                    isRunning = false;

                                    NewContainer2 nc = new NewContainer2(bh, inputStream);
                                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.Inner_Container, nc).commit();
                                    getActivity().getSupportFragmentManager().popBackStack();
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

    private void setButton(Button btn, boolean isEnabled){
        if(isEnabled){
            btn.setEnabled(true);
            btn.setBackgroundColor(getResources().getColor(R.color.Green));
            btn.setTextColor(getResources().getColor(R.color.DarkGreen));
        }
        else{
            btn.setEnabled(false);
            btn.setBackgroundColor(getResources().getColor(R.color.Grey));
            btn.setTextColor(getResources().getColor(R.color.DarkGrey));
        }
    }
}