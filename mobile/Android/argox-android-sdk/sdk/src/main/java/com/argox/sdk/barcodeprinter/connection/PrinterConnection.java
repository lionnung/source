package com.argox.sdk.barcodeprinter.connection;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 印表機連接類別，為抽象類別
 * @author Ken Ke
 * @param <T>
 */
public abstract class PrinterConnection<T extends ITwoWayStream> implements IPrinterConnection {

    private static final String TAG = PrinterConnection.class.getName();
    protected int writeTimeout = 10000;
    protected int readTimeout = 10000;
    protected boolean connected;
    protected PrinterDataListener dataListener;
    protected HandlerThread ht;
    protected Handler handler;
    protected boolean closed;
    protected T stream;
    protected IConnectionStateListener listener;
    protected ConnectionState state;

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean canWrite() {
        return true;
    }

    public boolean canRead() {
        return false;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAsyncRead() {
        return dataListener != null;
    }

    public PrinterDataListener getDataListener() {
        return dataListener;
    }

    public void setDataListener(PrinterDataListener dataListener) {
        this.dataListener = dataListener;
    }

    protected void openStream(final ITwoWayStream stream) {
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    if (stream.open()) {
                        connected = true;
                        if (isAsyncRead() && dataListener != null) {
                            startAsyncListening(stream, dataListener);
                        }
                    }
                } catch (Exception ex) {
                    Log.e(TAG, null, ex);
                }
            }
        });
        t.start();
    }

    protected void startAsyncListening(final ITwoWayStream stream, final PrinterDataListener dataListener) {
        ht = new HandlerThread("argox-printer-read-thread");
        ht.start();
        handler = new Handler(ht.getLooper());
        handler.post(new Runnable() {

            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int BUFFER_SIZE = 1024;
                byte[] buffer = new byte[BUFFER_SIZE];
                while (!closed && dataListener != null) {
                    try {
                        int readLength = stream.read(buffer);
                        if (readLength <= 0) {
                            if (baos.size() > 0) {
                                dataListener.onReceive(baos.toByteArray());
                                baos.reset();
                            }
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                Log.e(TAG, null, ex);
                            }
                        } else if (readLength < BUFFER_SIZE) {
                            baos.write(buffer, 0, readLength);
                            dataListener.onReceive(baos.toByteArray());
                            baos.reset();

                        } else {
                            baos.write(buffer, 0, readLength);
                        }
                    } catch (IOException ex) {
                        if (!closed) {
                            Log.e(TAG, null, ex);
                        }
                    }
                }
                ht.interrupt();
                ht = null;
                handler = null;
            }
        });

    }

    public void write(byte[] array) throws IOException {
        write(array, 0, array.length);
    }

    public void write(byte[] array, int offset, int count) throws IOException {
        stream.write(array, offset, array.length);
    }

//    private int readSync(byte[] array) throws IOException {
//        //
//    }
    public int read(byte[] array) throws IOException {
        if (isAsyncRead()) {
            //return readSync(array);
            throw new IOException("in asynchronize mode, use setPrinterDataListener() instead of calling read().");
        } else {
            return stream.read(array);
        }
    }

    public void close() {
        closed = true;
    }

    public IConnectionStateListener getStateListener() {
        return listener;
    }

    public void setStateListener(IConnectionStateListener listener) {
        this.listener = listener;
    }

    /**
     * call this method in your activity's onActivityResult(...) if you are
     * using a BluetoothConnection to connect printer.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);
}
