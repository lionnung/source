package com.argox.sdk.barcodeprinter.emulation.pplz;

import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;
import com.argox.sdk.barcodeprinter.BarcodePrinterIllegalArgumentException;
import com.argox.sdk.barcodeprinter.connection.file.FileStream;
import com.argox.sdk.barcodeprinter.util.InternalData;
import com.argox.sdk.barcodeprinter.util.LogFile;
import com.argox.sdk.barcodeprinter.util.Out;
import com.argox.sdk.barcodeprinter.util.RefObject;
import com.argox.sdk.barcodeprinter.util.Utils;
import java.io.File;

/**
 * 此類別提供 PPLZ 模擬器的印表機輸出/輸入相關程序的方法. 此類別用於 PPLZ 類別中.
 *
 */
public class PPLZ_IOUtil {

    private PPLZ parent; //此為指向父物件的參考.

    /**
     * 此為建構子.
     *
     * @param emulation [in]輸入一個資料型態為 {@link PPLZ} 物件的參考.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效的 PPLZ 物件的參考.
     *
     */
    public PPLZ_IOUtil(PPLZ emulation) {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.PPLZ_IOUtil.PPLZ_IOUtil()" + InternalData.szEnter;
            strLog += "	emulation : ";
            if (null == emulation) {
                strLog += InternalData.szNull;
            } else {
                strLog += emulation.toString();
            }
            LogFile.append(strLog);
        }
        this.parent = emulation;
    }

    /**
     * 此函式用來將儲存在暫存區中的指令輸出至連結. 然後, 完成後, 會將暫存區的資料清除. 這些指令是來自於使用者所呼叫的函式轉換而來的.
     *
     *
     * <p>
     * 此函式只是將暫存區的資料輸出至連結. 因此, 若需要輸出列印參數, 請呼叫
     * {@link PPLZ_SetUtil.SetPrintOut}, 去設定相對應輸出的數值及模式.</p>
     * <p>
     * 當你需要輸出多個標籤格式內容時, 你可以使用相結合的模式: {@link PPLZ_SetUtil.SetPrintOut} +
     * ... + {@link PPLZ_SetUtil.SetPrintOut} +
     * {@link PPLZ_IOUtil.PrintOut}.</p>
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterConnectionException 發生 I/O
     * 錯誤.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 寫入資料至連結失敗.
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterWriteTimeoutException
     * 寫入資料逾時.
     *
     * {@link PPLZ_SetUtil.SetPrintOut}
     */
    public final void printOut() throws Exception {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.IOUtil.PrintOut()" + InternalData.szEnter;
            LogFile.append(strLog);
        }

        String strTemp;
        strTemp = "^XZ" + InternalData.szEnter; //end format.

        try {
            Out.copyToBuffer(strTemp, this.parent.queueBuf);
            //===== Out Data =====//
            byte[] buf = new byte[0];
            strTemp = "^XA" + InternalData.szEnter; //Start FormatUtil.
            RefObject<byte[]> tempRef_buf = new RefObject<byte[]>(buf);
            Out.copyToBuffer(strTemp, 0, tempRef_buf);
            buf = tempRef_buf.value;
            this.parent.getParent().getConnection().write(buf);
            for (byte[] temp : this.parent.queueBuf) {
                this.parent.getParent().getConnection().write(temp, 0, temp.length);
            }
        } catch (java.lang.Exception e) {
            throw e;
        } finally {
            // 重新開始.
            this.parent.queueBuf.clear();
        }
    }

    /**
     * 此函式用來將使用者自行編輯的檔案資料添加至暫存區的最後.
     *
     * @param filename [in]此為一檔案名稱, 可為絕對 (最好) 路徑或相對路徑兩者其一. 資料型態: Unicode.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤, 或讀取檔案資料失敗.
     *
     */
    public final void appendFile(String filename) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.IOUtil.AppendFile()" + InternalData.szEnter;
            strLog += "	filename: " + (filename == null || filename.isEmpty() ? InternalData.szNull : filename) + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (false == new File(filename).exists()) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        byte[] byteTemp = null;

        try {
            byteTemp = Utils.read(new File(filename));
        } catch (java.lang.Exception e) {
            throw new BarcodePrinterGeneralException();
        }

        String strTemp;
        strTemp = InternalData.szEnter;
        Out.copyToBuffer(byteTemp, 0, byteTemp.length, this.parent.queueBuf);
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來將使用者自行編輯的指令及資料位元組添加至暫存區的最後.
     *
     * @param array [in]要寫入至連結的資料. 資料型態: 位元組陣列.
     * @param offset [in]要從 'array' 中以零起始的位元組位移, 用來開始將位元組複製至目前的連結中.
     * @param count [in]要寫入目前連結的位元組數目.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void appendData(byte[] array, int offset, int count) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.IOUtil.AppendData()" + InternalData.szEnter;
            strLog += "	array: ";
            if (null == array) {
                strLog += InternalData.szNull;
                LogFile.append(strLog);
            } else {
                LogFile.append(strLog);
                LogFile.append(array, 0, array.length);
            }
            strLog = InternalData.szEnter;
            strLog += "	offset: " + offset + InternalData.szEnter;
            strLog += "	count: " + count + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((null == array) || offset < 0 || count < 0) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = InternalData.szEnter;
        Out.copyToBuffer(array, offset, count, this.parent.queueBuf);
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }
}
