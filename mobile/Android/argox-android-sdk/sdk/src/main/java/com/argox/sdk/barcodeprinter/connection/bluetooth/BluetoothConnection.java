package com.argox.sdk.barcodeprinter.connection.bluetooth;

import android.app.Activity;
import static android.app.Activity.RESULT_OK;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;
import com.argox.sdk.barcodeprinter.connection.PrinterConnection;
import com.argox.sdk.barcodeprinter.connection.bluetooth.BluetoothDeviceManager;
import java.lang.reflect.Method;

/**
 *
 * @author Ken Ke
 */
public class BluetoothConnection extends PrinterConnection<BluetoothStream> implements BroadcastHandler {

    private static final String TAG = BluetoothConnection.class.getName();
    private static final boolean DEBUG = true;
    protected static final int REQUEST_ENABLE_BT = 0;
    public static final int STATE_NONE = 0; // we're doing nothing
    public static final int STATE_LISTEN = 1; // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3; // now connected to a remote device
    protected BluetoothAdapter adapter = null;
    protected BluetoothDevice printerDevice;
    // Constants that indicate the current connection state
    //private int state;
    protected Activity activity;
    protected String targetPrinterName;
    protected BluetoothBroadcastReceiver receiver;
    BluetoothDeviceManager manager;

    public BluetoothConnection(Activity activity) {
        this.activity = activity;
    }

    public BluetoothConnection(Activity activity, String targetPrinterName) {
        this.activity = activity;
        this.targetPrinterName = targetPrinterName;
    }

    public String getTargetPrinter() {
        return targetPrinterName;
    }

    public void setTargetPrinter(String targetPrinterName) {
        this.targetPrinterName = targetPrinterName;
    }

    public void open() throws Exception {
        if (!closed) {
            adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                // Device does not support Bluetooth
                Toast.makeText(activity, "Your device doesn't support bluetooth.", Toast.LENGTH_SHORT).show();
                //showDialog("不支援藍芽");
            } else {
                if (!adapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    searchPrinter();
                }
            }
        }
    }

    protected void searchPrinter() {
        if (adapter != null) {
            if (targetPrinterName != null) {
                for (BluetoothDevice device : adapter.getBondedDevices()) {
                    if (targetPrinterName.equalsIgnoreCase(device.getName())) {
                        printerDevice = device;
                        break;
                    }
                }
            }
            if (printerDevice != null) {
                connectPrinter();
            } else {
                if (manager == null) {
                    manager = new BluetoothDeviceManager(activity);
                }
                manager.pickDevice(new BluetoothDeviceManager.BluetoothDevicePickResultHandler() {

                    public void onDevicePicked(BluetoothDevice device) {
                        printerDevice = device;
                        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                            pairDevice(device);
                        } else {
                            connectPrinter();
                        }
                    }
                });
//                if (receiver != null) {
//                    activity.unregisterReceiver(receiver);
//                }
//                receiver = new BluetoothBroadcastReceiver(this);
//                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                activity.registerReceiver(receiver, filter);
//                filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//                activity.registerReceiver(receiver, filter);
//
//                adapter.startDiscovery();
            }
        }
    }

    protected void connectPrinter() {
        if (printerDevice != null) {
            if (printerDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                Log.e(TAG, "bluetooth device " + printerDevice.getName() + " is not bonded.");
            }
            Log.d(TAG, "try to connect bluetooth device: " + printerDevice.getName() + " (" + printerDevice.getAddress() + ")");
            stream = new BluetoothStream(printerDevice);
            stream.setStateListener(listener);
            openStream(stream);
        }
    }

    private void pairDevice(BluetoothDevice device) {
        receiver = new BluetoothBroadcastReceiver(BluetoothConnection.this);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        activity.registerReceiver(receiver, filter);
        try {
            Log.d(TAG, "Start Pairing...");
//            byte[] pinBytes = (byte[]) BluetoothDevice.class.getMethod("convertPinToBytes", String.class).invoke(BluetoothDevice.class, "0000");
//            BluetoothDevice.class.getMethod("setPin", byte[].class).invoke(device, pinBytes);
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);

            Log.d(TAG, "Pairing finished.");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void close() {
        super.close();
        if (receiver != null) {
            activity.unregisterReceiver(receiver);
            receiver = null;
        }
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    public void onDeviceFound(BluetoothDevice device) {
        Log.d(TAG, "found bluetooth device: " + device.getName() + " (" + device.getAddress() + ")");
        if (targetPrinterName != null && targetPrinterName.equalsIgnoreCase(device.getName())) {
            adapter.cancelDiscovery();
            activity.unregisterReceiver(receiver);
            receiver = null;
            printerDevice = device;
            connectPrinter();
        }
    }

    public void onDeviceBonded(BluetoothDevice device) {
        if (receiver != null) {
            activity.unregisterReceiver(receiver);
        }
        connectPrinter();
    }

    public void onDiscoveryFinished() {
    }

    @Override
    public String toString() {
        if (printerDevice == null) {
            return "bt:";
        } else {
            return "bt:" + printerDevice.getName() + "@" + printerDevice.getAddress() + ")";
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ENABLE_BT == requestCode) {
            if (resultCode == RESULT_OK) {
                // user open bluetooth
                searchPrinter();
            } else {
                // user not open bluetooth
                Toast.makeText(activity, "Bluetooth is not ready, abort print job.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
