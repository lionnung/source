package com.argox.sdk.barcodeprinter.emulation.pplz;

import com.argox.sdk.barcodeprinter.emulation.IEmulation;
import com.argox.sdk.barcodeprinter.BarcodePrinter;
import com.argox.sdk.barcodeprinter.util.Encoding;
import com.argox.sdk.barcodeprinter.util.InternalData;
import com.argox.sdk.barcodeprinter.util.LogFile;

/**
 * 此類別為 PPLZ 模擬器的主體, 包含了 PPLZ 模擬器各種不同的功能. 此類別需先實例化才能使用.
 *
 * {@link IEmulation}
 */
public class PPLZ implements IEmulation {

    private BarcodePrinter parent; //父物件的參考.
    private final PPLZ_BarcodeUtil barcodeUtil;
    private final PPLZ_FormatUtil formatUtil;
    private final PPLZ_GraphicsUtil graphicsUtil;
    private final PPLZ_IOUtil ioUtil;
    private final PPLZ_SetUtil setUtil;
    private final PPLZ_TextUtil textUtil;
    public java.util.LinkedList<byte[]> queueBuf; //buffer 的參考.

    /**
     * 此為建構子, 會自動建立此模擬器所需的物件.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效的 PPLZ 物件的參考.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 記憶體空間不足.
     *
     */
    public PPLZ() {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.PPLZ()" + InternalData.szEnter;
            LogFile.append(strLog);
        }
        //配記憶體.
        queueBuf = new java.util.LinkedList<byte[]>();

        //建立物件.
        barcodeUtil = new PPLZ_BarcodeUtil(this);
        formatUtil = new PPLZ_FormatUtil(this);
        graphicsUtil = new PPLZ_GraphicsUtil(this);
        ioUtil = new PPLZ_IOUtil(this);
        setUtil = new PPLZ_SetUtil(this);
        textUtil = new PPLZ_TextUtil(this);
    }

    /**
     * 此值表示標籤最大的長度和寬度的值; 單位: 像素.
     *
     * @return 為一常數值. 常數值: 32000.
     */
    public static int getMAX_LABEL_WH() {
        return 32000;
    }

    /**
     * 取得或設定指向父物件的參考, 也就是
     * {@link BarcodePrinter_API.Printer.BarcodePrinter">BarcodePrinter</see>
     * 物件.
     *
     * @return 此物件的父物件的參考. 資料型態:
     * {@link BarcodePrinter_API.Printer.BarcodePrinter">BarcodePrinter</see>.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效的 BarcodePrinter 物件的參考.
     *
     * {@link BarcodePrinter}
     */
    public final BarcodePrinter getParent() {
        return parent;
    }

    public final void setParent(BarcodePrinter printer) {
        this.parent = printer;
    }

    /**
     * 取得指向
     * {@link BarcodePrinter_API.Emulation.PPLZ_BarcodeUtil">PPLZ_BarcodeUtil</see>
     * 物件的參考.
     *
     * @return  PPLZ_BarcodeUtil 物件的參考. 資料型態:
     * {@link BarcodePrinter_API.Emulation.PPLZ_BarcodeUtil">PPLZ_BarcodeUtil</see>.
     * {@link PPLZ_BarcodeUtil}
     */
    public final PPLZ_BarcodeUtil getBarcodeUtil() {
        return barcodeUtil;
    }

    /**
     * 取得指向
     * {@link BarcodePrinter_API.Emulation.PPLZ_FormatUtil">PPLZ_FormatUtil</see>
     * 物件的參考.
     *
     * @return  PPLZ_FormatUtil 物件的參考. 資料型態:
     * {@link BarcodePrinter_API.Emulation.PPLZ_FormatUtil">PPLZ_FormatUtil</see>.
     * {@link PPLZ_FormatUtil}
     */
    public final PPLZ_FormatUtil getFormatUtil() {
        return formatUtil;
    }

    /**
     * 取得指向
     * {@link BarcodePrinter_API.Emulation.PPLZ_GraphicsUtil">PPLZ_GraphicsUtil</see>
     * 物件的參考.
     *
     * @return  PPLZ_GraphicsUtil 物件的參考. 資料型態:
     * {@link BarcodePrinter_API.Emulation.PPLZ_GraphicsUtil">PPLZ_GraphicsUtil</see>.
     * {@link PPLZ_GraphicsUtil}
     */
    public final PPLZ_GraphicsUtil getGraphicsUtil() {
        return graphicsUtil;
    }

    /**
     * 取得指向
     * {@link BarcodePrinter_API.Emulation.PPLZ_IOUtil">PPLZ_IOUtil</see>
     * 物件的參考.
     *
     * @return  PPLZ_IOUtil 物件的參考. 資料型態:
     * {@link BarcodePrinter_API.Emulation.PPLZ_IOUtil">PPLZ_IOUtil</see>.
     * {@link PPLZ_IOUtil}
     */
    public final PPLZ_IOUtil getIOUtil() {
        return ioUtil;
    }

    /**
     * 取得指向
     * {@link BarcodePrinter_API.Emulation.PPLZ_SetUtil">PPLZ_SetUtil</see>
     * 物件的參考.
     *
     * @return  PPLZ_SetUtil 物件的參考. 資料型態:
     * {@link BarcodePrinter_API.Emulation.PPLZ_SetUtil">PPLZ_SetUtil</see>.
     * {@link PPLZ_SetUtil}
     */
    public final PPLZ_SetUtil getSetUtil() {
        return setUtil;
    }

    /**
     * 取得指向
     * {@link BarcodePrinter_API.Emulation.PPLZ_TextUtil">PPLZ_TextUtil</see>
     * 物件的參考.
     *
     * @return  PPLZ_TextUtil 物件的參考. 資料型態:
     * {@link BarcodePrinter_API.Emulation.PPLZ_TextUtil">PPLZ_TextUtil</see>.
     * {@link PPLZ_TextUtil}
     */
    public final PPLZ_TextUtil getTextUtil() {
        return textUtil;
    }

    /**
     * 此函式的回傳格式 [Emulation Name].
     *
     * @return [out]回傳格式 [Emulation Name].
     */
    @Override
    public String getName() {
        return "PPLZ";
    }

    public void printOut() throws Exception {
        this.getIOUtil().printOut();
    }
}
