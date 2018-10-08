package com.argox.sdk.barcodeprinter.emulation.pplz;

/**
 * 此列舉用來設定 RSS (GS1 DataBar) 的符號種類.
 * <p>
 * RSS (GS1 DataBar) 的符號種類</p>
 * <list type="table">
 * <listheader>
 * <term>type</term>
 * <term>符號種類說明</term>
 * <term><p>
 * 符號種類標準最小高度</p><para>(單位: 像素)</para></term>
 * </listheader>
 * <item>
 * <term>1</term>
 * <term>RSS-14 (GS1 DataBar Omnidirectional).</term>
 * <term>33</term>
 * </item>
 * <item>
 * <term>2</term>
 * <term>RSS Truncated (GS1 DataBar Truncated).</term>
 * <term>13</term>
 * </item>
 * <item>
 * <term>3</term>
 * <term>RSS Stacked (GS1 DataBar Stacked).</term>
 * <term>13</term>
 * </item>
 * <item>
 * <term>4</term>
 * <term>RSS Stacked Omnidirectional (GS1 DataBar Stacked
 * Omnidirectional).</term>
 * <term>69</term>
 * </item>
 * <item>
 * <term>5</term>
 * <term>RSS Limited (GS1 DataBar Limited).</term>
 * <term>10</term>
 * </item>
 * <item>
 * <term>6</term>
 * <term>RSS Expanded (GS1 DataBar Expanded).</term>
 * <term>34</term>
 * </item>
 * </list>
 *
 */
public enum PPLZRSSType {

    /**
     * RSS-14 (GS1 DataBar Omnidirectional).
     *
     */
    RSS_14((byte) '1'),
    /**
     * RSS Truncated (GS1 DataBar Truncated).
     *
     */
    RSS_Truncated((byte) '2'),
    /**
     * RSS Stacked (GS1 DataBar Stacked).
     *
     */
    RSS_Stacked((byte) '3'),
    /**
     * RSS Stacked Omnidirectional (GS1 DataBar Stacked Omnidirectional).
     *
     */
    RSS_Stacked_Omnidirectional((byte) '4'),
    /**
     * RSS Limited (GS1 DataBar Limited).
     *
     */
    RSS_Limited((byte) '5'),
    /**
     * RSS Expanded (GS1 DataBar Expanded).
     *
     */
    RSS_Expanded((byte) '6');

    private int intValue;
    private static java.util.HashMap<Integer, PPLZRSSType> mappings;

    private synchronized static java.util.HashMap<Integer, PPLZRSSType> getMappings() {
        if (mappings == null) {
            mappings = new java.util.HashMap<Integer, PPLZRSSType>();
        }
        return mappings;
    }

    private PPLZRSSType(int value) {
        intValue = value;
        PPLZRSSType.getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static PPLZRSSType forValue(int value) {
        return getMappings().get(value);
    }
}
