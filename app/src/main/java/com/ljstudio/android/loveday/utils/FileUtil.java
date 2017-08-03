package com.ljstudio.android.loveday.utils;


import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FileUtil {

    public static final long GB = 1073741824; // 1024 * 1024 * 1024
    public static final long MB = 1048576; // 1024 * 1024
    public static final long KB = 1024;

    //Save Local FilePath
    public static File getSDCardFolderPath(String filePathName) {
        File folderFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "LJSTUDIO" + File.separator
                + "Android" + File.separator
                + "LoveDay" + File.separator
                + filePathName
                + File.separator);
        return folderFile;
    }

    public static String getSDCardFolderPathStr(String filePathName) {
        File folderFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "LJSTUDIO" + File.separator
                + "Android" + File.separator
                + "LoveDay" + File.separator
                + filePathName
                + File.separator);

        return folderFile.getAbsolutePath();
    }

    //get FilePath
    public static String getSDCardFilePath(String filePathName, String fileName) {
        File file = new File(getSDCardFolderPath(filePathName), fileName);
        return file.getAbsolutePath();
    }

    //create File
    public static String createFile2SD(String strFilePathName, String strFileName) throws IOException {
        //创建文件夹
        File filePath = getSDCardFolderPath(strFilePathName);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        // 创建文件
//		String file = filePath.getAbsolutePath() + File.separator + strFileName;
//		File f = new File(file);
        File f = new File(filePath.getAbsolutePath(), strFileName);
        if (!f.exists()) {
            f.createNewFile();
        }

        return f.getAbsolutePath();
    }

    //isExist File
    public static boolean isFileExist(String filePathName, String fileName) {
        File file = new File(getSDCardFolderPath(filePathName), fileName);
        return file.exists();
    }

    //
    public static boolean saveFile2SD(Context context, byte[] fileByte, String strFilePathName, String strFileName) throws IOException {
        boolean b = false;

        // 创建文件夹
        File filePath = getSDCardFolderPath(strFilePathName);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        // 创建文件
        String file = filePath.getAbsolutePath() + File.separator + strFileName;
        File f = new File(file);
        if (!f.exists()) {
            f.createNewFile();
        }

        // 字节输出流  文件写入SD卡
        FileOutputStream fos = new FileOutputStream(f);
        // 字节流
        InputStream inStream = new ByteArrayInputStream(fileByte);
        // 字符流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream, "gb2312")); //防止中文出现乱码  gb2312
        // FileInputStream fis = (FileInputStream)inStream;

        long max = fileByte.length;
        try {
            byte[] buffer = new byte[1024];
            int length = 0;

            while (true) {
                //InputStream 字节流
                int temp = inStream.read(buffer, 0, buffer.length);
                if (temp == -1) {
                    break;
                }
                fos.write(buffer, 0, temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                fos.close();

//				String strDownloadFilePath = getSDCardFile(strFilePathName, strFileName);
//				Toast.makeText(context, "文件已保存至: " + strDownloadFilePath, Toast.LENGTH_LONG).show();

                b = true;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return b;
    } // -end of _method saveFile2SD()_ by LJ.

    /**
     * 获取文件大小
     * @param file
     */
    public static String getFileSize(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            if (length >= GB) {
                return String.format("%.2f GB", length * 1.0 / GB);
            } else if (length >= MB) {
                return String.format("%.2f MB", length * 1.0 / MB);
            } else {
                return String.format("%.2f KB", length * 1.0 / KB);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "unread";
    }

    /**
     * 获取文件大小
     * @param length
     */
    public static String getFileSize(long length) {
        if (length >= GB) {
            return String.format("%.2f GB", length * 1.0 / GB);
        } else if (length >= MB) {
            return String.format("%.2f MB", length * 1.0 / MB);
        } else if (length >= KB) {
            return String.format("%.2f KB", length * 1.0 / KB);
        } else {
            return String.format("%.2f B", length * 1.0);
        }
    }

    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

}




