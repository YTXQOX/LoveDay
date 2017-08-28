package com.ljstudio.android.loveday.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Time;

/**
 * Created by guoren on 2017/8/28 17:35
 * Usage
 */

public class MyAppWidgetUpdateService extends Service {

    private UpdateThread mUpdateThread;
    private Context mContext;


    @Override
    public void onCreate() {
        // 创建并开启线程UpdateThread
        mUpdateThread = new UpdateThread();
        mUpdateThread.start();

        mContext = this.getApplicationContext();

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mUpdateThread != null) {
            mUpdateThread.interrupt();
        }

        super.onDestroy();
    }

    private class UpdateThread extends Thread {
        @Override
        public void run() {
            super.run();

            Time time = new Time();
            time.setToNow();
            int hour = time.hour;
            int min = time.minute;
            int second = time.second;
            int year = time.year;
            int month = time.month + 1;
            int day = time.monthDay;
        }
    }

}