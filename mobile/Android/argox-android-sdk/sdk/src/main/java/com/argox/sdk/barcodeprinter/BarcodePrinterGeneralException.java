package com.argox.sdk.barcodeprinter;

/**
 * 發生非 Barcode Printer 函式造成的錯誤時所擲回的例外狀況.
 *
 * {@link ApplicationException}
 */
public class BarcodePrinterGeneralException extends Exception {

    private String errorMsg; //錯誤訊息.

    /**
     * 取得描述目前例外狀況的訊息.
     *
     * @return 取得描述目前例外狀況的訊息, 或空字串 (""). 資料型態: Unicode.
     */
    @Override
    public String getMessage() {
        return this.errorMsg;
    }

    /**
     * 初始化 BarcodePrinterGeneralException 類別的新執行個體.
     *
     */
    public BarcodePrinterGeneralException() {
        this.errorMsg = "The message is non-Barcode Printer exceptions.";
    }

    /**
     * 使用指定的錯誤訊息來初始化 BarcodePrinterGeneralException 類別的新執行個體.
     *
     * @param message [in]描述錯誤的訊息. 資料型態: Unicode.
     */
    public BarcodePrinterGeneralException(String message) {
        this.errorMsg = message;
    }
}
