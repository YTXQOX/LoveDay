package com.ljstudio.android.loveday.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtil {

    private static Toast mToast;


    public static void toastShortCenter(Context context, String string) {
//		Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();

        if (mToast == null) {
            mToast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(string);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void toastLongCenter(Context context, String string) {
//		Toast toast = Toast.makeText(context, string, Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();

        if (mToast == null) {
            mToast = Toast.makeText(context, string, Toast.LENGTH_LONG);
        } else {
            mToast.setText(string);
            mToast.setDuration(Toast.LENGTH_LONG);
        }

        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void toastShort(Context context, String string) {
//		Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
//		toast.show();

        if (mToast == null) {
            mToast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(string);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }

        mToast.show();
    }

    public static void toastLong(Context context, String string) {
//		Toast toast = Toast.makeText(context, string, Toast.LENGTH_LONG);
//		toast.show();

        if (mToast == null) {
            mToast = Toast.makeText(context, string, Toast.LENGTH_LONG);
        } else {
            mToast.setText(string);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }

        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
    private static Toast Toast2;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context context, String s) {
        if (Toast2 == null) {
            Toast2 = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            Toast2.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    Toast2.show();
                }
            } else {
                oldMsg = s;
                Toast2.setText(s);
                Toast2.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
