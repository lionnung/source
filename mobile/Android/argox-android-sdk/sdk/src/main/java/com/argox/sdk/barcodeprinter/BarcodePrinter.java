package com.argox.sdk.barcodeprinter;

import com.argox.sdk.barcodeprinter.emulation.IEmulation;
import com.argox.sdk.barcodeprinter.connection.IPrinterConnection;
import com.argox.sdk.barcodeprinter.connection.PrinterConnection;
import com.argox.sdk.barcodeprinter.util.Encoding;
import com.argox.sdk.barcodeprinter.util.InternalData;
import com.argox.sdk.barcodeprinter.util.LogFile;
import com.argox.sdk.barcodeprinter.util.Out;
import com.argox.sdk.barcodeprinter.util.Utils;
import java.io.File;
import java.io.IOException;

/**
 * 此類別可視為一個主體, 當加入印表機輸出/輸入所需的物件後, 就可以控制印表機去完成想要的結果. 此類別需先實例化才能使用.
 *
 * {@link IPrinterConnection}
 * {@link IEmulation}
 * @param <T> 印表機連接類別
 * @param <S> 繪圖指令模擬器類別，目前僅提供PPLZ模擬器
 */
public class BarcodePrinter<T extends PrinterConnection, S extends IEmulation> {

    /**
     * 連結物件的參考.
     */
    protected T connection; //

    /**
     * 模擬器物件的參考.
     */
    protected S emulation; //

    /**
     * 此為建構子.
     *
     */
    public BarcodePrinter() {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "BarcodePrinter.BarcodePrinter()" + InternalData.szEnter;
            LogFile.append(strLog);
        }
        this.connection = null;
        this.emulation = null;
    }

    /**
     * 此函式用來加入一個連結的參考. 資料型態: {@link IPrinterConnection}.
     * 之後印表機的輸出/輸入控制都是透過此連結.
     *
     * @param connection [in]輸入一個連結物件的參考. 資料型態: {@link IPrinterConnection}.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterConnectionException 無效的
     * IPrinterConnection 物件的參考.
     *
     * {@link IPrinterConnection}
     */
    public final void setConnection(T connection) {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "BarcodePrinter.AddConnection()" + InternalData.szEnter;
            strLog += "	connection: " + ((null == connection) ? InternalData.szNull : connection.toString()) + InternalData.szEnter;
            LogFile.append(strLog);
        }
        this.connection = connection;
    }

    /**
     * 取得連結物件的參考, 印表機若想要做輸出/輸入控制都可透過此連結.
     *
     * @return 連結物件的參考. 資料型態: {@link IPrinterConnection}.
     */
    public final T getConnection() {
        return this.connection;
    }

    /**
     * 此函式用來加入一個模擬器的參考. 資料型態: {@link IEmulation}. 之後印表機所要完成的功能都是透過此連結去達成.
     *
     * @param emulation [in]輸入一個資料型態為 {@link IEmulation} 物件的參考.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效的 IEmulation 物件的參考.
     */
    public final void setEmulation(S emulation) {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "BarcodePrinter.AddEmulation()" + InternalData.szEnter;
            strLog += "	emulation: " + ((null == emulation) ? InternalData.szNull : emulation.toString()) + InternalData.szEnter;
            LogFile.append(strLog);
        }
        emulation.setParent(this);
        this.emulation = emulation;
    }

    /**
     * 取得目前模擬器的參考
     * @return
     */
    public S getEmulation() {
        return emulation;
    }

    public Encoding getEncoding() {
        return Out.getEncoding();
    }

    public void setEncoding(Encoding encoding) {
        Out.setEncoding(encoding);
    }

    /**
     * 此函式用來將使用者自行編輯的檔案資料寫入這個連結中.
     *
     * @param filePath [in]此為一檔案名稱, 可為絕對 (建議) 路徑或相對路徑兩者其一. 資料型態: Unicode.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterConnectionException 發生 I/O
     * 錯誤.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 寫入資料至連結失敗, 或讀取檔案資料失敗.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterWriteTimeoutException
     * 寫入資料逾時.
     *
     */
    public final void sendFile(String filePath) throws BarcodePrinterIllegalArgumentException, IOException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "BarcodePrinter.SendFile()" + InternalData.szEnter;
            strLog += "	filename: " + (filePath == null || filePath.isEmpty() ? InternalData.szNull : filePath) + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (!new File(filePath).exists()) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        byte[] byteTemp = Utils.read(new File(filePath));
        this.connection.write(byteTemp);
    }

    /**
     * 將列印指令暫存區內的資料輸出至印表機，開始列印
     * @throws Exception
     */
    public void print() throws Exception {
        emulation.printOut();
    }
}
