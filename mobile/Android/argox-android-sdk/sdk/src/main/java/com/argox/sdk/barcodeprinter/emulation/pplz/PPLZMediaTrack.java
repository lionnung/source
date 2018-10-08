package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定印表機的紙張偵測模式.
 *
 */
public enum PPLZMediaTrack {

    /**
     * 連續紙.
     *
     */
    Continuous_Media((byte) 'N'),
    /**
     * 不連續紙, Web 感應.
     *
     */
    Non_Continuous_Mdeia_Web_Sensing((byte) 'Y');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZMediaTrack> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZMediaTrack> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZMediaTrack>();
        }
        return mappings;
    }

    private PPLZMediaTrack(int value) {
        intValue = value;
        PPLZMediaTrack.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZMediaTrack forValue(int value) {
        return getMappings().get(value);
    }
}
