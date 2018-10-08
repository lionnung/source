package com.argox.sdk.barcodeprinter.emulation.pplz;

import android.graphics.Bitmap;
import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;
import com.argox.sdk.barcodeprinter.BarcodePrinterIllegalArgumentException;
import com.argox.sdk.barcodeprinter.util.InternalData;
import com.argox.sdk.barcodeprinter.util.LogFile;
import com.argox.sdk.barcodeprinter.util.Out;
import com.argox.sdk.barcodeprinter.util.RefObject;
import java.io.File;

/**
 * 此類別提供 PPLZ 模擬器的繪圖相關程序的方法. 此類別用於 PPLZ 類別中.
 *
 */
public class PPLZ_GraphicsUtil {

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
    public PPLZ_GraphicsUtil(PPLZ emulation) {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.PPLZ_GraphicsUtil.PPLZ_GraphicsUtil()" + InternalData.szEnter;
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
     * 此函式用來畫方框, 並可指定邊框的粗細.
     *
     * @param x [in]X 軸的座標; 單位: 像素.
     * @param y [in]Y 軸的座標; 單位: 像素.
     * @param width [in]線的寬度; 單位: 像素.
     * @param height [in]線的長度; 單位: 像素.
     * @param thickness [in]線條的粗細; 單位: 像素.
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    private final void drawBox(int x, int y, int width, int height, int thickness) throws BarcodePrinterGeneralException {
        String strTemp;
        strTemp = "^FO" + x + "," + y + "^GB" + width + "," + height + "," + thickness + "^FS\r\n";
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來畫直線.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param width [in]線的寬度. 範圍: 1~32000; 單位: 像素.
     * @param height [in]線的長度. 範圍: 1~32000; 單位: 像素.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void printLine(int x, int y, int width, int height) throws BarcodePrinterGeneralException, BarcodePrinterIllegalArgumentException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.GraphicsUtil.PrintLine()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	width: " + width + InternalData.szEnter;
            strLog += "	height: " + height + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (width < 1 || width > PPLZ.getMAX_LABEL_WH()) || (height < 1 || height > PPLZ.getMAX_LABEL_WH())) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        drawBox(x, y, width, height, (width > height) ? height : width);
    }

