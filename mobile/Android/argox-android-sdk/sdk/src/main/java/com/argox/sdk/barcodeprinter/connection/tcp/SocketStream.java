package com.argox.sdk.barcodeprinter.connection.tcp;

import android.util.Log;
import com.argox.sdk.barcodeprinter.connection.ConnectionState;
import com.argox.sdk.barcodeprinter.connection.TwoWayStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Ken Ke
 */
public class SocketStream extends TwoWayStream {

    private static final String TAG = SocketStream.class.getName();
    protected String host;
    protected int port;
    protected Socket socket;

    public SocketStream(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean open() throws Exception {
        if (listener != null) {
            listener.onStateChanged(ConnectionState.Connecting);
        }
        try {
            socket = new Socket(host, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            if (listener != null) {
                listener.onStateChanged(ConnectionState.Connected);
            }
            return true;
        } catch (Exception ex) {
            Log.e("argox", null, ex);
            if (listener != null) {
                listener.onStateChanged(ConnectionState.Disconnected);
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
