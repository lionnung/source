package com.argox.sdk.barcodeprinter.emulation.pplz;

import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;
import com.argox.sdk.barcodeprinter.BarcodePrinterIllegalArgumentException;
import com.argox.sdk.barcodeprinter.util.InternalData;
import com.argox.sdk.barcodeprinter.util.LogFile;
import com.argox.sdk.barcodeprinter.util.Out;
import com.argox.sdk.barcodeprinter.util.RefObject;
import java.text.DecimalFormat;

/**
 * 此類別提供 PPLZ 模擬器條碼相關程序的方法. 此類別用於 PPLZ 類別中.
 *
 */
public class PPLZ_BarcodeUtil {

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
    public PPLZ_BarcodeUtil(PPLZ emulation) {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.PPLZ_BarcodeUtil.PPLZ_BarcodeUtil()" + InternalData.szEnter;
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
     * 此函式用來繪製一維條碼.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param orient [in]列印方向定位. 使用 {@link PPLZOrient} 列舉來設定.
     * @param narrow [in]窄條寬度 (又稱模組寬度), 在此作用為放大. 範圍: 1~10; 單位: 像素.
     * @param wide [in]寬條寬度. 範圍: wide/narrow = 2.0~3.0; 請參考 "一維條碼的種類" 中的 "寬窄比".
     * @param height [in]條碼高度. 當 'type' = 'U','5' 時, 範圍: 1~9999; 其餘的, 範圍:
     * 1~32000; 單位: 像素.
     * @param type [in]一維條碼的種類. 請參考 "一維條碼的種類" 中的 "type"; 使用
     * {@link PPLZBarCodeType} 列舉來設定.
     * @param increase [in]增量或減量. 若有一個 "序列數值" 要列印時, 則條碼資訊會依此序列數值的改變而改變資料, 其數字以
     * 0~9 循環, 字母以 A~Z 或 a~z 循環.
     * @param data [in]條碼資料. 資料型態: 位元組陣列.
     * @param p1 [in]條碼列印條件. 請參考 "一維條碼的種類" 中的 "參數".
     * @param p2 [in]條碼列印條件. 請參考 "一維條碼的種類" 中的 "參數".
     * @param p3 [in]條碼列印條件. 請參考 "一維條碼的種類" 中的 "參數".
     * @param p4 [in]條碼列印條件. 請參考 "一維條碼的種類" 中的 "參數".
     * @param p5 [in]條碼列印條件. 請參考 "一維條碼的種類" 中的 "參數".
     *
     * <p>
     * 一維條碼的種類</p>
     * <p>
     * 備註: 若在 p1, p2, p3, p4, p5 中未使用到的參數則不理會輸入值.</p>
     * <list type="table">
     * <listheader>
     * <term>type</term>
     * <term>條碼種類</term>
     * <term>參數</term>
     * <term>寬窄比</term>
     * </listheader>
     * <item>
     * <term>1</term><term>Code 11</term>
     * <term>
     * <p>
     * p1 = 確認碼. 'Y' = 1 位數; 'N' = 2 位數.</p>
     * <p>
     * p2 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>2.0-3.0</term>
     * </item>
     * <item>
     * <term>2</term><term>Interleaved 2 of 5</term>
     * <term>
     * <p>
     * p1 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p2 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 是否列印 Mod-10 確認碼. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>2.0-3.0</term>
     * </item>
     * <item>
     * <term>3</term><term>Code 39</term>
     * <term>
     * <p>
     * p1 = 是否列印 Mod-43 確認碼. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p2 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>2.0-3.0</term>
     * </item>
     * <item>
     * <term><p>
     * 5</p><para>8</para><para>E</para><para>S</para></term>
     * <term><p>
     * Planet Code</p><para>EAN-8</para><para>EAN-13</para><para>UPC/EAN
     * Extensions</para></term>
     * <term>
     * <p>
     * p1 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p2 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>固定</term>
     * </item>
     * <item>
     * <term><p>
     * 9</p><para>A</para><para>U</para></term>
     * <term><p>
     * UPC-E</p><para>Code 93</para><para>UPC-A</para></term>
     * <term>
     * <p>
     * p1 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p2 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 是否列印確認碼. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>固定</term>
     * </item>
     * <item>
     * <term>C</term><term><p>
     * Code 128 Subset A,B,C</p><para>Code 128 UCC</para></term>
     * <term>
     * <p>
     * p1 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p2 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 是否列印 UCC Mod 10 確認碼. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p4 = 模式.</p>
     * <list type="bullet">
     * <item>'N' = 不選擇的模式.</item>
     * <item>'U' = UCC 案例模式.</item>
     * <item>'A' = 自動模式.</item>
     * </list>
     * </term>
     * <term>固定</term>
     * </item>
     * <item>
     * <term><p>
     * I</p><para>J</para></term>
     * <term><p>
     * Industrial 2 of 5</p><para>Standard 2 of 5</para></term>
     * <term>
     * <p>
     * p1 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p2 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>2.0-3.0</term>
     * </item>
     * <item>
     * <term>K</term><term>ANSI Codabar</term>
     * <term>
     * <p>
     * p1 = 是否列印確認碼. 其值固定為 'N'.</p>
     * <p>
     * p2 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p4 = 開始字元. 字元包含: 'A', 'B', 'C', 'D', '*', 'N', 'E', 'T'.</p>
     * <p>
     * p5 = 結束字元. 字元包含: 'A', 'B', 'C', 'D', '*', 'N', 'E', 'T'.</p>
     * </term>
     * <term>2.0-3.0</term>
     * </item>
     * <item>
     * <term>L</term><term>LOGMARS</term>
     * <term>
     * <p>
     * p1 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>2.0-3.0</term>
     * </item>
     * <item>
     * <term>M</term><term>MSI</term>
     * <term>
     * <p>
     * p1 = 確認碼.</p>
     * <list type="bullet">
     * <item>'A' = 無確認碼.</item>
     * <item>'B' = 1 Mod 10.</item>
     * <item>'C' = 2 Mod 10.</item>
     * <item>'D' = 1 Mod 10 和 1 Mod 11.</item>
     * </list>
     * <p>
     * p2 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p4 = 是否列印確認碼. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>2.0-3.0</term>
     * </item>
     * <item>
     * <term>P</term><term>Plessey</term>
     * <term>
     * <p>
     * p1 = 是否列印確認碼. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p2 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * </term>
     * <term>2.0-3.0</term>
     * </item>
     * <item>
     * <term><p>
     * Z</p></term>
     * <term><p>
     * POSTAL</p></term>
     * <term>
     * <p>
     * p1 = 是否列印條碼資料. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p2 = 條碼資料是否列印在條碼上方. 'Y' = 是; 'N' = 否.</p>
     * <p>
     * p3 = 型態.</p>
     * <list type="bullet">
     * <item>'0' = Postnet 條碼.</item>
     * <item>'1' = Planet 條碼.</item>
     * <item>'3' = Intelligent Mail 條碼. 範圍: 20~31; 單位: 字元.</item>
     * </list>
     * </term>
     * <term>固定</term>
     * </item>
     * </list>
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZOrient}
     * {@link PPLZBarCodeType}
     */
    public final void printOneDBarcode(int x, int y, PPLZOrient orient, int narrow, int wide, int height, PPLZBarCodeType type, int increase, byte[] data, char p1, char p2, char p3, char p4, char p5) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.BarcodeUtil.PrintOneDBarcode()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	orient: " + orient + InternalData.szEnter;
            strLog += "	narrow: " + narrow + InternalData.szEnter;
            strLog += "	wide: " + wide + InternalData.szEnter;
            strLog += "	height: " + height + InternalData.szEnter;
            strLog += "	type: " + type + InternalData.szEnter;
            strLog += "	increase: " + increase + InternalData.szEnter;
            strLog += "	data: ";
            if (null == data) {
                strLog += InternalData.szNull;
                LogFile.append(strLog);
            } else {
                LogFile.append(strLog);
                LogFile.append(data, 0, data.length);
            }
            strLog = InternalData.szEnter;
            strLog += "	p1: " + p1 + InternalData.szEnter;
            strLog += "	p2: " + p2 + InternalData.szEnter;
            strLog += "	p3: " + p3 + InternalData.szEnter;
            strLog += "	p4: " + p4 + InternalData.szEnter;
            strLog += "	p5: " + p5 + InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (narrow < 1 || narrow > 10) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        float radio = (float) wide / narrow;