    /**
     * 此函式用來畫方框.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param width [in]線的寬度. 範圍: 1~32000; 單位: 像素.
     * @param height [in]線的長度. 範圍: 1~32000; 單位: 像素.
     * @param thickness [in]線條的粗細. 範圍: 1~32000; 單位: 像素.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     */
    public final void printBox(int x, int y, int width, int height, int thickness) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.GraphicsUtil.PrintBox()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	width: " + width + InternalData.szEnter;
            strLog += "	height: " + height + InternalData.szEnter;
            strLog += "	thickness: " + thickness + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (width < 1 || width > PPLZ.getMAX_LABEL_WH()) || (height < 1 || height > PPLZ.getMAX_LABEL_WH()) || (thickness < 1 || thickness > PPLZ.getMAX_LABEL_WH())) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        drawBox(x, y, width, height, thickness);
    }

    /**
     * 此函式用來依使用者所指定的影像名稱及儲存位置, 然後將圖形資料儲存至印表機中.
     *
     * @param fileName [in]要轉換或下載的圖形檔名, 可為絕對 (最好) 路徑或相對路徑兩者其一. 資料型態: Unicode;
     * 支援的格式: BMP, GIF, EXIG, JPG, PNG 及 TIFF.
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param imageName [in]要儲存的影像名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     * @throws BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤, 或讀取檔案資料失敗.
     *
     *
     * <p>
     * 此函式只做儲存圖形資料的動作, 並將其內容儲存在印表機的儲存裝置中. 若需要列印時, 請呼叫
     * {@link PrintStoreGraphic}.</p>
     * <p>
     * 且因為快閃記憶體有存取壽命的限制, 除非圖形資料要重複使用, 否則建議將圖形存到 DRAM 中.</p>
     *
     * {@link PPLZStorage}
     * {@link PrintStoreGraphic#printStoreGraphic} // 4test.再補一個可以放大倍數的.
     */
    public final void storeGraphic(String fileName, PPLZStorage storageType, String imageName) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if ((false == new File(fileName).exists()) || (false == InternalFun.isObjectName(imageName))) {
            throw new BarcodePrinterIllegalArgumentException();
        }
        Bitmap bitmap = InternalFun.loadBitmap(new File(fileName));
        if (bitmap == null) {
            throw new BarcodePrinterIllegalArgumentException();
        }
        storeGraphic(bitmap, storageType, imageName);
    }

    /**
     * 此函式用來依使用者所指定的影像名稱及儲存位置, 然後將圖形資料儲存至印表機中.
     *
     * @param bitmap [in]要轉換或下載的圖形點陣圖; 支援的格式: BMP, GIF, EXIG, JPG, PNG 及 TIFF.
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param imageName [in]要儲存的影像名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     * @throws BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤, 或讀取檔案資料失敗.
     *
     *
     * <p>
     * 此函式只做儲存圖形資料的動作, 並將其內容儲存在印表機的儲存裝置中. 若需要列印時, 請呼叫
     * {@link PrintStoreGraphic}.</p>
     * <p>
     * 且因為快閃記憶體有存取壽命的限制, 除非圖形資料要重複使用, 否則建議將圖形存到 DRAM 中.</p>
     *
     * {@link PPLZStorage}
     * {@link PrintStoreGraphic#printStoreGraphic} // 4test.再補一個可以放大倍數的.
     */
    public final void storeGraphic(Bitmap bitmap, PPLZStorage storageType, String imageName) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.GraphicsUtil.StoreGraphic()" + InternalData.szEnter;
            strLog += "	bitmap: " + bitmap.getWidth() + "x" + bitmap.getHeight() + " image" + InternalData.szEnter;
            strLog += "	storageType: " + storageType + InternalData.szEnter;
            strLog += "	imagename: " + (imageName == null || imageName.isEmpty() ? InternalData.szNull : imageName) + InternalData.szEnter;
            LogFile.append(strLog);
        }

        byte[] monodata = new byte[0];
        byte[] hexbuf = new byte[0];
        int width, height;
        width = height = 0;
        RefObject<Integer> tempRef_width = new RefObject<Integer>(width);
        RefObject<Integer> tempRef_height = new RefObject<Integer>(height);
        RefObject<byte[]> tempRef_monodata = new RefObject<byte[]>(monodata);
        boolean tempVar = false == InternalFun.convertColorToMono(bitmap, tempRef_width, tempRef_height, tempRef_monodata);
        width = tempRef_width.value;
        height = tempRef_height.value;
        monodata = tempRef_monodata.value;
        if (tempVar) {
            throw new BarcodePrinterGeneralException();
        }

        String strTemp;
        strTemp = "~DG" + (char) storageType.getValue() + ":" + imageName + ".GRF," + monodata.length + "," + (monodata.length / height) + ","; //Download Graphics
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
        RefObject<byte[]> tempRef_hexbuf = new RefObject<byte[]>(hexbuf);
        boolean tempVar2 = false == InternalFun.convertDataToHex(monodata, tempRef_hexbuf);
        hexbuf = tempRef_hexbuf.value;
        if (tempVar2) {
            throw new BarcodePrinterGeneralException();
        }
        Out.appendtoBuffer(hexbuf, this.parent.queueBuf);

        strTemp = InternalData.szEnter;
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來指定一個存在印表機中的影像名稱, 並將其列印出來.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param imagename [in]要畫的影像名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @param horizontal [in]水平放大倍數. 範圍: 1~10; 預設值: 1.
     * @param vertical [in]垂直放大倍數. 範圍: 1~10; 預設值: 1.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZStorage}
     */
    public final void printStoreGraphic(int x, int y, PPLZStorage storageType, String imagename, int horizontal, int vertical) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.GraphicsUtil.PrintStoreGraphic()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	storageType: " + storageType + InternalData.szEnter;
            strLog += "	imagename: " + (imagename == null || imagename.isEmpty() ? InternalData.szNull : imagename) + InternalData.szEnter;
            strLog += "	horizontal: " + horizontal + InternalData.szEnter;
            strLog += "	vertical: " + vertical + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (false == InternalFun.isObjectName(imagename)) || (horizontal < 1 || horizontal > 10) || (vertical < 1 || vertical > 10)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^XG" + (char) storageType.getValue() + ":" + imagename + ".GRF," + horizontal + "," + vertical + "^FS\r\n";

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來刪除儲存在印表機中的影像物件.
     *
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param imagename [in]要刪除的影像物件名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZStorage}
     */
    public final void deleteStoreGraphic(PPLZStorage storageType, String imagename) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.GraphicsUtil.DeleteStoreGraphic()" + InternalData.szEnter;
            strLog += "	storageType: " + storageType + InternalData.szEnter;
            strLog += "	imagename: " + (imagename == null || imagename.isEmpty() ? InternalData.szNull : imagename) + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((false == InternalFun.isObjectName(imagename))) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^ID" + (char) storageType.getValue() + ":" + imagename + ".GRF" + InternalData.szEnter;

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }
}
