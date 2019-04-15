package com.ljstudio.android.loveday.base;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "loveday-db"; // 数据库名
    private static final int DATABASE_VERSION = 2; // 数据库版本

    /**
     * 表
     */
    public static final String TABLE_ALL_PARAMETER = "ALL_PARAMETER";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper(Context context, String name, CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();

        db.execSQL("CREATE TABLE if not exists " + TABLE_ALL_PARAMETER + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PARAM_ID VARCHAR(20), PARAM_NAME VARCHAR(20), VALUE_TYPE VARCHAR(20), LEN INTEGER(10), DATE_TIME INTEGER(20), VALUE INTEGER(20))");

        db.setTransactionSuccessful();
        db.endTransaction();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
