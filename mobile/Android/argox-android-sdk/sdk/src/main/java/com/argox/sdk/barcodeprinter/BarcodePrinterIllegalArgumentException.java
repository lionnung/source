package com.argox.sdk.barcodeprinter;

/**
 * 發生 Barcode Printer 函式輸入無效參數時所擲回的例外狀況.
 *
 * {@link ApplicationException}
 */
public class BarcodePrinterIllegalArgumentException extends Exception {

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
     * 初始化 BarcodePrinterIllegalArgumentException 類別的新執行個體.
     *
     */
    public BarcodePrinterIllegalArgumentException() {
        this.errorMsg = "The argument is illegal.";
    }

    /**
     * 使用指定的錯誤訊息來初始化 BarcodePrinterIllegalArgumentException 類別的新執行個體.
     *
     * @param message [in]描述錯誤的訊息. 資料型態: Unicode.
     */
    public BarcodePrinterIllegalArgumentException(String message) {
        this.errorMsg = message;
    }

}
