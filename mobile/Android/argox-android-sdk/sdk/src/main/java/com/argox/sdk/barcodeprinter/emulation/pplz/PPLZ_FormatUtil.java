package com.argox.sdk.barcodeprinter.emulation.pplz;

import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;
import com.argox.sdk.barcodeprinter.BarcodePrinterIllegalArgumentException;
import com.argox.sdk.barcodeprinter.util.InternalData;
import com.argox.sdk.barcodeprinter.util.LogFile;
import com.argox.sdk.barcodeprinter.util.Out;

/**
 * 此類別提供 PPLZ 模擬器的 'Format' 相關程序的方法. 此類別用於 PPLZ 類別中.
 *
 */
public class PPLZ_FormatUtil {

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
    public PPLZ_FormatUtil(PPLZ emulation) {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.PPLZ_FormatUtil.PPLZ_FormatUtil()" + InternalData.szEnter;
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
     * 此函式用來建立及開始一個儲存在印表機中的 'Format', 且這函式應該和 {@link StoreFormatEnd} 成對使用.
     *
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param name [in]要建立的 'Format' 名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     * @throws BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     *
     * <p>
     * 若沒有和 {@link StoreFormatEnd} 成對使用時, 則會有不可預期的錯誤, 使用時需注意.</p>
     * <p>
     * 此函式只做建立及下載 'Format' 的動作, 然後將 'Format' 的內容儲存在印表機的儲存裝置中. 不會執行任何列印或刪除
     * 'Format' 的動作.</p>
     * <p>
     * 因此, 若需要執行 (列印) 時, 請呼叫 {@link PrintStoreFormat}; 若需要刪除, 請呼叫
     * {@link DeleteStoreFormat}.</p>
     * <p>
     * 且因為快閃記憶體有存取壽命的限制, 除非 'Format' 要重複使用, 否則建議將 'Format' 存到 DRAM 中.</p>
     *
     * {@link PPLZStorage}
     * {@link StoreFormatEnd#storeFormatEnd}
     * {@link PrintStoreFormat#printStoreFormat}
     * {@link DeleteStoreFormat#deleteStoreFormat}
     */
    public final void storeFormatStart(PPLZStorage storageType, String name) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.FormatUtil.StoreFormatStart()" + InternalData.szEnter;
            strLog += "	name: " + (name == null || name.isEmpty() ? InternalData.szNull : name) + InternalData.szEnter;
            strLog += "	storageType: " + storageType + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((false == InternalFun.isObjectName(name))) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^DF" + (char) storageType.getValue() + ":" + name + ".ZPL" + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來將一個建立中的 'Format' 做上結束記號, 且這函式應該和 {@link StoreFormatStart}
     * 成對使用.
     *
     * 若沒有和 {@link StoreFormatStart} 成對使用時, 則會有不可預期的錯誤, 使用時需注意.
     *
     * @throws BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link StoreFormatStart#StartstoreFormat}
     */
    public final void storeFormatEnd() throws BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.FormatUtil.StoreFormatEnd()" + InternalData.szEnter;
            LogFile.append(strLog);
        }
        String strTemp;
        strTemp = "^XZ" + InternalData.szEnter + "^XA" + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來刪除儲存在印表機中的 'Format'.
     *
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param name [in]要刪除的 'Format' 名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZStorage}
     */
    public final void deleteStoreFormat(PPLZStorage storageType, String name) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.FormatUtil.DeleteStoreFormat()" + InternalData.szEnter;
            strLog += "	storageType: " + storageType + InternalData.szEnter;
            strLog += "	name: " + (name == null || name.isEmpty() ? InternalData.szNull : name) + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((false == InternalFun.isObjectName(name))) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^ID" + (char) storageType.getValue() + ":" + name + ".ZPL" + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來執行 (列印) 儲存在印表機中的 'Format'.
     *
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param name [in]要建立的 'Format' 名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZStorage}
     */
    public final void printStoreFormat(PPLZStorage storageType, String name) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.FormatUtil.PrintStoreFormat()" + InternalData.szEnter;
            strLog += "	name: " + (name == null || name.isEmpty() ? InternalData.szNull : name) + InternalData.szEnter;
            strLog += "	storageType: " + storageType + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((false == InternalFun.isObjectName(name))) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^XF" + (char) storageType.getValue() + ":" + name + ".ZPL" + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }
}
