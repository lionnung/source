package com.argox.sdk.barcodeprinter.emulation;

import com.argox.sdk.barcodeprinter.BarcodePrinter;

/**
 * 此介面為用來做為模擬器的基本介面.
 *
 */
public interface IEmulation {

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
    public BarcodePrinter getParent();

    public void setParent(BarcodePrinter printer);

    public void printOut() throws Exception;

    /**
     * 此函式回傳的描述字串依賴該類別的方法.
     *
     * @return [out]回傳連結所描述的字串.
     */
    public String getName();
}
