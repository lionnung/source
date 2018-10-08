package com.argox.sdk.barcodeprinter.connection.usb;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;
import com.argox.sdk.barcodeprinter.connection.PrinterConnection;
import com.argox.sdk.barcodeprinter.BarcodePrinterConnectionException;
import java.util.HashMap;

/**
 *
 * @author Ken Ke
 */
public class USBConnection extends PrinterConnection<USBStream> {

    private static final String TAG = USBConnection.class.getName();
    protected UsbDevice device;
    //protected USBStream usbStream;
    protected UsbManager usbManager;
    protected Context context;

    public USBConnection(Context context) {
        this.context = context;
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        for (String key : deviceList.keySet()) {
            device = deviceList.get(key);
            break;
        }
    }

    public USBConnection(Context context, UsbDevice device) {
        this.device = device;
        this.context = context;
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    public UsbDevice getDevice() {
        return device;
    }

    public void setDevice(UsbDevice device) {
        this.device = device;
    }

    public void open() throws Exception {
        if (device == null) {
            throw new BarcodePrinterConnectionException("cannot found any usb device");
        }
        if (!closed) {
            Log.d(TAG, "has permission to control device? " + usbManager.hasPermission(device));
            UsbDeviceConnection connection = usbManager.openDevice(device);
            if (connection == null) {
                Toast.makeText(context, "can't open usb device", Toast.LENGTH_SHORT).show();
            } else {
                stream = new USBStream(device, connection);
                stream.setStateListener(listener);
                //openStream(stream);
                stream.open();
                connected = true;
            }
        }
    }

    @Override
    public void close() {
        connected = false;
        closed = true;
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    @Override
    public String toString() {
        if (device == null) {
            return "usb:";
        } else {
            return "usb:" + device.getVendorId() + "-" + device.getProductId();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
