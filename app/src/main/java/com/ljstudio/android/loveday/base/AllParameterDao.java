package com.ljstudio.android.loveday.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ljstudio.android.loveday.utils.SystemOutUtil;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by tianyani on 16-1-28.
 */
public class AllParameterDao {
    private static AllParameterDao testPangDao;

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public AllParameterDao(Context context) {
        this.dataBaseHelper = new DataBaseHelper(context);
    }

    public static AllParameterDao getInstance(Context context) {
        synchronized (AllParameterDao.class) {
            if (null == testPangDao) {
                testPangDao = new AllParameterDao(context);
            }
        }
        return testPangDao;
    }

    /**
     * 表是否存在
     */
    public boolean tableIsExist(String tableName) {
        db = this.dataBaseHelper.getReadableDatabase();
        r.lock();
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {

            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            SystemOutUtil.sysOut("RecommendationCategoryDao-->tableIsExist-->" + e.toString());
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            r.unlock();
            db.close();
        }

        return result;
    }

    /**
     * 添加数据
     */
    public void insertAllData(List<String> listData) {
        try {
            this.db = this.dataBaseHelper.getWritableDatabase();
            w.lock();
            for (String itemSQL : listData) {
                db.execSQL(itemSQL);
                SystemOutUtil.sysOut("insertAllData-->id-->" + itemSQL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            SystemOutUtil.sysOut("insertAllData-->" + listData);
        } finally {
            w.unlock();
            db.close();
        }
    }

    /**
     * 添加数据
     */
    public void insertData(String data) {
        try {
            this.db = this.dataBaseHelper.getWritableDatabase();
            w.lock();
            db.execSQL(data);
            SystemOutUtil.sysOut("insertAllData-->id-->" + data);
        } catch (Exception e) {
            e.printStackTrace();
            SystemOutUtil.sysOut("insertAllData-->" + data);
        } finally {
            w.unlock();
            db.close();
        }
    }
}
