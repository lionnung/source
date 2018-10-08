package com.argox.sdk.barcodeprinter;

/**
 * 發生 Barcode Printer 連結物件中寫入資料逾時時所擲回的例外狀況.
 *
 * {@link ApplicationException}
 */
public class BarcodePrinterWriteTimeoutException extends Exception {

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
    public BarcodePrinterWriteTimeoutException() {
        this.errorMsg = "The message is write timeout exceptions.";
    }

    /**
     * 使用指定的錯誤訊息來初始化 BarcodePrinterGeneralException 類別的新執行個體.
     *
     * @param message [in]描述錯誤的訊息. 資料型態: Unicode.
     */
    public BarcodePrinterWriteTimeoutException(String message) {
        this.errorMsg = message;
    }
}
