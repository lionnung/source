package com.argox.sdk.barcodeprinter.connection;

import java.io.IOException;

/**
 *
 * @author Ken Ke
 */
public interface ITwoWayStream {

    public boolean open() throws Exception;

    public int write(byte[] array, int offset, int count) throws IOException;

    public int read(byte[] array) throws IOException;

    //public void EndWrite(IAsyncResult asyncResult);
    public void flush() throws IOException;

    public void close();

    public IConnectionStateListener getStateListener();

    public void setStateListener(IConnectionStateListener listener);
}
