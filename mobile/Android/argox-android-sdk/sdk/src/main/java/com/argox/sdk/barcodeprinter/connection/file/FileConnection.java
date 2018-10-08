package com.argox.sdk.barcodeprinter.connection.file;

import android.content.Intent;
import com.argox.sdk.barcodeprinter.connection.PrinterConnection;
import java.io.File;

/**
 *
 * @author Ken Ke
 */
public class FileConnection extends PrinterConnection<FileStream> {

    protected File file;

    public FileConnection(String filePath) {
        this.file = new File(filePath);
    }

    public FileConnection(File file) {
        this.file = file;
    }

    public void open() throws Exception {
        if (stream == null) {
            stream = new FileStream(file);
            stream.setStateListener(listener);
            stream.open();
            connected = true;
        }
    }

    @Override
    public void close() {
        super.close();
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    @Override
    public String toString() {
        if (file == null) {
            return "file:";
        } else {
            return "file:" + file.getAbsolutePath();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
