package com.argox.sdk.barcodeprinter.connection;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Ken Ke
 */
public abstract class TwoWayStream implements ITwoWayStream {

    private static final String TAG = TwoWayStream.class.getName();
    protected InputStream in;
    protected OutputStream out;
    protected IConnectionStateListener listener;

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public int write(byte[] array, int offset, int count) throws IOException {
        byte[] bytes = new byte[count];
        System.arraycopy(array, offset, bytes, 0, count);
        out.write(bytes);
        return array.length;
    }

    public int read(byte[] array) throws IOException {
        int result = in.read(array);
        return result;
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {
            Log.e(TAG, null, ex);
        }
        try {
            if (out != null) {
                out.close();
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
