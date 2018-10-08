package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定物件旋轉的角度, 方向.
 *
 */
public enum PPLZOrient {

    /**
     * 右旋 0 度.
     *
     */
    Clockwise_0_Degrees((byte) 'N'),
    /**
     * 右旋 90 度.
     *
     */
    Clockwise_90_Degrees((byte) 'R'),
    /**
     * 右旋 180 度.
     *
     */
    Clockwise_180_Degrees((byte) 'I'),
    /**
     * 右旋 270 度.
     *
     */
    Clockwise_270_Degrees((byte) 'B');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZOrient> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZOrient> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZOrient>();
        }
        return mappings;
    }

    private PPLZOrient(int value) {
        intValue = value;
        PPLZOrient.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZOrient forValue(int value) {
        return getMappings().get(value);
    }
}
