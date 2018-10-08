package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定儲存物件的裝置.
 */
public enum PPLZStorage {

    /**
     * 記憶體.
     *
     */
    Dram((byte) 'R'),
    /**
     * 快閃記憶體.
     *
     */
    Flash((byte) 'B');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZStorage> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZStorage> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZStorage>();
        }
        return mappings;
    }

    private PPLZStorage(int value) {
        intValue = value;
        PPLZStorage.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZStorage forValue(int value) {
        return getMappings().get(value);
    }
}
