package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定一維條碼和二維條碼的種類。在PPLZ中需使用此列舉。
 * 當使用BarCodeType時，可利用名稱字串來轉換成同名的PPLZBarCodeType
 *
 */
public enum PPLZBarCodeType {

    /**
     * Code 11.
     *
     */
    Code_11((byte) '1'),
    /**
     * Interleaved 2 of 5.
     *
     */
    Interleaved_2_of_5((byte) '2'),
    /**
     * Code 39.
     *
     */
    Code_39((byte) '3'),
    /**
     * Planet Code.
     *
     */
    Planet_Code((byte) '5'),
    /**
     * EAN-8.
     *
     */
    EAN_8((byte) '8'),
    /**
     * UPC-E.
     *
     */
    UPC_E((byte) '9'),
    /**
     * Code 93.
     *
     */
    Code_93((byte) 'A'),
    /**
     * Code 128 Subset A,B,C, Code 128 UCC.
     *
     */
    Code_128((byte) 'C'),
    /**
     * EAN-13.
     *
     */
    EAN_13((byte) 'E'),
    /**
     * Industrial 2 of 5.
     *
     */
    Industrial_2_of_5((byte) 'I'),
    /**
     * Standard 2 of 5.
     *
     */
    Standard_2_of_5((byte) 'J'),
    /**
     * ANSI Codabar.
     *
     */
    ANSI_Codabar((byte) 'K'),
    /**
     * LOGMARS.
     *
     */
    LOGMARS((byte) 'L'),
    /**
     * MSI.
     *
     */
    MSI((byte) 'M'),
    /**
     * Plessey.
     *
     */
    Plessey((byte) 'P'),
    /**
     * UPC/EAN Extensions.
     *
     */
    UPC_EAN_Extensions((byte) 'S'),
    /**
     * UPC-A.
     *
     */
    UPC_A((byte) 'U'),
    /**
     * POSTAL.
     *
     */
    POSTAL((byte) 'Z');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZBarCodeType> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZBarCodeType> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZBarCodeType>();
        }
        return mappings;
    }

    private PPLZBarCodeType(int value) {
        intValue = value;
        PPLZBarCodeType.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZBarCodeType forValue(int value) {
        return getMappings().get(value);
    }
}
