package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定印表機內建字型.
 *
 *
 * 點陣字型的大小.
 * <list type="table">
 * <listheader><term>字型</term><term>寬度 * 高度</term></listheader>
 * <item><term>A</term><term>5 * 9</term></item>
 * <item><term>B</term><term>7 * 11</term></item>
 * <item><term>C</term><term>10 * 18</term></item>
 * <item><term>D</term><term>10 * 18</term></item>
 * <item><term>E</term><term>15 * 28</term></item>
 * <item><term>F</term><term>13 * 26</term></item>
 * <item><term>G</term><term>40 * 60</term></item>
 * <item><term>H</term><term>13 * 21</term></item>
 * </list>
 *
 */
public enum PPLZFont {

    /**
     * 點陣字型 A, (寬度,高度) = (5,9).
     *
     */
    Font_A((byte) 'A'),
    /**
     * 點陣字型 B, (寬度,高度) = (7,11).
     *
     */
    Font_B((byte) 'B'),
    /**
     * 點陣字型 C, (寬度,高度) = (10,18).
     *
     */
    Font_C((byte) 'C'),
    /**
     * 點陣字型 D, (寬度,高度) = (10,18).
     *
     */
    Font_D((byte) 'D'),
    /**
     * 點陣字型 E, (寬度,高度) = (15,28).
     *
     */
    Font_E((byte) 'E'),
    /**
     * 點陣字型 F, (寬度,高度) = (13,26).
     *
     */
    Font_F((byte) 'F'),
    /**
     * 點陣字型 G, (寬度,高度) = (40,60).
     *
     */
    Font_G((byte) 'G'),
    /**
     * 點陣字型 H, (寬度,高度) = (13,21).
     *
     */
    Font_H((byte) 'H'),
    /**
     * 線性字形 Zero.
     *
     */
    Font_Zero((byte) '0'),
    /**
     * 線性字形 P.
     *
     */
    Font_P((byte) 'P'),
    /**
     * 線性字形 Q.
     *
     */
    Font_Q((byte) 'Q'),
    /**
     * 線性字形 R.
     *
     */
    Font_R((byte) 'R'),
    /**
     * 線性字形 S.
     *
     */
    Font_S((byte) 'S'),
    /**
     * 線性字形 T.
     *
     */
    Font_T((byte) 'T'),
    /**
     * 線性字形 U.
     *
     */
    Font_U((byte) 'U'),
    /**
     * 線性字形 V.
     *
     */
    Font_V((byte) 'V');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZFont> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZFont> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZFont>();
        }
        return mappings;
    }

    private PPLZFont(int value) {
        intValue = value;
        PPLZFont.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZFont forValue(int value) {
        return getMappings().get(value);
    }
}
