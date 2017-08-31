package com.ljstudio.android.loveday.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.greendao.DaysDataDao;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by guoren on 2017/8/25 15:34
 * Usage
 */

public class MyAppWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_UPDATE_ALL = "com.ljstudio.android.loveday.UPDATE_ALL";
    private static final String TAG = "MyAppWidgetProvider";
    private final Intent MY_APP_SERVICE_INTENT = new Intent("android.appwidget.action.MY_APP_WIDGET_SERVICE");
    private static Set idsSet = new HashSet();
    private static final int BUTTON_SHOW = 1;

    private boolean DEBUG = false;
    private List<DaysData> listDays = new ArrayList<>();
    private Context mContext;


    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_UPDATE_ALL.equals(action)) {
//            ToastUtil.toastShortCenter(context, "Thread更新 Widget");

            initData();
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
        } else if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            Uri data = intent.getData();
            int buttonId = Integer.parseInt(data.getSchemeSpecificPart());
            if (buttonId == BUTTON_SHOW) {
                ToastUtil.toastShortCenter(context, "好的 爱你");

                initData();
                updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);

//                Intent i = new Intent(context, DetailActivity.class);
//                context.startActivity(i);
            }
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(Integer.valueOf(appWidgetId));
        }
        prtSet();

//        initData();

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当 widget 被删除时被触发
     *
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
        prtSet();

        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当第1个 widget 的实例被创建时触发
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        mContext = context;
        initData();

        Intent mIntent = new Intent();
        mIntent.setAction("android.appwidget.action.MY_APP_WIDGET_SERVICE");
        mIntent.setPackage(mContext.getPackageName());
        context.startService(mIntent);

        super.onEnabled(context);
    }

    /**
     * 当最后1个 widget 的实例被删除时触发
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        mContext = context;

        Intent mIntent = new Intent();
        mIntent.setAction("android.appwidget.action.MY_APP_WIDGET_SERVICE");
        mIntent.setPackage(mContext.getPackageName());
        context.stopService(mIntent);

        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    private void initData() {
        listDays.clear();
        listDays = readAll4DB();
        if (listDays == null || listDays.size() == 0) {
            testData();
            listDays = readAll4DB();
        }
    }

    private List<DaysData> readAll4DB() {
        final DaysDataDao dao = MyApplication.getDaoSession(mContext).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();

        return list;
    }

    private void testData() {
        DaysData data1 = new DaysData();
        data1.setTitle("ttt.XY一起美丽时光");
        data1.setDate("2015-11-26");
        data1.setDays("1");
        data1.setUnit("天");
        data1.setIsTop(true);
        writeOne2DB(data1);
    }

    private void writeOne2DB(final DaysData data) {
        try {
            MyApplication.getDaoSession(mContext).runInTx(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getDaoSession(mContext).getDaysDataDao().insertOrReplace(data);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, Set set) {
        Log.i(TAG, "updateAllAppWidgets()-->size-->" + set.size());

        // widget 的id
        int appID;
        // 迭代器，用于遍历所有保存的widget的id
        Iterator it = set.iterator();

        while (it.hasNext()) {
            appID = ((Integer) it.next()).intValue();

//            int index = (new java.util.Random().nextInt(listDays.size()));
//            if (DEBUG) Log.i(TAG, "onUpdate(): index=" + index);

            DaysData data;
            List<DaysData> list = readTop4DB(context);
            if (list == null || 0 == list.size()) {
                data = listDays.get(0);
            } else {
                data =  list.get(0);
            }

            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_my_app_widget);
            Date date = DateFormatUtil.convertStr2Date(data.getDate(), DateFormatUtil.sdfDate1);
            if (1 == DateUtil.compareDate(date, new Date())) {
                remoteView.setTextColor(R.id.id_widget_days, context.getResources().getColor(R.color.colorBlue));
                remoteView.setTextColor(R.id.id_widget_date, context.getResources().getColor(R.color.colorBlue));
            } else {
                remoteView.setTextColor(R.id.id_widget_days, context.getResources().getColor(R.color.colorAccent));
                remoteView.setTextColor(R.id.id_widget_date, context.getResources().getColor(R.color.colorAccent));
            }

            int days = DateUtil.betweenDays(date, new Date());
            remoteView.setTextViewText(R.id.id_widget_title, data.getTitle());
            remoteView.setTextViewText(R.id.id_widget_days, String.valueOf(days) + "天");
            remoteView.setTextViewText(R.id.id_widget_date, data.getDate());

            remoteView.setOnClickPendingIntent(R.id.id_widget_title, getPendingIntent(context, BUTTON_SHOW));
            appWidgetManager.updateAppWidget(appID, remoteView);
        }
    }

    private List<DaysData> readTop4DB(Context context) {
        final DaysDataDao dao = MyApplication.getDaoSession(context).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .where(DaysDataDao.Properties.IsTop.eq(true))
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();

        return list;
    }

    private PendingIntent getPendingIntent(Context context, int buttonId) {
        Intent intent = new Intent();
        intent.setClass(context, MyAppWidgetProvider.class);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setData(Uri.parse("custom:" + buttonId));
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }

    private void prtSet() {
        if (DEBUG) {
            int index = 0;
            int size = idsSet.size();
            Iterator it = idsSet.iterator();
            Log.i(TAG, "total-->" + size);
            while (it.hasNext()) {
                Log.i(TAG, index + "-->" + ((Integer) it.next()).intValue());
            }
        }
    }
}
