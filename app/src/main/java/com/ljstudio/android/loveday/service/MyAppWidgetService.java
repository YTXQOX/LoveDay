package com.ljstudio.android.loveday.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ljstudio.android.loveday.receiver.MyAppWidgetProvider;


/**
 * Created by guoren on 2017/8/25 15:53
 * Usage
 */

public class MyAppWidgetService extends Service {

    private static final int UPDATE_TIME = 1000 * 60 * 30;

    private UpdateThread mUpdateThread;
    private Context mContext;
    private int count = 0;


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
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
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

            try {
                count = 0;
                while (true) {
                    count++;

//                    Time time = new Time();
//                    time.setToNow();
//                    int h = time.hour;
//                    int m = time.minute;
//                    int s = time.second;

//                    if (0 == h) {
                        Intent updateIntent = new Intent(MyAppWidgetProvider.ACTION_UPDATE_ALL);
                        mContext.sendBroadcast(updateIntent);
//                    }

                    Thread.sleep(UPDATE_TIME);
                }
            } catch (InterruptedException e) {
                // 将 InterruptedException 定义在while循环之外，意味着抛出 InterruptedException 异常时，终止线程。
                e.printStackTrace();
            }
        }
    }

}