        char cType;
        cType = (char) type.getValue();
        if ('U' == cType || '5' == cType) {
            if (height < 1 || height > 9999) {
                throw new BarcodePrinterIllegalArgumentException();
            }
        } else if (height < 1 || height > PPLZ.getMAX_LABEL_WH()) {
            throw new BarcodePrinterIllegalArgumentException();
        }
        p1 = Character.toUpperCase(p1);
        p2 = Character.toUpperCase(p2);
        p3 = Character.toUpperCase(p3);
        p4 = Character.toUpperCase(p4);
        p5 = Character.toUpperCase(p5);

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.

        strTemp += "^BY" + narrow + "," + new DecimalFormat("0.0").format(radio); //寬/寬窄比.
        strTemp += "^B" + cType + (char) orient.getValue() + ","; //條碼種類.

        switch (cType) {
            case '1': // Code 11
            case '3': // Code 39
            case 'P': // Plessey
                if ((false == InternalFun.checkWidthHeightRadio(radio)) || (p1 != 'Y' && p1 != 'N') || (p2 != 'Y' && p2 != 'N') || (p3 != 'Y' && p3 != 'N')) {
                    throw new BarcodePrinterIllegalArgumentException();
                }

                strTemp += p1 + "," + height + "," + p2 + "," + p3;
                break;
            case '2': // Interleaved 2 of 5
            case '9': // UPC-E
            case 'A': // Code 93
            case 'U': // UPC-A
                if (('2' == cType) && (false == InternalFun.checkWidthHeightRadio(radio))) {
                    throw new BarcodePrinterIllegalArgumentException();
                }
                if ((p1 != 'Y' && p1 != 'N') || (p2 != 'Y' && p2 != 'N') || (p3 != 'Y' && p3 != 'N')) {
                    throw new BarcodePrinterIllegalArgumentException();
                }

                strTemp += height + "," + p1 + "," + p2 + "," + p3;
                break;
            case '5': // Planet Code
            case '8': // EAN-8
            case 'E': // EAN-13
            case 'I': // Industrial 2 of 5
            case 'J': // Standard 2 of 5
            case 'S': // UPC/EAN Extensions
                if (('I' == cType || 'J' == cType) && (false == InternalFun.checkWidthHeightRadio(radio))) {
                    throw new BarcodePrinterIllegalArgumentException();
                }
                if ((p1 != 'Y' && p1 != 'N') || (p2 != 'Y' && p2 != 'N')) {
                    throw new BarcodePrinterIllegalArgumentException();
                }

                strTemp += height + "," + p1 + "," + p2;
                break;
            case 'Z': // POSTAL
                if ((p1 != 'Y' && p1 != 'N') || (p2 != 'Y' && p2 != 'N')) {
                    throw new BarcodePrinterIllegalArgumentException();
                }
                if (p3 != '0' && p3 != '1' && p3 != '3') {
                    throw new BarcodePrinterIllegalArgumentException();
                }
                if (p3 == '3' && (data.length < 20 || data.length > 31)) {
                    throw new BarcodePrinterIllegalArgumentException();
                }

                strTemp += height + "," + p1 + "," + p2 + "," + p3;
                break;
            case 'C': // Code 128(Subsets A,B,C,Autoexec) or Code 128 UCC
                if ((p1 != 'Y' && p1 != 'N') || (p2 != 'Y' && p2 != 'N') || (p3 != 'Y' && p3 != 'N') || (p4 != 'N' && p4 != 'U' && p4 != 'A')) {
                    throw new BarcodePrinterIllegalArgumentException();
                }

                strTemp += height + "," + p1 + "," + p2 + "," + p3 + "," + p4;
                break;
            case 'K': // ANSI Codabar
                if (false == InternalFun.checkWidthHeightRadio(radio)) {
                    throw new BarcodePrinterIllegalArgumentException();
                }
                if ((p1 != 'N') || (p2 != 'Y' && p2 != 'N') || (p3 != 'Y' && p3 != 'N') || (p4 != 'A' && p4 != 'B' && p4 != 'C' && p4 != 'D' && p4 != '*' && p4 != 'N' && p4 != 'E' && p4 != 'T') || (p5 != 'A' && p5 != 'B' && p5 != 'C' && p5 != 'D' && p5 != '*' && p5 != 'N' && p5 != 'E' && p5 != 'T')) {
                    throw new BarcodePrinterIllegalArgumentException();
                }

                strTemp += p1 + "," + height + "," + p2 + "," + p3 + "," + p4 + "," + p5;
                break;
            case 'L': // LOGMARS
                if (false == InternalFun.checkWidthHeightRadio(radio)) {
                    throw new BarcodePrinterIllegalArgumentException();
                }
                if ((p1 != 'Y' && p1 != 'N')) {
                    throw new BarcodePrinterIllegalArgumentException();
                }

                strTemp += height + "," + p1;
                break;
            case 'M': // MSI
                if (false == InternalFun.checkWidthHeightRadio(radio)) {
                    throw new BarcodePrinterIllegalArgumentException();
                }
                if ((p1 != 'A' && p1 != 'B' && p1 != 'C' && p1 != 'D') || (p2 != 'Y' && p2 != 'N') || (p3 != 'Y' && p3 != 'N') || (p4 != 'Y' && p4 != 'N')) {
                    strTemp += p1 + "," + height + "," + p2 + "," + p3 + "," + p4;
                }
                break;
            default:
                throw new BarcodePrinterIllegalArgumentException();
        }

