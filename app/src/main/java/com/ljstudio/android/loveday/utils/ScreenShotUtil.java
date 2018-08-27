package com.ljstudio.android.loveday.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

//        MediaStore.Images.Media.insertImage(activity.getContentResolver(),
//                takeScreenShot(activity), "LoveDay", "LoveDay_ScreenShot");

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

    public static String screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
        Date date = new Date(currentTime);
        String path = null;
        try {
            path = FileUtil.createFile2SD("Screenshot", "Screenshot_" + sdf.format(date) + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
//        decorView.setWillNotCacheDrawing(false);
        Bitmap bmp = decorView.getDrawingCache();

//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        Bitmap bmp = view.getDrawingCache();

        if (bmp == null)
            return null;

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap bitmap;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);

            bitmap = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        } else {
            bitmap = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }

        decorView.setDrawingCacheEnabled(false);
        decorView.destroyDrawingCache();
//        view.destroyDrawingCache();

        return ScreenShotUtil.save2SD(bitmap, path);
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

    public static Bitmap getViewBitmap(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(),
                    View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                    v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return b;
    }

    // 保存图片到Sd卡中
    public static String save2SD(Bitmap bitmap, String path) {
        String strPath = "";
        if (TextUtils.isEmpty(path)) {
            long currentTime = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
            Date date = new Date(currentTime);
            try {
                strPath = FileUtil.createFile2SD("Screenshot", "Screenshot_" + sdf.format(date) + ".png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            strPath = path;
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(strPath));
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.flush();
                fos.close();
                return strPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


}