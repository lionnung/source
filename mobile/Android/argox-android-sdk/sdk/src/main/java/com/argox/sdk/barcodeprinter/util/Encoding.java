package com.argox.sdk.barcodeprinter.util;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Ken Ke
 */
public enum Encoding {

    US_ASCII("US-ASCII"), ISO_8859_1("ISO-8859-1"), UTF_8("UTF-8"), UTF_16BE("UTF-16BE"), UTF_16LE("UTF-16LE"), UTF_16("UTF-16");
    String charset;

    Encoding(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return this.charset;
    }

    public byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes(charset);
    }
}