        strTemp += "^FD";
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
        if (('C' == cType) && ('>' == data[0] && '9' == data[1])) //code 128中的Subsets A.
        {
            int i, j;
            String str = "";
            str += (char) data[0];
            str += (char) data[1];
            for (i = 2; i < data.length; i++) {
                if (data[i] >= 32 && data[i] <= 95) {
                    j = data[i] - 32;
                } else {
                    j = data[i] + 64;
                }
                str += new DecimalFormat("00").format(j);
            }
            Out.copyToBuffer(str, this.parent.queueBuf);
        } else {
            Out.copyToBuffer(data, 0, data.length, this.parent.queueBuf);
        }

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
     * 此函式用來繪製 MaxiCode 條碼.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param mode [in]MaxiCode 的模式. 使用 {@link PPLZMaxiCodeMode} 列舉來設定.
     * <list type="bullet">
     * <item>Mode_2 = 結構化乘載訊息: 數字郵政代碼 (U.S.).</item>
     * <item>Mode_3 = 結構化乘載訊息: 字母郵政代碼 (非 U.S.).</item>
     * </list>
     *
     * @param service [in]3 碼服務類別. 只在 mode 2 和 mode 3 有作用.
     * @param country [in]3 碼國碼. 只在 mode 2 和 mode 3 有作用.
     * @param zipcode [in]郵遞區號. 只在 mode 2 和 mode 3 有作用.
     * <list type="bullet">
     * <item>mode 2: 請輸入 9 個字元的郵遞區號 (5 位郵遞區號 + 4 位郵遞區號延伸), 若沒有郵遞區號延伸, 請填
     * "0000".</item>
     * <item>mode 3: 請輸入 6 個字元的郵遞區號.</item>
     * </list>
     *
     * @param data [in]條碼資料. 資料型態: 位元組陣列.
     * @param increase [in]增量或減量. 若有一個 "序列數值" 要列印時, 則條碼資訊會依此序列數值的改變而改變資料, 其數字以
     * 0~9 循環, 字母以 A~Z 或 a~z 循環.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZMaxiCodeMode}
     */
    public final void printMaxiCode(int x, int y, PPLZMaxiCodeMode mode, int service, int country, String zipcode, byte[] data, int increase) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.BarcodeUtil.PrintMaxiCode()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	mode: " + mode + InternalData.szEnter;
            strLog += "	service: " + service + InternalData.szEnter;
            strLog += "	country: " + country + InternalData.szEnter;
            strLog += "	zipcode: " + (zipcode == null || zipcode.isEmpty() ? InternalData.szNull : zipcode) + InternalData.szEnter;
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
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (service < 0 || service > 1000) || (country < 0 || country > 1000) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        char cMode = (char) mode.getValue();
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^BD" + cMode; //條碼種類.
        strTemp += "^FD";
        if ('2' == cMode || '3' == cMode) {
            if (('2' == cMode && 9 != zipcode.length()) || ('3' == cMode && 6 != zipcode.length())) {
                throw new BarcodePrinterIllegalArgumentException();
            }

            DecimalFormat df = new DecimalFormat("000");
            strTemp += df.format(service) + df.format(country) + zipcode;
        }
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
     * 此函式用來繪製 QR 條碼.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param model [in]QR 條碼的模組. 使用 {@link PPLZQRCodeModel} 列舉來設定.
     * @param magni [in]放大倍率. 範圍: 1~10.
     * @param errCorrect [in]錯誤校正等級. 使用 {@link PPLZQRCodeErrCorrect}
     * 列舉來設定.
     * @param data [in]條碼資料. 資料型態: 位元組陣列.
     * @param increase [in]增量或減量. 若有一個 "序列數值" 要列印時, 則條碼資訊會依此序列數值的改變而改變資料, 其數字以
     * 0~9 循環, 字母以 A~Z 或 a~z 循環.
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZQRCodeModel}
     * {@link PPLZQRCodeErrCorrect}
     */
    public final void printQRCode(int x, int y, PPLZQRCodeModel model, int magni, PPLZQRCodeErrCorrect errCorrect, byte[] data, int increase) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.BarcodeUtil.PrintQRCode()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	model: " + model + InternalData.szEnter;
            strLog += "	magni: " + magni + InternalData.szEnter;
            strLog += "	errCorrect: " + errCorrect + InternalData.szEnter;
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
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (magni < 1 || magni > 10) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^BQ" + 'N' + "," + (char) model.getValue() + "," + magni; //條碼種類.
        strTemp += "^FD" + (char) errCorrect.getValue() + "A,";

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
     * 此函式用來繪製 PDF417 條碼.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param orient [in]列印方向定位. 使用 {@link PPLZOrient} 列舉來設定.
     * @param height [in]各行的高度. 範圍: 1~9999; 單位: 像素.
     * @param security [in]錯誤校正等級. 範圍: 0~8; 0 表示只有錯誤偵測, 沒有校正.
     * @param columns [in]以幾行資料去編碼. 範圍: 1~30; 0: 意指自動.
     * @param rows [in]以幾列資料去編碼. 範圍: 3~90; 0: 意指自動.
     * @param isTruncate [in]截斷右行中的指示及停止樣式. 'true' 為截斷; 'false' 為不截斷.
     * @param narrow [in]窄條寬度 (又稱模組寬度), 在此作用為放大. 範圍: 1~10; 單位: 像素.
     * @param data [in]條碼資料. 資料型態: 位元組陣列.
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
     */
    public final void printPDF417(int x, int y, PPLZOrient orient, int height, int security, int columns, int rows, boolean isTruncate, int narrow, byte[] data, int increase) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.BarcodeUtil.PrintPDF417()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	orient: " + orient + InternalData.szEnter;
            strLog += "	height: " + height + InternalData.szEnter;
            strLog += "	security: " + security + InternalData.szEnter;
            strLog += "	columns: " + columns + InternalData.szEnter;
            strLog += "	rows: " + rows + InternalData.szEnter;
            strLog += "	isTruncate: " + isTruncate + InternalData.szEnter;
            strLog += "	narrow: " + narrow + InternalData.szEnter;
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
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (height < 0 || height > 9999) || (security < 0 || security > 8) || (columns < 0 || columns > 30) || (rows < 0 || rows > 90) || (narrow < 1 || narrow > 10) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^BY" + narrow; //放大倍數.
        strTemp += "^B7" + (char) orient.getValue() + ","; //條碼種類.
        strTemp += height + "," + security + "," + columns + "," + rows + "," + ((true == isTruncate) ? 'Y' : 'N');
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
     * 此函式用來繪製 MicroPDF417 條碼.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param orient [in]列印方向定位. 使用 {@link PPLZOrient} 列舉來設定.
     * @param height [in]各行的高度. 範圍: 1~9999; 單位: 像素.
     * @param mode [in]MicroPDF417 的模式. 範圍: 0~33; 預設值: 0; 請參考 "MicroPDF417 的模式".
     * @param narrow [in]窄條寬度 (又稱模組寬度), 在此作用為放大. 範圍: 1~10; 單位: 像素.
     * @param data [in]條碼資料. 資料型態: 位元組陣列.
     * @param increase [in]增量或減量. 若有一個 "序列數值" 要列印時, 則條碼資訊會依此序列數值的改變而改變資料, 其數字以
     * 0~9 循環, 字母以 A~Z 或 a~z 循環.
     *
     * <p>
     * MicroPDF417 的模式</p>
     * <list type="table">
     * <listheader>
     * <term>模式</term>
     * <term>資料行數量</term>
     * <term>資料列數量</term>
     * <term>錯誤校正字碼的百分比</term>
     * <term>最大字母字元</term>
     * <term>最大數字</term>
     * </listheader>
     * <item><term>0</term><term>1</term><term>11</term><term>64</term><term>6</term><term>8</term></item>
     * <item><term>1</term><term>1</term><term>14</term><term>50</term><term>12</term><term>17</term></item>
     * <item><term>2</term><term>1</term><term>17</term><term>41</term><term>18</term><term>26</term></item>
     * <item><term>3</term><term>1</term><term>20</term><term>40</term><term>22</term><term>32</term></item>
     * <item><term>4</term><term>1</term><term>24</term><term>33</term><term>30</term><term>44</term></item>
     * <item><term>5</term><term>1</term><term>28</term><term>29</term><term>38</term><term>55</term></item>
     * <item><term>6</term><term>2</term><term>8</term><term>50</term><term>14</term><term>20</term></item>
     * <item><term>7</term><term>2</term><term>11</term><term>41</term><term>24</term><term>35</term></item>
     * <item><term>8</term><term>2</term><term>14</term><term>32</term><term>36</term><term>52</term></item>
     * <item><term>9</term><term>2</term><term>17</term><term>29</term><term>46</term><term>67</term></item>
     * <item><term>10</term><term>2</term><term>20</term><term>28</term><term>56</term><term>82</term></item>
     * <item><term>11</term><term>2</term><term>23</term><term>28</term><term>64</term><term>93</term></item>
     * <item><term>12</term><term>2</term><term>26</term><term>29</term><term>72</term><term>105</term></item>
     * <item><term>13</term><term>3</term><term>6</term><term>67</term><term>10</term><term>14</term></item>
     * <item><term>14</term><term>3</term><term>8</term><term>58</term><term>18</term><term>26</term></item>
     * <item><term>15</term><term>3</term><term>10</term><term>53</term><term>26</term><term>38</term></item>
     * <item><term>16</term><term>3</term><term>12</term><term>50</term><term>34</term><term>49</term></item>
     * <item><term>17</term><term>3</term><term>15</term><term>47</term><term>46</term><term>67</term></item>
     * <item><term>18</term><term>3</term><term>20</term><term>43</term><term>66</term><term>96</term></item>
     * <item><term>19</term><term>3</term><term>26</term><term>41</term><term>90</term><term>132</term></item>
     * <item><term>20</term><term>3</term><term>32</term><term>40</term><term>114</term><term>167</term></item>
     * <item><term>21</term><term>3</term><term>38</term><term>39</term><term>138</term><term>202</term></item>
     * <item><term>22</term><term>3</term><term>44</term><term>38</term><term>162</term><term>237</term></item>
     * <item><term>23</term><term>4</term><term>6</term><term>50</term><term>22</term><term>32</term></item>
     * <item><term>24</term><term>4</term><term>8</term><term>44</term><term>34</term><term>49</term></item>
     * <item><term>25</term><term>4</term><term>10</term><term>40</term><term>46</term><term>67</term></item>
     * <item><term>26</term><term>4</term><term>12</term><term>38</term><term>58</term><term>85</term></item>
     * <item><term>27</term><term>4</term><term>15</term><term>35</term><term>76</term><term>111</term></item>
     * <item><term>28</term><term>4</term><term>20</term><term>33</term><term>106</term><term>155</term></item>
     * <item><term>29</term><term>4</term><term>26</term><term>31</term><term>142</term><term>208</term></item>
     * <item><term>30</term><term>4</term><term>32</term><term>30</term><term>178</term><term>261</term></item>
     * <item><term>31</term><term>4</term><term>38</term><term>29</term><term>214</term><term>313</term></item>
     * <item><term>32</term><term>4</term><term>44</term><term>28</term><term>250</term><term>366</term></item>
     * <item><term>33</term><term>4</term><term>4</term><term>50</term><term>14</term><term>20</term></item>
     * </list>
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZOrient}
     */
    public final void printMicroPDF417(int x, int y, PPLZOrient orient, int height, int mode, int narrow, byte[] data, int increase) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.BarcodeUtil.PrintMicroPDF417()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	orient: " + orient + InternalData.szEnter;
            strLog += "	height: " + height + InternalData.szEnter;
            strLog += "	mode: " + mode + InternalData.szEnter;
            strLog += "	narrow: " + narrow + InternalData.szEnter;
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
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (height < 0 || height > 9999) || (mode < 0 || mode > 33) || (narrow < 1 || narrow > 10) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^BY" + narrow; //放大倍數.
        strTemp += "^BF" + (char) orient.getValue() + ","; //條碼種類.
        strTemp += height + "," + mode;
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
     * 此函式用來繪製 Aztec Code 條碼.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param orient [in]列印方向定位. 使用 {@link PPLZOrient} 列舉來設定.
     * @param magni [in]放大倍率. 範圍: 1~10.
     * @param errCorrect [in]錯誤校正等級.
     * <list type="bullet">
     * <item>0 = 預設的錯誤校正等級.</item>
     * <item>01~99 = 錯誤校正百分比 (最小).</item>
     * <item>101~104 = 1 至 4 階的緊密符號.</item>
     * <item>201~232 = 1 至 32 階的全範圍符號.</item>
     * <item>300 = 一個簡單的 Aztec "Rune".</item>
     * </list>
     *
     * @param data [in]條碼資料. 資料型態: 位元組陣列.
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
     * {@link PPLZRSSType}
     */
    public final void printAztecCode(int x, int y, PPLZOrient orient, int magni, int errCorrect, byte[] data, int increase) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.BarcodeUtil.PrintAztecCode()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	orient: " + orient + InternalData.szEnter;
            strLog += "	magni: " + magni + InternalData.szEnter;
            strLog += "	errCorrect: " + errCorrect + InternalData.szEnter;
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
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (magni < 1 || magni > 10) || (errCorrect < 0 || errCorrect == 100 || (errCorrect > 104 && errCorrect < 201) || (errCorrect > 232 && errCorrect != 300)) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^BO" + (char) orient.getValue() + ","; //條碼種類.
        strTemp += magni + ",N," + errCorrect + ",N,1,0";
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
     * 此函式用來繪製 RSS (Reduced Space Symbology) 條碼.
     *
     * 此條碼又稱之為 GS1 DataBar.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param orient [in]列印方向定位. 使用 {@link PPLZOrient} 列舉來設定.
     * @param type [in]符號種類. 使用 {@link PPLZRSSType} 列舉來設定.
     * @param magni [in]放大倍率. 範圍: 1~10.
     * @param height [in]條碼高度. 範圍: 1~32000; 單位: 像素; 0 為條碼標準值; 請參考 "RSS (GS1
     * DataBar) 的符號種類" 中的 "符號種類標準最小高度".
     * @param segment
     * <p>
     * [in]每列條碼可包含的最大資料區塊數量. 此參數只在 RSS Expanded 條碼時有效; 在其它類型的 RSS 條碼中則無效.</p>
     * <p>
     * 範圍: 2~22 (此值必需為偶數); 預設值: 22.</p>
     * <p>
     * 當此值為 22 時, 為 RSS Expanded; 若為非 22 時, 為 RSS Expanded Stacked.</p>
     *
     * @param data
     * <p>
     * [in]條碼資料. 資料型態: 位元組陣列.</p>
     * <p>
     * 一維條碼的資料格式為 [1D 資料]. 例如: "00614141999996".</p>
     * <p>
     * 一/二維條碼的資料格式為 [1D 資料]|[2D 複合資料], 利用 '|' 字元來分隔 1D/2D 的資料. 例如:
     * "00614141999996|RSS test".</p>
     *
     *
     * <p>
     * RSS (GS1 DataBar) 的符號種類</p>
     * <list type="table">
     * <listheader>
     * <term>type</term>
     * <term>符號種類說明</term>
     * <term><p>
     * 符號種類標準最小高度</p><para>(單位: 像素)</para></term>
     * </listheader>
     * <item>
     * <term>1</term>
     * <term>RSS-14 (GS1 DataBar Omnidirectional).</term>
     * <term>33</term>
     * </item>
     * <item>
     * <term>2</term>
     * <term>RSS Truncated (GS1 DataBar Truncated).</term>
     * <term>13</term>
     * </item>
     * <item>
     * <term>3</term>
     * <term>RSS Stacked (GS1 DataBar Stacked).</term>
     * <term>13</term>
     * </item>
     * <item>
     * <term>4</term>
     * <term>RSS Stacked Omnidirectional (GS1 DataBar Stacked
     * Omnidirectional).</term>
     * <term>69</term>
     * </item>
     * <item>
     * <term>5</term>
     * <term>RSS Limited (GS1 DataBar Limited).</term>
     * <term>10</term>
     * </item>
     * <item>
     * <term>6</term>
     * <term>RSS Expanded (GS1 DataBar Expanded).</term>
     * <term>34</term>
     * </item>
     * </list>
     *
     * @exception
     * BarcodePrinter_API.ApiException.BarcodePrinterIllegalArgumentException
     * 無效參數或參數在有效範圍外.
     *
     * @exception BarcodePrinter_API.ApiException.BarcodePrinterGeneralException
     * 一般性的錯誤, 位元組陣列操作有錯誤.
     *
     * {@link PPLZOrient}
     * {@link PPLZRSSType}
     */
    public final void printRSS(int x, int y, PPLZOrient orient, PPLZRSSType type, int magni, int height, int segment, byte[] data) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.BarcodeUtil.PrintRSS()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	orient: " + orient + InternalData.szEnter;
            strLog += "	type: " + type + InternalData.szEnter;
            strLog += "	magni: " + magni + InternalData.szEnter;
            strLog += "	height: " + height + InternalData.szEnter;
            strLog += "	segment: " + segment + InternalData.szEnter;
            strLog += "	data: ";
            if (null == data) {
                strLog += InternalData.szNull;
                LogFile.append(strLog);
            } else {
                LogFile.append(strLog);
                LogFile.append(data, 0, data.length);
            }
            strLog = InternalData.szEnter;
            LogFile.append(strLog);
        }
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (magni < 1 || magni > 10) || (height < 0 || height > PPLZ.getMAX_LABEL_WH()) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }
        char cType = (char) type.getValue();
        if ('6' == cType && (segment < 0 || segment > 22 || 1 == segment % 2)) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^BR" + (char) orient.getValue() + "," + cType + "," + magni + ",1," + height + "," + segment; //條碼種類, ",1,"中的1指的是separator height.
        strTemp += "^FD";
        Out.copyToBuffer(strTemp, this.parent.queueBuf);
        Out.copyToBuffer(data, 0, data.length, this.parent.queueBuf);

        strTemp = "^FS\r\n";

        Out.copyToBuffer(strTemp, this.parent.queueBuf);
    }

    /**
     * 此函式用來繪製 Data Matrix 條碼, 且品質水準 (quality level) 只支援 ECC 200.
     *
     * @param x [in]X 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param y [in]Y 軸的座標. 範圍: 0~32000; 單位: 像素.
     * @param orient [in]列印方向定位. 使用 {@link PPLZOrient} 列舉來設定.
     * @param height [in]單個符號元素的高度尺寸. 範圍: 1~9999; 單位: 像素; 0 為條碼標準值.
     * @param columns [in]以幾行資料去編碼. 範圍: 9~49; 0: 意指自動; 在 ECC 200 時此值只支援偶數值.
     * @param rows [in]以幾列資料去編碼. 範圍: 9~49; 0: 意指自動; 在 ECC 200 時此值只支援偶數值.
     * @param data [in]條碼資料. 資料型態: 位元組陣列.
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
     */
    public final void printDataMatrix(int x, int y, PPLZOrient orient, int height, int columns, int rows, byte[] data, int increase) throws BarcodePrinterIllegalArgumentException, BarcodePrinterGeneralException {
        if (true == LogFile.getEnabled()) {
            String strLog;
            strLog = "PPLZ.BarcodeUtil.PrintDataMatrix()" + InternalData.szEnter;
            strLog += "	x: " + x + InternalData.szEnter;
            strLog += "	y: " + y + InternalData.szEnter;
            strLog += "	orient: " + orient + InternalData.szEnter;
            strLog += "	height: " + height + InternalData.szEnter;
            strLog += "	columns: " + columns + InternalData.szEnter;
            strLog += "	rows: " + rows + InternalData.szEnter;
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
        if ((x < 0 || x > PPLZ.getMAX_LABEL_WH()) || (y < 0 || y > PPLZ.getMAX_LABEL_WH()) || (height < 1 || height > 9999) || (null == data || 0 == data.length)) {
            throw new BarcodePrinterIllegalArgumentException();
        }
        if ((1 == columns % 2) || ((columns != 0) && (9 > columns || columns > 49)) || (1 == rows % 2) || ((rows != 0) && (9 > rows || rows > 49))) {
            throw new BarcodePrinterIllegalArgumentException();
        }

        String strTemp;
        strTemp = "^FO" + x + "," + y; //原點.
        strTemp += "^BX" + (char) orient.getValue() + "," + height + ",200," + columns + "," + rows; //條碼種類, ",200,"中的200指的是quality level.
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
}
