package com.argox.sdk.barcodeprinter.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 * @author Ken Ke
 */
public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = BluetoothBroadcastReceiver.class.getName();
    protected BroadcastHandler handler;

    public BluetoothBroadcastReceiver(BroadcastHandler handler) {
        this.handler = handler;
    }

    public synchronized void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            handler.onDeviceFound(device);
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            handler.onDiscoveryFinished();
        }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getBondState()==BluetoothDevice.BOND_BONDED){
                Log.d(TAG, "device "+device.getName()+" bonded!");
                handler.onDeviceBonded(device);
            }else{
                Log.e(TAG, "device "+device.getName()+" is not bonded!");
            }
        }
    }
}
