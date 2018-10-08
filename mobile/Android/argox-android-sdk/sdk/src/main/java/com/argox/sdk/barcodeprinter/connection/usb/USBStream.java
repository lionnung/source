/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argox.sdk.barcodeprinter.connection.usb;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;
import android.util.Log;
import com.argox.sdk.barcodeprinter.connection.ConnectionState;
import com.argox.sdk.barcodeprinter.connection.TwoWayStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.IOException;

/**
 *
 * @author Ken Ke
 */
public class USBStream extends TwoWayStream {

    private static final String TAG = USBStream.class.getName();
    protected UsbDeviceConnection connection;
    protected UsbDevice device;
    protected UsbInterface intf;
    protected UsbEndpoint endpoint;
    protected int transfercount;

    public USBStream(UsbDevice device, UsbDeviceConnection connection) {
        this.connection = connection;
        this.device = device;
    }

    public boolean open() throws Exception {
        Log.d(TAG, "USB Connection Count = " + device.getInterfaceCount());
        intf = device.getInterface(0);
        boolean result = connection.claimInterface(intf, true);
        Log.d(TAG, "USB claim interface result is " + result);
        if (listener != null) {
            listener.onStateChanged(ConnectionState.Connected);
        }
        for (int i = 0; i < intf.getEndpointCount(); i++) {
            if (intf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_OUT) {
                endpoint = intf.getEndpoint(i);
                break;
            }
        }
        return true;
    }

    @Override
    public int write(byte[] array, int offset, int count) throws IOException {
        transfercount = 0;
        MyThread th = new MyThread(array, offset, count);

        try {
            th.t.join();
        } catch (InterruptedException e) {
        }
        return transfercount;
    }

/*之前寫的程式碼, 用在手機上在超過4KB以上的傳檔會有傳不出的問題, 所以才會有上面的新版本的寫法.
    public int write(byte[] array, int offset, int count) throws IOException {
        final byte[] bytes = new byte[count];
        System.arraycopy(array, offset, bytes, 0, count);
        Thread t = new Thread(new Runnable() {

            public void run() {
                connection.bulkTransfer(endpoint, bytes, bytes.length, 0); //do in another thread
//                Log.d(TAG, "Endpoint count is " + intf.getEndpointCount());
//                for (int i = 0; i < intf.getEndpointCount(); i++) {
//                    UsbEndpoint endpoint = intf.getEndpoint(i);
//                    UsbRequest req = new UsbRequest();
//                    req.initialize(connection, endpoint);
//                    ByteBuffer output_buffer = ByteBuffer.wrap(bytes);
//                    req.queue(output_buffer, bytes.length);
//                    if (connection.requestWait() == req) {
//                        output_buffer.clear();
//                    } else {
//                        Log.d(TAG, "No USBRequest received from endpoint "+i);
//                    }
//                }
            }
        });
        t.start();
        return array.length;
    }
*/

    @Override
    public void close() {
        try {
            connection.releaseInterface(intf);
            connection.close();
            if (listener != null) {
                listener.onStateChanged(ConnectionState.Disconnected);
            }
        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
    }
    
    class MyThread implements Runnable {
        Thread t;
        final byte[] bytes;

        MyThread(byte[] array, int offset, int count) {
            bytes = new byte[count];
            System.arraycopy(array, offset, bytes, 0, count);
            t = new Thread(this);
            t.start();
        }

        public void run() {
            int r = 0;
            int pos = 0;
            int len = bytes.length;
            int blk = 16*1024;
            byte[] data = new byte[blk];

            while (len > 0) {
                if (len < blk)
                    blk = len;
                System.arraycopy(bytes, pos, data, 0, blk);
                r = connection.bulkTransfer(endpoint, data, blk, 0);
                if (0 > r) {
                    transfercount = pos;
                    return;
                }
                pos = pos + r;
                len = len - r;
            }
        }
    }
}
