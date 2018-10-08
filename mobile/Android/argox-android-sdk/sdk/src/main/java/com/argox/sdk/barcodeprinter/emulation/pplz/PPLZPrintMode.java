package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定列印後對標籤紙的動作模式.
 *
 */
public enum PPLZPrintMode {

    /**
     * 撕開.
     *
     */
    Tear_Off((byte) 'T'),
    /**
     * 剝紙.
     *
     */
    Peel_Off((byte) 'P'),
    /**
     * 裁切.
     *
     */
    Cutter((byte) 'C');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZPrintMode> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZPrintMode> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZPrintMode>();
        }
        return mappings;
    }

    private PPLZPrintMode(int value) {
        intValue = value;
        PPLZPrintMode.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZPrintMode forValue(int value) {
        return getMappings().get(value);
    }
}
