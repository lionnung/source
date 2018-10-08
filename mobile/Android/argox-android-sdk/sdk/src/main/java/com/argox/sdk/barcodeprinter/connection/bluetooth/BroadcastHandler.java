package com.argox.sdk.barcodeprinter.connection.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 *
 * @author Ken Ke
 */
public interface BroadcastHandler {

    public void onDeviceFound(BluetoothDevice device);

    public void onDeviceBonded(BluetoothDevice device);

    public void onDiscoveryFinished();

}
