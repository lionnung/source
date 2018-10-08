package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定 MaxiCode 的模式.
 *
 */
public enum PPLZMaxiCodeMode {

    /**
     * 結構化乘載訊息: 數字郵政代碼 (U.S.).
     *
     */
    Mode_2((byte) '2'),
    /**
     * 結構化乘載訊息: 字母郵政代碼 (非 U.S.).
     *
     */
    Mode_3((byte) '3');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZMaxiCodeMode> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZMaxiCodeMode> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZMaxiCodeMode>();
        }
        return mappings;
    }

    private PPLZMaxiCodeMode(int value) {
        intValue = value;
        PPLZMaxiCodeMode.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZMaxiCodeMode forValue(int value) {
        return getMappings().get(value);
    }
}
