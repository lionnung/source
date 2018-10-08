package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定紙張的種類.
 *
 */
public enum PPLZMediaType {

    /**
     * 感熱轉印.
     *
     */
    Thermal_Transfer_Media((byte) 'T'),
    /**
     * 直接感熱.
     *
     */
    Direct_Thermal_Media((byte) 'D');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZMediaType> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZMediaType> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZMediaType>();
        }
        return mappings;
    }

    private PPLZMediaType(int value) {
        intValue = value;
        PPLZMediaType.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZMediaType forValue(int value) {
        return getMappings().get(value);
    }
}
