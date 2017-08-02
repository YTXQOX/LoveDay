package com.ljstudio.android.loveday;

import android.app.Application;
import android.content.Context;

import com.ljstudio.android.loveday.greendao.DaoMaster;
import com.ljstudio.android.loveday.greendao.DaoSession;

/**
 * Created by guoren on 2017/5/3 16:24
 * Usage
 */

public class MyApplication extends Application {

    private static final String DB_NAME = "loveday-db";
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;


    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
