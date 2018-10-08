package com.argox.sdk.barcodeprinter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Ken Ke
 */
public final class Utils {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static byte[] resize(byte[] bytes, int newLength) {
        byte[] newBytes = new byte[newLength];
        System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
        return newBytes;
    }

    public static byte[] read(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }
        return buffer;
    }
}
