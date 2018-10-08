package com.argox.sdk.barcodeprinter.emulation.pplz;

import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;
import com.argox.sdk.barcodeprinter.BarcodePrinterIllegalArgumentException;
import com.argox.sdk.barcodeprinter.util.InternalData;
import com.argox.sdk.barcodeprinter.util.LogFile;
import com.argox.sdk.barcodeprinter.util.Out;

/**
 * 此類別提供 PPLZ 模擬器的印表機參數設定相關程序的方法. 此類別用於 PPLZ 類別中.
 *
 */
public class PPLZ_SetUtil {

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
    public PPLZ_SetUtil(PPLZ emulation) {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.PPLZ_SetUtil.PPLZ_SetUtil()" + InternalData.szEnter;
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
     * 此函式用來做為開機重置, 且將清除所有儲存在暫除區和記憶體的資料.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setReset() throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetReset()" + InternalData.szEnter;
            LogFile.append(strLog);
        }

        String strTemp = "~JR\r\n"; // Power on reset.
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定印表機的度量單位為英吋, 公厘或像素.
     *
     * @param unit [in]要設定的度量單位. 使用 {@link PPLZUnitType} 列舉來設定.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZUnitType}
     */
    public final void setUnit(PPLZUnitType unit) throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetUnit()" + InternalData.szEnter;
            strLog += "	unit: " + unit + InternalData.szEnter;
            LogFile.append(strLog);
        }

        String strTemp;
        strTemp = "^MU" + (char) unit.getValue() + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定紙張長度, 此參數在連續紙時是必須的.
     *
     * @param length [in]要設定的紙張長度. 範圍: 1~32000; 單位: 像素.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setLabelLength(int length) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetLabelLength()" + InternalData.szEnter;
            strLog += "	length: " + length + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (length < 1 || length > PPLZ.getMAX_LABEL_WH()) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^LL" + length + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定列印範圍, 也等同紙張寬度.
     *
     * @param width [in]要設定的紙張寬度. 範圍: 2~印表機的寬度; 單位: 像素.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setPrintWidth(int width) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetPrintWidth()" + InternalData.szEnter;
            strLog += "	width: " + width + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (width < 2) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^PW" + width + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定紙張偵測的模式.
     *
     * @param type [in]紙張的偵測模式. 使用 {@link PPLZMediaTrack} 列舉來設定.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZMediaTrack}
     */
    public final void setMediaTrack(PPLZMediaTrack type) throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetMediaTrack()" + InternalData.szEnter;
            strLog += "	type: " + type + InternalData.szEnter;
            LogFile.append(strLog);
        }
        String strTemp;
        strTemp = "^MN" + (char) type.getValue() + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定標籤原點位置. 預設的原點位置在左上角 (0,0).
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setHomePosition(int x, int y) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetHomePosition()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH())) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^LH" + x + "," + y + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來致能或不致能鏡射模式.
     *
     *
     * <p>
     * 此函式無論在程式中任意位置被呼叫, 皆會依設定執行鏡射列印或一般列印於所有要列印的文字, 條碼或圖案.</p>
     * <p>
     * 在程式中若重複呼叫此函數, 則以最後一次呼叫此函數的設定為主.</p>
     *
     * @param isMirror [in]是否列印於鏡射模式下. 'true' 為鏡射模式; 'false' 為一般模式; 預設值為
     * 'false'.
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setMirror(boolean isMirror) throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetMirror()" + InternalData.szEnter;
            strLog += "	isMirror: " + isMirror + InternalData.szEnter;
            LogFile.append(strLog);
        }

        String strTemp;
        char cMirror;
        cMirror = (true == isMirror) ? 'Y' : 'N';
        strTemp = "^PM" + cMirror + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定印表機列印後對標籤紙的動作模式.
     *
     * @param mode [in]列印後對標籤紙的動作模式. 使用 {@link PPLZPrintMode} 列舉來設定.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZPrintMode}
     */
    public final void setPrintMode(PPLZPrintMode mode) throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetPrintMode()" + InternalData.szEnter;
            strLog += "	mode: " + mode + InternalData.szEnter;
            LogFile.append(strLog);
        }

        String strTemp;
        strTemp = "^MM" + (char) mode.getValue() + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定列印時的紙張的種類.
     *
     * @param type [in]紙張的種類. 使用 {@link PPLZMediaType} 列舉來設定.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZMediaType}
     */
    public final void setMediaType(PPLZMediaType type) throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetMediaType()" + InternalData.szEnter;
            strLog += "	type: " + type + InternalData.szEnter;
            LogFile.append(strLog);
        }

        String strTemp;
        strTemp = "^MT" + (char) type.getValue() + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定列印前, 標籤回收的距離.
     *
     * 此函式將會吐出標籤紙. 在列印後, 以設定的長度並停在送紙後的位置上.
     *
     * @param length [in]吐出的長度. 範圍: -255~255; 單位: 像素.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setBackfeed(int length) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetBackfeed()" + InternalData.szEnter;
            strLog += "	length: " + length + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (length < -255 || length > 255) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "~TA" + length + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定列印頭列印熱度.
     *
     * @param darkness [in]設定熱感頭列印熱度. 範圍: 0~30. 列印品質絕非單一因素決定的. 想得到更好的列印品質,
     * 應該考慮一些別的因素, 例如: printing speed, media types, ribbon types (wax,
     * semi-resin 和 resin), image pattern, ... 等.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setDarkness(int darkness) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetDarkness()" + InternalData.szEnter;
            strLog += "	darkness: " + darkness + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (darkness < 0 || darkness > 30) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^MD0~SD" + darkness + InternalData.szEnter; //^MD設成0, 只利用~SD來做設定.

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定列印, 送紙及回轉的速度.
     *
     * @param printSpeed [in]列印速度. 範圍: 1~12; 單位: IPS; 預設值: 2.
     * @param feedSpeed [in]送紙速度. 範圍: 1~12; 單位: IPS; 預設值: 2.
     * @param backfeedSpeed [in]回轉速度. 範圍: 1~12; 單位: IPS; 預設值: 2.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setPrintRate(int printSpeed, int feedSpeed, int backfeedSpeed) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetPrintRate()" + InternalData.szEnter;
            strLog += "	printSpeed: " + printSpeed + InternalData.szEnter;
            strLog += "	feedSpeed: " + feedSpeed + InternalData.szEnter;
            strLog += "	backfeedSpeed: " + backfeedSpeed + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((printSpeed < 1 || printSpeed > 12) || (feedSpeed < 1 || feedSpeed > 12) || (backfeedSpeed < 1 || backfeedSpeed > 12)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^PR" + printSpeed + "," + feedSpeed + "," + backfeedSpeed + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來設定列印的速度.
     *
     * @param printSpeed [in]列印速度. 範圍: 1~12; 單位: IPS; 預設值: 2.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void setPrintRate(int printSpeed) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetPrintRate()" + InternalData.szEnter;
            strLog += "	printSpeed: " + printSpeed + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (printSpeed < 1 || printSpeed > 12) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^PR" + printSpeed + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來清除儲存裝置中的物件, 包含 format(.ZPL), garphic(.GRF), font(.FNT).
     *
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZStorage}
     */
    public final void clearStore(PPLZStorage storageType) throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.ClearStore()" + InternalData.szEnter;
            strLog += "	storageType: " + storageType + InternalData.szEnter;
            LogFile.append(strLog);
        }

        String strTemp;
        strTemp = "^ID" + (char) storageType.getValue() + ":*.*" + InternalData.szEnter;
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來讓印表機做標籤長度的偵測及校正動作.
     *
     */
    public final void setMediaCalibration() throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetMediaCalibration()" + InternalData.szEnter;
            LogFile.append(strLog);
        }

        String strTemp;
        strTemp = "~JC\r\n";
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來輸出列印指令及列印張數至暫存區, 這樣等於結束一個標籤格式內容. 所以, 在此函式呼叫完成後, 將是一個新的標籤格式內容的開始.
     *
     * 當你需要輸出多個標籤格式內容時, 你可以使用相結合的模式: {@link PPLZ_SetUtil.SetPrintOut} +
     * ... + {@link PPLZ_SetUtil.SetPrintOut} +
     * {@link PPLZ_IOUtil.PrintOut}.
     *
     * @param totalLabel [in]全部要列印的總張數. 數值應為 "同樣的標籤列印份數" * "序列標籤數"; 範圍: >= 1.
     * @param pauseCutValue [in]暫停和裁列的張數. 範圍: >= 0.
     * @param copies [in]每個序列值要列印的份數. 範圍: >= 0.
     * @param usePauseCut [in]是否每列印 'pauseCutValue' 張數就要暫停或裁切. 'true' 為要暫停;
     * 'false' 為不暫停.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZ_IOUtil.PrintOut}
     */
    public final void setPrintOut(int totalLabel, int pauseCutValue, int copies, boolean usePauseCut) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.SetUtil.SetPrintOut()" + InternalData.szEnter;
            strLog += "	totalLabel: " + totalLabel + InternalData.szEnter;
            strLog += "	pauseCutValue: " + pauseCutValue + InternalData.szEnter;
            strLog += "	copies: " + copies + InternalData.szEnter;
            strLog += "	usePauseCut: " + usePauseCut + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (totalLabel < 1 || pauseCutValue < 0 || copies < 0) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^PQ" + totalLabel + "," + pauseCutValue + "," + copies + ((true == usePauseCut) ? ",N" : ",Y");
        strTemp += "\r\n^XZ\r\n^XA\r\n"; //end format / start format.
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }
}
