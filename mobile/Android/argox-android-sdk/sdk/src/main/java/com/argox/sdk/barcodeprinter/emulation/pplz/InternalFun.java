package com.argox.sdk.barcodeprinter.emulation.pplz;

import com.argox.sdk.barcodeprinter.util.RefObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public final class InternalFun {

    /**
     * 此函式用來做寬窄比是否在 2.0~3.0 間.
     *
     * @param radio [in]寬窄比的值.
     * @return [out]'true' 為正確; 'false' 為錯誤.
     */
    public static boolean checkWidthHeightRadio(float radio) {
        return true;
//            //雖然可以做限制, 但因為firmware會自動做調整, 所以還是讓它過好了.
//            return (radio < 2.0 || radio > 3.0) ? false : true;
//            
    }

    /**
     * 此函式用來將 ARGB 顏色轉換成 HSV.
     *
     * @param color [in]要轉換的顏色.
     * @param hue [out]轉換成色相.
     * @param saturation [out]轉換成飽和度.
     * @param value [out]轉換成色調.
     * //http://zh.wikipedia.org/zh-tw/HSL%E5%92%8CHSV%E8%89%B2%E5%BD%A9%E7%A9%BA%E9%97%B4
     */
    public static void convertColorToHSV(int color, RefObject<Float> hue, RefObject<Float> saturation, RefObject<Float> value) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));

        if (0 == Color.alpha(color)) {
            min = max = 0xFF;
        }
        if (max == min) {
            hue.value = 0f;
        } else if (max == r && g >= b) {
            hue.value = Float.valueOf(60 * (g - b) / (max - min) + 0);
        } else if (max == r && g < b) {
            hue.value = Float.valueOf(60 * (g - b) / (max - min) + 360);
        } else if (max == g) {
            hue.value = Float.valueOf(60 * (b - r) / (max - min) + 120);
        } else //if (max == b)
        {
            hue.value = Float.valueOf(60 * (r - g) / (max - min) + 240);
        }
        saturation.value = Float.valueOf((max == 0) ? 0 : 1f - (min / max));
        value.value = max / 255f;
    }

    /**
     * 此函式用來將顏色轉換成 HSV 中的 V.
     *
     * @param color [in]要轉換的顏色.
     * @param value [out]轉換成色調.
     */
    public static void convertColorToHSV(int color, RefObject<Float> value) {
        float hue = 0f, saturation = 0f;
        RefObject<Float> tempRef_hue = new RefObject<Float>(hue);
        RefObject<Float> tempRef_saturation = new RefObject<Float>(saturation);
        convertColorToHSV(color, tempRef_hue, tempRef_saturation, value);
        hue = tempRef_hue.value;
        saturation = tempRef_saturation.value;
    }

    /**
     * 此函式用來記算每列的位元組數目.
     *
     * @param width [in]寬度; 單位: 像素.
     * @param bpp [in]bpp 的數值.
     * @param splitSize [in]每列對齊的位元組數目; 單位: 位元組.
     * @return [out]每列的位元組數目.
     */
    public static int linebyte(int width, int bpp, int splitSize) {
        int bitcount = width * bpp;
        return ((bitcount + (8 * splitSize) - 1) / (8 * splitSize));
    }

    /**
     * 此函式用來讀取一個 Bitmap 物件中的圖形資料, 並將資料轉成單色資料.
     *
     * @param bitmap [in]為一有效的 Bitmap 物件.
     * @param width [out]回傳圖形的寬度.
     * @param height [out]回傳圖形的高度.
     * @param monoData [out]回傳圖形的單色圖形資料, 其資料的原點為圖的左上, 每行以一個位元組數目對齊.
     * @return [out]'true' 為正確; 'false' 為錯誤.
     */
    public static boolean convertColorToMono(Bitmap bitmap, RefObject<Integer> width, RefObject<Integer> height, RefObject<byte[]> monoData) {
        try {
            resize(monoData, linebyte(bitmap.getWidth(), 1, 1) * bitmap.getHeight());
        } catch (java.lang.Exception e) {
            width.value = height.value = 0;
            return false;
        }

        // convert to 1bpp bmp.
        int x, y;
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        int color;
        int index;
        int line = 0;
        int step = (w >> 3) + ((w % 8 == 0) ? 0 : 1);
        int[] pixels = new int[w];
        float[] hsv = new float[3];

        for (y = 0; y < h; ++y) {
            bitmap.getPixels(pixels, 0, w, 0, y, w, 1);
            for (x = 0; x < w; ++x) {
                color = pixels[x];
                Color.colorToHSV(color, hsv);
                if (hsv[2] < 0.5) {
                    index = line + (x >> 3);
                    monoData.value[index] |= (byte) (0x80 >> (x & 7));
                }
            }
            line += step;
        }
        width.value = bitmap.getWidth();
        height.value = bitmap.getHeight();
        return true;
    }

    /**
     * 此函式用來讀取一個 Text Bitmap 物件中的圖形資料, 並將資料轉成單色資料.
     *
     * @param bitmap [in]為一有效的 Bitmap 物件.
     * @param width [out]回傳圖形的寬度.
     * @param height [out]回傳圖形的高度.
     * @param monoData [out]回傳圖形的單色圖形資料, 其資料的原點為圖的左上, 每行以一個位元組數目對齊.
     * @return [out]'true' 為正確; 'false' 為錯誤.
     */
    public static boolean convertTextColorToMono(Bitmap bitmap, RefObject<Integer> width, RefObject<Integer> height, RefObject<byte[]> monoData) {
        try {
            resize(monoData, linebyte(bitmap.getWidth(), 1, 1) * bitmap.getHeight());
        } catch (java.lang.Exception e) {
            width.value = height.value = 0;
            return false;
        }

        // convert to 1bpp bmp.
        int x, y;
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        int color;
        int index;
        int line = 0;
        int step = (w >> 3) + ((w % 8 == 0) ? 0 : 1);
        int[] pixels = new int[w];

        for (y = 0; y < h; ++y) {
            bitmap.getPixels(pixels, 0, w, 0, y, w, 1);
            for (x = 0; x < w; ++x) {
                color = pixels[x];
                if ((color & 0x80) == 0x80) {
                    index = line + (x >> 3);
                    monoData.value[index] |= (byte) (0x80 >> (x & 7));
                }
            }
            line += step;
        }
        width.value = bitmap.getWidth();
        height.value = bitmap.getHeight();
        return true;
    }

    /**
     * 此函式用來讀取一個 Bitmap 物件中的圖形資料, 並將其旋轉.
     *
     * @param bitmap [in]為一有效的 Bitmap 物件.
     * @param rotation [in]旋轉角度. 範圍: 0, 90, 180, 270.
     * @return [out]Bitmap.
     */
    public static Bitmap rotateToDegrees(Bitmap bitmap, int rotation) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(rotation);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }

    /**
     *
     * @param f
     * @return
     */
    public static Bitmap loadBitmap(File f) {
        if (f.exists()) {
            try {
                return BitmapFactory.decodeStream(new FileInputStream(f));
            } catch (FileNotFoundException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 此函式用來開啟一個圖形檔案並讀取出資料, 並將資料轉成單色圖形資料.
     *
     * @param name [in]圖形檔名. 資料型態: Unicode, 可為絕對 (最好) 路徑或相對路徑兩者其一. 支援的圖形格式: BMP,
     * GIF, EXIG, JPG, PNG 及 TIFF.
     * @param width [out]回傳圖形的寬度.
     * @param height [out]回傳圖形的高度.
     * @param monoData [out]回傳圖形的單色圖形資料, 其資料的原點為圖的左上, 每行以一個位元組數目對齊.
     * @return [out]'true' 為正確; 'false' 為錯誤.
     */
    public static boolean convertColorToMono(String name, RefObject<Integer> width, RefObject<Integer> height, RefObject<byte[]> monoData) {
        try {
            boolean ret;
            Bitmap bitmap = loadBitmap(new File(name));
            ret = convertColorToMono(bitmap, width, height, monoData);
            bitmap.recycle();
            return ret;
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    /**
     * 此函式用來判斷是否為物件的名稱.
     *
     * @param name [in]物件名稱. 範圍: 1~8 個英文字母或數字 (alphanumeric) 字元.
     * @return [out]'true' 為正確; 'false' 為錯誤.
     */
    public static boolean isObjectName(String name) {
        if (name == null || name.length() == 0 || name.length() > 8) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isLetterOrDigit(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 此函式用來判斷字串是否為數字字串.
     *
     * @param str [in]要判斷的字串, 其字串為 Unicode 字元.
     * @return [out]'true' 為正確; 'false' 為錯誤.
     */
    public static boolean isDigit(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 此函式用來將字串反轉.
     *
     * @param str [in]要字串反轉的原始資料.
     * @return [out]反轉後的字串.
     */
    public static String reverse(String str) {
        int len = str.length();
        String strTemp = "";
        while (0 != len--) {
            strTemp += str.charAt(len);
        }
        return strTemp;
    }

    /**
     * 此函式用來當要使用增減函式時, 將資料字串轉成 ^SF 中的字串.
     *
     * @param data [in]原始的字串資料.
     * @param increase [in]增量或減量. 若有一個 "序列數值" 要列印時, 則條碼資訊會依此序列數值的改變而改變資料, 其數字以
     * 0~9 循環, 字母以 A~Z 或 a~z 循環.
     * @param maskStr [in]將原始的字串, 轉換成只有 'a', 'A', 'D', '%' 字元的字串.
     * @param incStr [in]將 increase 變數, 依 maskStr 字串為基本, 轉換成以數字 0~9, 字母 A~Z 或
     * a~z, 或 %.
     */
    public static void convertSFCommand(byte[] data, int increase, RefObject<String> maskStr, RefObject<String> incStr) {
        int i;
        maskStr.value = "";
        incStr.value = "";
        for (i = 0; i < data.length; ++i) {
            char ch = (char) data[i];
            if (Character.isDigit(ch)) {
                ch = 'D';
            } else if (Character.isUpperCase(ch)) {
                ch = 'A';
            } else if (Character.isLowerCase(ch)) {
                ch = 'a';
            } else {
                ch = '%';
            }
            maskStr.value += ch;
        }

        i = maskStr.value.length();
        int addvalue = Math.abs(increase);
        while ((0 != i--) && (0 != addvalue)) {
            char ch = maskStr.value.charAt(i);
            switch (ch) {
                case 'D':
                    incStr.value += (addvalue % 10);
                    addvalue /= 10;
                    break;
                case 'A':
                    incStr.value += (char) ('A' + addvalue % 26);
                    addvalue /= 26;
                    break;
                case 'a':
                    incStr.value += (char) ('a' + addvalue % 26);
                    addvalue /= 26;
                    break;
                default:
                    incStr.value += "%";
                    break;
            }
            incStr.value = reverse(incStr.value);
        }
    }
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * 此函式用來將資料轉成 16 進制的資料.
     *
     * @param data [in]要轉換的資料.
     * @param hexData [out]回傳轉換成 16 進制的資料.
     * @return [out]'true' 為正確; 'false' 為錯誤.
     */
    public static boolean convertDataToHex(byte[] data, RefObject<byte[]> hexData) {
        //int i, j;
        try {
            byte[] hexBytes = new byte[data.length * 2];
            for (int j = 0; j < data.length; j++) {
                int v = data[j] & 0xFF;
                hexBytes[j * 2] = (byte) hexArray[v >>> 4];
                hexBytes[j * 2 + 1] = (byte) hexArray[v & 0x0F];
            }
            hexData.value = hexBytes;
            return true;
        } catch (java.lang.Exception e) {
            Log.e("argox", null, e);
            return false;
        }
    }

    private static void resize(RefObject<byte[]> hexData, int size) {
        hexData.value = new byte[size];
    }
}
