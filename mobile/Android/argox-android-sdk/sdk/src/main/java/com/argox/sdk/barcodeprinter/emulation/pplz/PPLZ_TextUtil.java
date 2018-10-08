package com.argox.sdk.barcodeprinter.emulation.pplz;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;
import com.argox.sdk.barcodeprinter.BarcodePrinterIllegalArgumentException;
import com.argox.sdk.barcodeprinter.util.InternalData;
import com.argox.sdk.barcodeprinter.util.LogFile;
import com.argox.sdk.barcodeprinter.util.Out;
import com.argox.sdk.barcodeprinter.util.RefObject;

/**
 * 此類別提供 PPLZ 模擬器的文字相關程序的方法. 此類別用於 PPLZ 類別中.
 *
 */
public class PPLZ_TextUtil {

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
    public PPLZ_TextUtil(PPLZ emulation) {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.PPLZ_TextUtil.PPLZ_TextUtil()" + InternalData.szEnter;
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
     * 此函式用來列印文字字串, 並指定印表機內部的內建字型來做列印.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param orient [in]列印方向定位. 使用 {@link PPLZOrient} 列舉來設定.
     * @param font [in]選擇字形. 分別有點陣字型及線性字形兩大類; 使用 {@link PPLZFont} 列舉來設定.
     * @param width
     * <p>
     * [in]字的寬度. 範圍: 點陣字型為原字型大小的 1~10 倍 (請參考
     * {@link PPLZFont">點陣字型的大小</see>).</p>
     * <p>
     * 線性字形. 範圍: 10~32000; 單位: 像素.</p>
     *
     * @param height
     * <p>
     * [in]字的長度. 範圍: 點陣字型為原字型大小的 1~10 倍 (請參考
     * {@link PPLZFont">點陣字型的大小</see>).</p>
     * <p>
     * 線性字形. 範圍: 10~32000; 單位: 像素.</p>
     *
     * @param data [in]要列印的字串. 資料型態: 位元組陣列.
     * @param increase [in]增量或減量. 若有一個 "序列數值" 要列印時, 則條碼資訊會依此序列數值的改變而改變資料, 其數字以
     * 0~9 循環, 字母以 A~Z 或 a~z 循環.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZOrient}
     * {@link PPLZFont}
     */
    public final void printText(int x, int y, PPLZOrient orient, PPLZFont font, int width, int height, byte[] data, int increase) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.TextUtil.PrintText()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	orient: " + orient + InternalData.szEnter;
            strLog += "	font: " + font + InternalData.szEnter;
            strLog += "	width: " + width + InternalData.szEnter;
            strLog += "	height: " + height + InternalData.szEnter;
            strLog += "	data: ";
            if (null == data) {
                strLog += InternalData.szNull;
                LogFile.append(strLog);
            } else {
                LogFile.append(strLog);
                LogFile.append(data, 0, data.length);
            }
            strLog = InternalData.szEnter;
            strLog += "	increase: " + increase + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (width < 1 || width > PPLZ.getMAX_LABEL_WH()) || (height < 1 || height > PPLZ.getMAX_LABEL_WH()) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        int minW = 1, minH = 1, maxW, maxH;
        switch ((char) font.getValue()) {
            case 'A':
                maxH = 90;
                maxW = 50;
                break;
            case 'B':
                maxH = 110;
                maxW = 70;
                break;
            case 'C':
            case 'D':
                maxH = 180;
                maxW = 100;
                break;
            case 'E':
                maxH = 280;
                maxW = 150;
                break;
            case 'F':
                maxH = 260;
                maxW = 130;
                break;
            case 'G':
                maxH = 600;
                maxW = 400;
                break;
            case 'H':
                maxH = 210;
                maxW = 130;
                break;
            case '0':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
                minH = 10;
                minW = 10;
                maxH = PPLZ.getMAX_LABEL_WH();
                maxW = PPLZ.getMAX_LABEL_WH();
                break;
            default:
                minH = minW = maxH = maxW = Integer.MIN_VALUE;
                break;
        }
        if ((height < minH || height > maxH) || (width < minW || width > maxW)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^A" + (char) font.getValue() + (char) orient.getValue() + "," + height + "," + width; //字型種類.
        strTemp += "^FD";

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
        Out.copyToBuffer(data, 0, data.length, this.parent.queueBuf);

        if (0 != increase) {
            String maskstr = null, incstr = null;
            RefObject<String> tempRef_maskstr = new RefObject<String>(maskstr);
            RefObject<String> tempRef_incstr = new RefObject<String>(incstr);
            InternalFun.convertSFCommand(data, increase, tempRef_maskstr, tempRef_incstr);
            maskstr = tempRef_maskstr.value;
            incstr = tempRef_incstr.value;
            strTemp = "^SF" + maskstr + "," + ((increase < 0) ? "-" : "") + incstr + "^FS\r\n";
        } else {
            strTemp = "^FS\r\n";
        }

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來下載使用系統字型轉出的文字圖形, 將圖形儲存在印表機中.
     *
     * @param fontname [in]系統的字型名稱. 資料型態: Unicode.
     * @param fontsize [in]字型的大小. 範圍: >= 0.
     * @param useBold [in]使用粗體文字. 'true' 為使用; 'false' 為不使用.
     * @param useItalic [in]使用斜體文字. 'true' 為使用; 'false' 為不使用.
     * @param useUnderline [in]使用加上底線的文字. 'true' 為使用; 'false' 為不使用.
     * @param useStrikeout [in]使用中間有線條經過的文字. 'true' 為使用; 'false' 為不使用.
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param imagename [in]要儲存的影像名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @param data [in]要轉換成圖形檔的的文字字串, 不可為空字串. 資料型態: Unicode.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤, 或產生圖形資料失敗.
     *
     *
     * <p>
     * 此函式只做儲存圖形資料的動作, 並將其內容儲存在印表機的儲存裝置中. 若需要列印時, 請呼叫
     * {@link PPLZ_GraphicsUtil.PrintStoreGraphic}.</p>
     * <p>
     * 且因為快閃記憶體有存取壽命的限制, 除非圖形資料要重複使用, 否則建議將圖形存到 DRAM 中.</p>
     *
     * {@link PPLZStorage}
     * @see PPLZ_GraphicsUtil.PrintStoreGraphic PrintStoreGraphic
     * //4test.此函式不夠完美, 少了旋轉, 直式文字.
     */
    public final void storeTextGraphic(Typeface fontType, int fontsize, boolean useBold, boolean useItalic, boolean useUnderline, boolean useStrikeout, PPLZStorage storageType, String imagename, String data) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        storeTextGraphic(fontType, fontsize, useBold, useItalic, useUnderline, useStrikeout, storageType, imagename, data, 0, 0);
    }

    /**
     * 此函式用來下載使用系統字型轉出的文字圖形, 將圖形儲存在印表機中.
     *
     * @param fontname [in]系統的字型名稱. 資料型態: Unicode.
     * @param fontsize [in]字型的大小. 範圍: >= 0.
     * @param useBold [in]使用粗體文字. 'true' 為使用; 'false' 為不使用.
     * @param useItalic [in]使用斜體文字. 'true' 為使用; 'false' 為不使用.
     * @param useUnderline [in]使用加上底線的文字. 'true' 為使用; 'false' 為不使用.
     * @param useStrikeout [in]使用中間有線條經過的文字. 'true' 為使用; 'false' 為不使用.
     * @param storageType [in]儲存裝置的位置. 使用 {@link PPLZStorage} 列舉來設定.
     * @param imagename [in]要儲存的影像名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @param data [in]要轉換成圖形檔的的文字字串, 不可為空字串. 資料型態: Unicode.
     * @param rotation [in]旋轉角度. 範圍: 0, 90, 180, 270.
     * @param useWidth [in]使用者自訂寬度. 範圍: >= 0; 0 表示系統自動計算寬度.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤, 或產生圖形資料失敗.
     *
     *
     * <p>
     * 此函式只做儲存圖形資料的動作, 並將其內容儲存在印表機的儲存裝置中. 若需要列印時, 請呼叫
     * {@link PPLZ_GraphicsUtil.PrintStoreGraphic}.</p>
     * <p>
     * 且因為快閃記憶體有存取壽命的限制, 除非圖形資料要重複使用, 否則建議將圖形存到 DRAM 中.</p>
     *
     * {@link PPLZStorage}
     * @see PPLZ_GraphicsUtil.PrintStoreGraphic PrintStoreGraphic
     * //4test.此函式不夠完美, 少了直式文字.
     */
    public final void storeTextGraphic(Typeface fontType, int fontsize, boolean useBold, boolean useItalic, boolean useUnderline, boolean useStrikeout, PPLZStorage storageType, String imagename, String data, int rotation, int useWidth) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.TextUtil.StoreTextGraphic()" + InternalData.szEnter;
            strLog += "	fontname: " + (fontType == null ? InternalData.szNull : "some font") + InternalData.szEnter;
            strLog += "	fontsize: " + fontsize + InternalData.szEnter;
            strLog += "	useBold: " + useBold + InternalData.szEnter;
            strLog += "	useItalic: " + useItalic + InternalData.szEnter;
            strLog += "	useUnderline: " + useUnderline + InternalData.szEnter;
            strLog += "	useStrikeout: " + useStrikeout + InternalData.szEnter;
            strLog += "	storageType: " + storageType + InternalData.szEnter;
            strLog += "	imagename: " + (imagename == null || imagename.isEmpty() ? InternalData.szNull : imagename) + InternalData.szEnter;
            strLog += "	data: " + (data == null || data.isEmpty() ? InternalData.szNull : data) + InternalData.szEnter;
            strLog += " rotation: " + rotation + InternalData.szEnter;
            strLog += " useWidth: " + useWidth + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if (fontType == null || (fontsize < 0) || (false == InternalFun.isObjectName(imagename)) || data == null || data.isEmpty() ||
                (rotation != 0 && rotation != 90 && rotation != 180 && rotation != 270) || useWidth < 0) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        byte[] monodata = new byte[0];
        byte[] hexbuf = new byte[0];
        int index = 2;
        int width, height;
        width = height = 10;
        try {
            //第一次主要是要取字串大小, 第二次resize成字串真實大小, 然後取出圖形資料.
            Bitmap img = createTextBitmap(fontType, fontsize, useBold, useItalic, useUnderline, useStrikeout, data, useWidth);
            img = InternalFun.rotateToDegrees(img, rotation);
            width = img.getWidth();
            height = img.getHeight();
            RefObject<Integer> tempRef_width = new RefObject<Integer>(width);
            RefObject<Integer> tempRef_height = new RefObject<Integer>(height);
            RefObject<byte[]> tempRef_monodata = new RefObject<byte[]>(monodata);
            InternalFun.convertTextColorToMono(img, tempRef_width, tempRef_height, tempRef_monodata);
            width = tempRef_width.value;
            height = tempRef_height.value;
            monodata = tempRef_monodata.value;
        } catch (java.lang.Exception e) {
            throw new BarcodePrinterGeneralException();
        }

        String strTemp;
        strTemp = "~DG" + (char) storageType.getValue() + ":" + imagename + ".GRF," + monodata.length + "," + (monodata.length / height) + ","; //Download Graphics
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
        RefObject<byte[]> tempRef_hexbuf = new RefObject<byte[]>(hexbuf);
        boolean tempVar = false == InternalFun.convertDataToHex(monodata, tempRef_hexbuf);
        hexbuf = tempRef_hexbuf.value;
        if (tempVar) {
            throw new BarcodePrinterGeneralException();
        }
        Out.appendtoBuffer(hexbuf, this.parent.queueBuf);

        strTemp = InternalData.szEnter;
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    public Bitmap createTextBitmap(Typeface fontType, int fontsize, boolean useBold, boolean useItalic, boolean useUnderline, boolean useStrikeout, String data) {
        return createTextBitmap(fontType, fontsize, useBold, useItalic, useUnderline, useStrikeout, data, 0);
    }

    public Bitmap createTextBitmap(Typeface fontType, int fontsize, boolean useBold, boolean useItalic, boolean useUnderline, boolean useStrikeout, String data, int useWidth) {
        Paint paint = new Paint();
        if (useItalic && (fontType == Typeface.DEFAULT || fontType == Typeface.DEFAULT_BOLD || fontType == Typeface.SANS_SERIF || fontType == Typeface.SERIF)) {
            paint.setTypeface(Typeface.create(fontType, Typeface.ITALIC));
        } else {
            paint.setTypeface(fontType);
        }
        paint.setTextSize(fontsize);
        paint.setColor(Color.WHITE);
        paint.setUnderlineText(useUnderline);
        paint.setStrikeThruText(useStrikeout);
        paint.setFakeBoldText(useBold);
        paint.setTextAlign(Paint.Align.LEFT);
        int width = (int) (paint.measureText(data) + 0.5f); // round
        float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(useWidth == 0 ? width : useWidth, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(data, 0, baseline, paint);
        return image;
    }
}
