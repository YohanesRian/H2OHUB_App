package com.tanpanama.h2ohub.Handler;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tanpanama.h2ohub.Dashboard.Dashboard;
import com.tanpanama.h2ohub.NewContainer.NewContainer1;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class bluetoothHandler {
    private Activity activity;
    private Context ctx;
    private BluetoothAdapter ba;
    private BluetoothDevice bd;
    private BluetoothSocket bs;
    private InputStream inputStream;
    private OutputStream outputStream;

    public bluetoothHandler(Activity activity, Context ctx){
        this.activity = activity;
        this.ctx = ctx;

        BluetoothManager bm = (BluetoothManager) getSystemService(this.ctx, BluetoothManager.class);
        ba = bm.getAdapter();
    }
    public boolean haveBluetooth(){
        return ba != null;
    }

    public boolean isEnabled(){
        return ba.isEnabled();
    }
    public boolean isConnected(){
        return bs.isConnected();
    }

    public InputStream getInputStream(){
        return inputStream;
    }

    public void sendData(String data){
        if(isConnected()){
            try{
                outputStream.write(data.getBytes());
            }catch(Exception E){
                Toasty.error(activity.getApplicationContext(), "Unable to communication to H2OHUB_Dispenser", Toast.LENGTH_SHORT, true).show();
            }
        }
        else{
            Toasty.error(activity.getApplicationContext(), "Unable to connect to H2OHUB_Dispenser", Toast.LENGTH_SHORT, true).show();
        }
    }

    public boolean searchConnection(){
        boolean success = false;
        Set<BluetoothDevice> bds = ba.getBondedDevices();
        for(BluetoothDevice device : bds){
            if(device.getName().equals("H2OHUB_Dispenser")){
                bd = device;
                ba.cancelDiscovery();
                break;
            }
        }
        if(bd != null){
            try{
                bs = bd.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                bs.connect();
                if(isConnected()){
                    inputStream = bs.getInputStream();
                    outputStream = bs.getOutputStream();
                    success = true;
                }
            }catch(Exception E){
                Toasty.error(activity.getApplicationContext(), "Unable to connect to H2OHUB_Dispenser", Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(activity, Dashboard.class);
                ctx.startActivity(intent);
                activity.finish();
            }
        }
        else{
            Toasty.error(activity.getApplicationContext(), "Please add H2OHUB_Dispenser to your bluetooth", Toast.LENGTH_SHORT, true).show();
        }
        return success;
    }

    public void disabledBT(){
        if(ba.isEnabled()){
            ba.disable();
        }
    }
}
