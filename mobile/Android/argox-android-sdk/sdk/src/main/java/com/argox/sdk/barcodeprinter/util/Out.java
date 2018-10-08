package com.argox.sdk.barcodeprinter.util;

import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;

/**
 * 此類別用來做為資料處理的類別.
 *
 */
public final class Out {

    private static Encoding encode = Encoding.UTF_8;

    public static void setEncoding(Encoding encode) {
        Out.encode = encode;
    }

    public static Encoding getEncoding() {
        return encode;
    }

    public static int copyToBuffer(byte[] src, int index, int src_len, int dec_len, RefObject<byte[]> dec) throws BarcodePrinterGeneralException {
        if ((null == src) && (0 == src_len)) {
            return dec_len;
        }
        try {
            dec.value = Utils.resize(dec.value, dec_len + src_len);
            System.arraycopy(src, index, dec.value, dec_len, src_len);
            return dec_len + src_len;
        } catch (java.lang.Exception e) {
            throw new BarcodePrinterGeneralException();
        }
    }

    public static int copyToBuffer(String str, int dec_len, RefObject<byte[]> dec) throws BarcodePrinterGeneralException {
        try {
            if (true == Utils.isNullOrEmpty(str)) {
                return dec_len;
            }
            byte[] strBytes = str.getBytes(encode.getCharset());
            dec.value = Utils.resize(dec.value, dec_len + strBytes.length);
            System.arraycopy(strBytes, 0, dec.value, dec_len, strBytes.length);
            return dec_len + strBytes.length;
        } catch (java.lang.Exception e) {
            throw new BarcodePrinterGeneralException(e.getMessage());
        }
    }

    public static void copyToBuffer(byte[] src, int index, int src_len, java.util.LinkedList<byte[]> queue) throws BarcodePrinterGeneralException {
        if ((null == src) && (0 == src_len)) {
            return;
        }
        try {
            byte[] buf = new byte[src_len];
            System.arraycopy(src, index, buf, 0, src_len);
            queue.offer(buf);
        } catch (java.lang.Exception e) {
            throw new BarcodePrinterGeneralException();
        }
    }

    public static void copyToBuffer(String str, java.util.LinkedList<byte[]> queue) throws BarcodePrinterGeneralException {
        try {
            if (true == Utils.isNullOrEmpty(str)) {
                return;
            }
            byte[] buf = str.getBytes(encode.getCharset());
            queue.offer(buf);
        } catch (java.lang.Exception e) {
            throw new BarcodePrinterGeneralException();
        }
    }

    public static void appendtoBuffer(byte[] src, java.util.LinkedList<byte[]> queue) throws BarcodePrinterGeneralException {
        try {
            if (0 == src.length) {
                return;
            }
            queue.offer(src);
        } catch (java.lang.Exception e) {
            throw new BarcodePrinterGeneralException();
        }
    }

    public static int readFromQueue(byte[] dec, int index, int dec_len, java.util.LinkedList<byte[]> queue, RefObject<Integer> offset) throws BarcodePrinterGeneralException {
        if ((null == dec) && (0 == dec_len)) {
            return 0;
        }
        try {
            int len, total = 0;
            byte[] buf;
            do {
                if (0 == queue.size()) {
                    break;
                }
                buf = queue.peek();
                len = Math.min(buf.length - offset.value, dec_len);
                System.arraycopy(buf, offset.value, dec, index, len);
                total += len;
                if (buf.length == len + offset.value) {
                    queue.poll();
                    offset.value = 0;
                    dec_len -= len;
                    index += len;
                } else {
                    offset.value += len;
                    break;
                }
            } while (true);
            return total;
        } catch (java.lang.Exception e) {
            throw new BarcodePrinterGeneralException();
        }
    }
}
