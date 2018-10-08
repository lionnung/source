package com.argox.sdk.barcodeprinter.connection;

import java.io.IOException;

/**
 * 此介面為用來做為連接埠的基本介面.
 *
 */
public interface IPrinterConnection {

    /**
     * 取得或設定此連接埠寫入資料時最大的逾時時間.
     *
     * @return 寫入資料發生逾時的時間. 單位: 毫秒 (ms).
     */
    public int getWriteTimeout();

    public void setWriteTimeout(int value);

    /**
     * 取得或設定此連接埠讀取資料時最大的逾時時間.
     *
     * @return 讀取資料發生逾時的時間. 單位: 毫秒 (ms).
     */
    public int getReadTimeout();

    public void setReadTimeout(int value);

    /**
     * 取得此連接埠是否支援寫入功能.
     *
     * @return 'true' 為可寫入; 'false' 為不可寫入.
     */
    public boolean canWrite();

    /**
     * 取得此連接埠是否支援讀取功能.
     *
     * @return 'true' 為可讀取; 'false' 為不可讀取.
     */
    public boolean canRead();

    /**
     * 取得此連接埠是否在連接狀態中.
     *
     * @return 'true' 為連接; 'false' 為關閉.
     */
    public boolean isConnected();

    /**
     * 取得此連接埠是同步或非同步的模式.
     *
     * @return 'true' 為非同步模式; 'false' 為同步模式.
     */
    public boolean isAsyncRead();

    /**
     * 此函式用來開啟連結.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterConnectionException 發生 I/O
     * 錯誤. 發生 System.IO.IOException 的例外狀況.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外. 發生 System.ArgumentOutOfRangeException 的例外狀況.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 無法開啟連結. 發生非 System.IO.IOException 的例外狀況.
     *
     */
    public void open() throws Exception;

    /**
     * 此函式用來關閉目前的連結, 並將目前連結相關的任何資源釋放.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterConnectionException 發生 I/O
     * 錯誤. 發生 System.IO.IOException 的例外狀況.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 無法關閉連結. 發生非 System.IO.IOException 的例外狀況.
     *
     */
    public void close();

    /**
     * 此函式用來將緩衝區中所指定的位元組區塊寫入這個連結中.
     *
     * @param array [in]要寫入至連結的資料. 資料型態: 位元組陣列.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterConnectionException 發生 I/O
     * 錯誤, 或是連結未建立. 發生 System.IO.IOException 的例外狀況.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 寫入資料至連結失敗. 發生非 System.IO.IOException 的例外狀況.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterWriteTimeoutException
     * 寫入資料逾時.
     *
     */
    public void write(byte[] array) throws IOException;

    /**
     * 此函式用來將緩衝區中所指定的位元組區塊寫入這個連結中.
     *
     * @param array [in]要寫入至連結的資料. 資料型態: 位元組陣列.
     * @param offset [in]要從 'array' 中以零起始的位元組位移, 用來開始將位元組複製至目前的連結中.
     * @param count [in]要寫入目前連結的位元組數目.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterConnectionException 發生 I/O
     * 錯誤, 或是連結未建立. 發生 System.IO.IOException 的例外狀況.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外. 發生 System.ArgumentOutOfRangeException 的例外狀況.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 寫入資料至連結失敗. 發生非 System.IO.IOException 的例外狀況.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterWriteTimeoutException
     * 寫入資料逾時.
     *
     */
    public void write(byte[] array, int offset, int count) throws IOException;

    /**
     * 此函式用來由連結中讀取位元組區塊且傳輸至指定的緩衝區中. 這個呼叫是不會鎖住的.
     *
     * @param array [out]當這個方法傳回時, 其緩衝區值介於 'offset' 和 ('offset' + 'count' - 1)
     * 之間的指定位元組陣列, 這些值會由從目前連結中讀取的位元組取代.
     * @param offset [in]要在緩衝區中放置讀取位元組之 'array' 的位元組位移.
     * @param count [in]要讀取的最大位元組數目.
     * @return [out]回傳緩衝區所讀取的總位元組數目. 如果目前無法提供要求的位元組數目, 則這個數目可能小於所要求的位元組數;
     * 如果已經到達資料流末端, 則為零.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterConnectionException 發生 I/O
     * 錯誤, 或是連結未建立. 發生 System.IO.IOException 的例外狀況.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外. 發生 System.ArgumentOutOfRangeException 的例外狀況.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 由連結中讀取資料失敗. 發生非 System.IO.IOException 的例外狀況.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterReadTimeoutException
     * 讀取資料逾時.
     *
     */
    public int read(byte[] array) throws IOException;
}
