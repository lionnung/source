package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定 QR Code 的模組.
 *
 */
public enum PPLZQRCodeModel {

    /**
     * 原始的.
     *
     */
    Model_1((byte) '1'),
    /**
     * 增強推荐.
     *
     */
    Model_2((byte) '2');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZQRCodeModel> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZQRCodeModel> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZQRCodeModel>();
        }
        return mappings;
    }

    private PPLZQRCodeModel(int value) {
        intValue = value;
        PPLZQRCodeModel.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZQRCodeModel forValue(int value) {
        return getMappings().get(value);
    }
}
