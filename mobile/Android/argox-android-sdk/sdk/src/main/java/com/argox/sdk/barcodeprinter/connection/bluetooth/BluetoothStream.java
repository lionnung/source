package com.argox.sdk.barcodeprinter.connection.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com.argox.sdk.barcodeprinter.connection.ConnectionState;
import com.argox.sdk.barcodeprinter.connection.TwoWayStream;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Ken Ke
 */
public class BluetoothStream extends TwoWayStream {

    private static final String TAG = BluetoothStream.class.getName();
    protected BluetoothDevice device;
    protected BluetoothSocket socket;
    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//fa87c0d0-afac-11de-8a39-0800200c9a66

    public BluetoothStream(BluetoothDevice device) {
        this.device = device;
    }

    public boolean open() throws Exception {
        if (listener != null) {
            listener.onStateChanged(ConnectionState.Connecting);
        }

        try {
            //socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            socket.connect();
            in = socket.getInputStream();
            out = socket.getOutputStream();
            Log.d(TAG, "success to open in/out stream of device " + device.getName());
            if (listener != null) {
                listener.onStateChanged(ConnectionState.Connected);
            }
            return true;
        } catch (IOException ex) {
            // Unable to connect; close the socket and get out
            Log.e(TAG, null, ex);
            try {
                socket.close();
                if (listener != null) {
                    listener.onStateChanged(ConnectionState.Disconnected);
                }
            } catch (IOException closeException) {
                Log.e(TAG, null, closeException);
            }
            throw ex;
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            socket.close();
            if (listener != null) {
                listener.onStateChanged(ConnectionState.Disconnected);
            }
        } catch (IOException ex) {
            Log.e(TAG, null, ex);
        }
    }
}
