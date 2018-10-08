package com.argox.sdk.barcodeprinter.connection;

/**
 *
 * @author Ken Ke
 */
public interface PrinterDataListener {

    public void onReceive(byte[] bytes);
}
