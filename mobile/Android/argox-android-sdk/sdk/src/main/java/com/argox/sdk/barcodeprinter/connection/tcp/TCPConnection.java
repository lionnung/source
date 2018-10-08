package com.argox.sdk.barcodeprinter.connection.tcp;

import android.content.Intent;
import com.argox.sdk.barcodeprinter.connection.PrinterConnection;
import java.io.IOException;

/**
 *
 * @author Ken Ke
 */
public class TCPConnection extends PrinterConnection<SocketStream> {

    private static final String TAG = TCPConnection.class.getName();
    protected String host;
    protected int port;

    public TCPConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void open() throws Exception {
        if (!closed) {
            stream = new SocketStream(host, port);
            stream.setStateListener(listener);
            openStream(stream);
        }
    }

    @Override
    public void close() {
        closed = true;
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    @Override
    public String toString() {
        if (host == null) {
            return "tcp:";
        } else {
            return "tcp:"+host + ":" + port;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
