package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定印表機的度量單位.
 *
 */
public enum PPLZUnitType {

    /**
     * 像素.
     *
     */
    Dot((byte) 'D'),
    /**
     * 英吋.
     *
     */
    Inch((byte) 'I'),
    /**
     * 公厘.
     *
     */
    Millimeter((byte) 'M');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZUnitType> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZUnitType> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZUnitType>();
        }
        return mappings;
    }

    private PPLZUnitType(int value) {
        intValue = value;
        PPLZUnitType.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZUnitType forValue(int value) {
        return getMappings().get(value);
    }
}
