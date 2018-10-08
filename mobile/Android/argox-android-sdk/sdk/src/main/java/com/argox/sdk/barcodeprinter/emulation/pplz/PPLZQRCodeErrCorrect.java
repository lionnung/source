package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定 QR Code 的錯誤校正等級.
 *
 */
public enum PPLZQRCodeErrCorrect {

    /**
     * 高密度, 復原 7%.
     *
     */
    High_Density((byte) 'L'),
    /**
     * 標準, 復原 15%.
     *
     */
    Standard((byte) 'M'),
    /**
     * 高可靠度, 復原 25%.
     *
     */
    High_Reliability((byte) 'Q'),
    /**
     * 超高可靠度, 復原 30%.
     *
     */
    Ultra_High_Reliability((byte) 'H');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZQRCodeErrCorrect> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZQRCodeErrCorrect> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZQRCodeErrCorrect>();
        }
        return mappings;
    }

    private PPLZQRCodeErrCorrect(int value) {
        intValue = value;
        PPLZQRCodeErrCorrect.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZQRCodeErrCorrect forValue(int value) {
        return getMappings().get(value);
    }
}
