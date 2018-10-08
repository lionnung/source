package com.argox.sdk.barcodeprinter.connection.file;

import android.util.Log;
import com.argox.sdk.barcodeprinter.connection.ConnectionState;
import com.argox.sdk.barcodeprinter.connection.IConnectionStateListener;
import com.argox.sdk.barcodeprinter.connection.ITwoWayStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Ken Ke
 */
public class FileStream implements ITwoWayStream {

    private static final String TAG = FileStream.class.getName();
    protected File file;
    protected FileOutputStream fos;
    protected IConnectionStateListener listener;

    public FileStream(String path) {
        this.file = new File(path);
    }

    public FileStream(File file) {
        this.file = file;
    }

    public boolean open() throws Exception {
        if (listener != null) {
            listener.onStateChanged(ConnectionState.Connecting);
        }
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            if (listener != null) {
                listener.onStateChanged(ConnectionState.Connected);
            }
            return true;
        } catch (Exception ex) {
            // Unable to connect; close the socket and get out
            Log.e(TAG, null, ex);
            if (listener != null) {
                listener.onStateChanged(ConnectionState.Disconnected);
            }
            throw ex;
        }
    }

    public int write(byte[] array, int offset, int count) throws IOException {
        fos.write(array, offset, count);
        return 0;
    }

    public int read(byte[] array) throws IOException {
        return 0;
    }

    public void flush() throws IOException {
        fos.flush();
    }

    public void close() {
        try {
            fos.close();
            if (listener != null) {
                listener.onStateChanged(ConnectionState.Disconnected);
            }

        } catch (IOException ex) {
            Log.e(TAG, null, ex);
        }
    }

    public IConnectionStateListener getStateListener() {
        return listener;
    }

    public void setStateListener(IConnectionStateListener listener) {
        this.listener = listener;
    }
}
