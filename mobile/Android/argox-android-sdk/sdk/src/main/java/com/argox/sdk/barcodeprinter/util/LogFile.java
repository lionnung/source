package com.argox.sdk.barcodeprinter.util;

//PS:此類別不使用"例外".
import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 此類別提供記錄此類別庫中所有的函式相關的活動, 將其存在指定的檔案中的靜態方法.
 *
 * 只有在 {@link #logPath logPath} 存在, 且{@link #enabled enabled} = 'true', 然後此類別庫中所有函式相關的資訊才會被記錄.
 */
public final class LogFile {

    /**
     * 值為 "{android.os.Environment.getExternalStorageDirectory()}/logger.dat"
     */
    public static final String szLogFile = android.os.Environment.getExternalStorageDirectory() + "logger.dat";

    /**
     * 操作Log檔所存放的路徑，預設為{@link #szLogFile}
     */
    private static String logPath = szLogFile;

    /**
     * 是否啟用輸出Log文字檔，預設為關
     */
    private static boolean enabled = false;

    /**
     * 取得或設定用來記錄檔案名稱的絕對路徑.
     *
     * @return 
     * <p>
     * 記錄檔案的檔名. 資料型態: Unicode; 預設值: "logger.dat", 此為一絕對路徑.</p>
     * <p>
     * 在不修改此檔名的情況下然後呼叫 {@link #create()}, 一般而言,
     * 會在執行檔的同一目錄下建立此檔案.</p>
     *  {@link Create#create}
     */
    public static String getLogPath() {
        return logPath;
    }

    public static void setLogPath(String value) {
        logPath = value;
    }

    /**
     * 取得記錄功能是致能或不致能.
     *
     * @return 'true' 為致能; 'false' 為不致能; 預設值為 'false'.
     */
    public static boolean getEnabled() {
        return enabled;
    }

    /**
     * 設定記錄功能是致能或不致能.
     *
     * @return 'true' 為致能; 'false' 為不致能; 預設值為 'false'.
     */
    public static void setEnabled(boolean value) {
        enabled = value;
    }

    /**
     * 此函式用來判斷記錄檔案是否存在.
     *
     * @return [out]'true' 為存在; 'false' 為不存在. {@link LogPath}
     */
    public static boolean exists() {
        return new File(logPath).exists();
    }

    /**
     * 此函式用來建立記錄檔案, 使其可以寫入記錄.
     *
     * @return [out]'true' 為正確; 'false' 為錯誤. {@link LogPath}
     */
    public static boolean create() {
        boolean ret = true;
        return ret;
    }

    /**
     * 此函式用來刪除記錄檔案.
     *
     * @return [out]'true' 為正確; 'false' 為錯誤.
     */
    public static boolean delete() {
        try {
            return new File(logPath).delete();
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    /**
     * 此函式用來將資料添加在記錄檔案後.
     *
     * @param str [in]要寫到記錄檔案中的資料, 其為 Unicode 的字串.
     * <return>[out]'true' 為正確; 'false' 為錯誤.</return>
     */
    public static boolean append(String str) {
        //Log.d("argox", str);
        if (enabled) {
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
                out.println(str);
                out.close();
            } catch (IOException e) {
                Log.e("argox", null, e);
            }
        }
        return true;
    }

    /**
     * 此函式用來將資料添加在記錄檔案後.
     *
     * @param str 要寫到記錄檔案中的資料, 其為 byte陣列.
     * @param offset byte陣列的起始位置
     * @param size 
     * @return 'true' 為正確; 'false' 為錯誤.
     */
    public static boolean append(byte[] str, int offset, int size) {
        //Log.d("argox", str);
        if (enabled) {
            try {
                byte[] newbytes = new byte[size];
                System.arraycopy(str, offset, newbytes, 0, size);
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
                out.println(newbytes);//若不要換行符號，請將println改為print()
                out.close();
            } catch (IOException e) {
                Log.e("argox", null, e);
            }
        }
        return true;
    }
}
