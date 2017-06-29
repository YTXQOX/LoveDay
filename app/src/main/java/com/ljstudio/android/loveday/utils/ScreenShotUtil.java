package com.ljstudio.android.loveday.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guoren on 2017/6/6 17:00
 * Usage
 */
public class ScreenShotUtil {

    @SuppressLint("SimpleDateFormat")
    public static String screenShotBitmap(Activity activity) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(currentTime);
        String path = null;
        try {
            path = FileUtil.createFile2SD("Screenshot", "Screenshot_" + sdf.format(date) + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                takeScreenShot(activity), "LoveDay", "LoveDay_ScreenShot");

//        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
//                .parse("file://" + Environment.getExternalStorageDirectory())));

        MediaScannerConnection.scanFile(activity,
                new String[]{path},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });

        return ScreenShotUtil.save2SD(takeScreenShot(activity), path);
    }

    @SuppressLint("SimpleDateFormat")
    public static String screenShotBitmap(Activity activity, View view) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(currentTime);
        String path = null;
        try {
            path = FileUtil.createFile2SD("Screenshot", "Screenshot_" + sdf.format(date) + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                takeScreenShot(activity, view), "LoveDay", "LoveDay_ScreenShot");

//        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
//                .parse("file://" + Environment.getExternalStorageDirectory())));

        MediaScannerConnection.scanFile(activity,
                new String[]{path},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });

        return ScreenShotUtil.save2SD(takeScreenShot(activity), path);
    }

    public static Bitmap takeScreenShot(Activity activity) {
        Bitmap bitmap;
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();

        // 获取状态栏的高度
        Rect frame = new Rect();
        // 测量屏幕宽高
        view.getWindowVisibleDisplayFrame(frame);
//        int mStatusHeight = frame.top;
//        Log.i("ScreenShotUtil-->", "状态栏高度-->" + mStatusHeight);

//        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
//        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        // 根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    public static Bitmap takeScreenShot(Activity activity, View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        // 获取状态栏的高度
        Rect frame = new Rect();
        // 测量屏幕宽高
        view.getWindowVisibleDisplayFrame(frame);
//        int mStatusHeight = frame.top;
//        Log.i("ScreenShotUtil-->", "状态栏高度-->" + mStatusHeight);

//        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
//        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        // 根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    // 保存图片到Sd卡中
    private static String save2SD(Bitmap bitmap, String path) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(path);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.flush();
                fos.close();
                return path;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


}