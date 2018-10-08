/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argox.sdk.barcodeprinter.demo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.argox.sdk.barcodeprinter.BarcodePrinter;
import com.argox.sdk.barcodeprinter.connection.usb.USBConnection;
import com.argox.sdk.barcodeprinter.emulation.pplz.PPLZ;
import com.argox.sdk.barcodeprinter.emulation.pplz.PPLZFont;
import com.argox.sdk.barcodeprinter.emulation.pplz.PPLZOrient;
import grandroid.view.LayoutMaker;
import grandroid.view.ViewDesigner;
import java.util.HashMap;

/**
 *
 * @author Rovers
 */
public class FrameUSBConnection extends Activity {

    final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    protected UsbDevice device;
    private PendingIntent mPermissionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.

        device = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
        if (device == null) {
            UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
            for (String key : deviceList.keySet()) {
                device = deviceList.get(key);
                if (device.getVendorId() == 2214) {
                    String msg = device.getDeviceName() + ", vendorID=" + device.getVendorId();
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    break;
                }
                Log.d("grandroid", device.getDeviceName() + ", vendorID=" + device.getVendorId());
            }
        }

        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbReceiver, filter);

        LayoutMaker maker = new LayoutMaker(this);
        maker.getLastLayout().setBackgroundColor(Color.WHITE);
        maker.setDrawableDesignWidth(this, 640);
        ViewDesigner vd = new ViewDesigner() {

            @Override
            public Button stylise(Button btn) {
                btn.setBackgroundResource(R.drawable.b1);
                btn.setTextColor(Color.BLACK);
                return btn;
            }

        };
        maker.setDesigner(vd);
        maker.addColLayout(false, maker.layFF());
        {
            maker.setScalablePadding(maker.getLastLayout(), 20, 25, 20, 25);
            if (device != null) {
                maker.addTextView("DeviceName: " + device.getDeviceName());
                maker.addTextView("DeviceId: " + device.getDeviceId());
                maker.addTextView("VendorId: " + device.getVendorId());
                maker.addTextView("ProductId: " + device.getProductId());
                maker.addTextView("DeviceProtocol: " + device.getDeviceProtocol());
                maker.addTextView("DeviceClass: " + device.getDeviceClass());
                Button btnShowCases = maker.addButton("Print \"Hello World\"");
                btnShowCases.setBackgroundResource(R.drawable.b1);
                btnShowCases.setLayoutParams(maker.layAbsolute(0, 25, LinearLayout.LayoutParams.MATCH_PARENT, 100));
                btnShowCases.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        BarcodePrinter<USBConnection, PPLZ> printer = new BarcodePrinter<USBConnection, PPLZ>();
                        //if you use BluetoothConnection instead of TCPConnection , you must implements onActivityResult(...) callback function
                        //with calling printer.getConnection().onActivityResult(...)
                        printer.setConnection(new USBConnection(v.getContext(), device));
                        printer.setEmulation(new PPLZ());

                        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                        if (device != null && !manager.hasPermission(device)) {
                            manager.requestPermission(device, mPermissionIntent);
                        }

                        try {
                            printer.getConnection().open();
                            String text = "Hello World!";
                            printer.getEmulation().getTextUtil().printText(0, 0, PPLZOrient.Clockwise_0_Degrees, PPLZFont.Font_Zero, 20, 20, text.getBytes(), 0);
                            printer.getEmulation().getIOUtil().printOut();
                        } catch (Exception ex) {
                            Log.e("argox_demo", null, ex);
                        }
                    }
                });
            } else {
                maker.addTextView("No usb printer exist.");
            }
            maker.escape();
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (device != null) {
                            Toast.makeText(FrameUSBConnection.this, "Got Permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(FrameUSBConnection.this, "No Permission", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Toast.makeText(FrameUSBConnection.this, "USB device attached", Toast.LENGTH_LONG).show();
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Toast.makeText(FrameUSBConnection.this, "USB device detached", Toast.LENGTH_LONG).show();
            }
        }
    };
}
