package com.ljstudio.android.loveday.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.adapter.DaysAdapter;
import com.ljstudio.android.loveday.adapter.QuickDaysAdapter;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.entity.ExcelDaysData;
import com.ljstudio.android.loveday.eventbus.MessageEvent;
import com.ljstudio.android.loveday.greendao.DaysDataDao;
import com.ljstudio.android.loveday.test.PPangActivity;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;
import com.ljstudio.android.loveday.utils.FileUtil;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.ljstudio.android.loveday.utils.VersionUtil;
import com.ljstudio.android.loveday.views.LabelView;
import com.ljstudio.android.loveday.views.excel.ExcelManager;
import com.ljstudio.android.loveday.views.fonts.FontsManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tapadoo.alerter.Alerter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nekocode.triangulation.TriangulationDrawable;
import es.dmoral.toasty.Toasty;

import static com.ljstudio.android.loveday.utils.FileUtil.getSDCardFolderPath;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.id_main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_main_top_layout)
    RelativeLayout layoutTop;
    @BindView(R.id.id_main_top_hot)
    LabelView labelView;
    @BindView(R.id.id_main_top_title)
    TextView tvTopTitle;
    @BindView(R.id.id_main_top_date)
    TextView tvTopDate;
    @BindView(R.id.id_main_top_days)
    TextView tvTopDays;
    @BindView(R.id.id_main_top_days_unit_layout)
    LinearLayout llDaysAndUnit;
//    MultiScrollNumber tvTopDays;

    @BindView(R.id.id_main_top_unit)
    TextView tvTopUnit;
    @BindView(R.id.id_main_recycler_view)
    RecyclerView recyclerView;

    private DaysAdapter daysAdapter;
    private QuickDaysAdapter quickDaysAdapter;
    private List<DaysData> listDays = new ArrayList<>();

    private TriangulationDrawable triangulationDrawable;
    private boolean isColorfulBg;

    private Handler mHandler;

    private SingleClick singleClick;
    private DoubleClick doubleClick;
    private TripleClick tripleClick;

    private long mClickTime1 = 0;
    private long mClickTime2 = 0;
    private long mClickTime3 = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        mHandler = new Handler();

        isColorfulBg = PreferencesUtil.getPrefBoolean(MainActivity.this, Constant.COLORFUL_BG, false);
        refreshUI(isColorfulBg);

//        labelView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClickCopy(View view) {
//                showEasterEgg();

//                Intent intent = new Intent(MainActivity.this, TestActivity.class);
//                startActivity(intent);
//            }
//        });

        labelView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onClick(event, "easter_egg", 1000);
                return false;
            }
        });

        llDaysAndUnit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onClick(event, "apps", 1001);
                return false;
            }
        });

        tvTopTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PPangActivity.class);
                startActivity(intent);

//                String path = WaterMarkUtil.getImageFilePath("marry");
//                SystemOutUtil.sysOut("path==>" + path);
//                WaterMarkUtil.addWatermarkBitmap(MainActivity.this, WaterMarkUtil.getLocalBitmap(path),"仅限柒牌婚假专用", 0 ,0);
            }
        });

        String str = "";
        str.replaceAll("\\\"", "\"");

        /**
         * Refactoring RxJava(RxAndroid)
         */
        if (checkIsFirst()) {
            File filePath = getSDCardFolderPath("Export");
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            File file = new File(filePath.getAbsolutePath(), "LoveDay.xlsx");
            if (file.exists()) {
                SystemOutUtil.sysOut("file.getAbsolutePath()-->" + file.getAbsolutePath());

                onImport(file.getAbsolutePath());
            } else {
                testData();
            }

//            Observable<List<DaysData>> local = Observable.create(new ObservableOnSubscribe<List<DaysData>>() {
//                @Override
//                public void subscribe(ObservableEmitter<List<DaysData>> e) throws Exception {
//                    File filePath = getSDCardFolderPath("Export");
//                    if (!filePath.exists()) {
//                        filePath.mkdirs();
//                    }
//                    File file = new File(filePath.getAbsolutePath(), "LoveDay.xlsx");
//                    if (file.exists()) {
//                        SystemOutUtil.sysOut("file.getAbsolutePath()-->" + file.getAbsolutePath());
//
//                        onImport(file.getAbsolutePath());
//
//                        e.onNext(listDays);
//                    } else {
//                        e.onComplete();
//                    }
//                }
//            });

//            Observable<List<DaysData>> origin = Observable.create(new ObservableOnSubscribe<List<DaysData>>() {
//                @Override
//                public void subscribe(ObservableEmitter<List<DaysData>> e) throws Exception {
//                    testData();
//                }
//            });

//            Observable.concat(local, origin)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<List<DaysData>>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(List<DaysData> daysDatas) {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });

//            Observable.concat(local, origin)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<List<DaysData>>() {
//                        @Override
//                        public void accept(@io.reactivex.annotations.NonNull List<DaysData> daysDatas) throws Exception {
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
//                        }
//                    });
        } else {
            resetData();
        }

        FontsManager.initFormAssets(this, "fonts/gtw.ttf");
        FontsManager.changeFonts(this);
    }

    private void onClick(MotionEvent event, String name, int type) {
        mClickTime3 = mClickTime2;
        mClickTime2 = mClickTime1;
        mClickTime1 = event.getEventTime();
        if ((mClickTime1 - mClickTime3) < 600) {
            // 三击 先取消双击单击的post
            if (doubleClick != null)
                mHandler.removeCallbacks(doubleClick);

            if (singleClick != null)
                mHandler.removeCallbacks(singleClick);

            tripleClick = new TripleClick(name);
            mHandler.post(tripleClick);
            // 防止连按四下多次执行三击操作
            mClickTime3 = 0;
        } else if ((mClickTime1 - mClickTime2) < 300) {
            // 双击 先取消单击的post
            if (singleClick != null)
                mHandler.removeCallbacks(singleClick);

            if (1001 == type) {
                doubleClick = new DoubleClick(name, type);
                mHandler.postDelayed(doubleClick, 300);
            }
        } else {
            // 单击
//            singleClick = new SingleClick(name);
//            mHandler.postDelayed(singleClick, 300);
        }
    }

    /**
     * 单击
     */
    class SingleClick implements Runnable {
        String str;

        SingleClick(String str) {
            this.str = str;
        }

        @Override
        public void run() {

        }
    }

    /**
     * 双击
     */
    class DoubleClick implements Runnable {
        String str;
        int type;

        DoubleClick(String name, int type) {
            this.str = str;
            this.type = type;
        }

        @Override
        public void run() {
            if (1001 == type) {
                go2Apps();
            }
        }
    }

    /**
     * 三击
     */
    class TripleClick implements Runnable {
        String str;

        TripleClick(String str) {
            this.str = str;
        }

        @Override
        public void run() {
            showEasterEgg();
        }
    }

    private void refreshUI(boolean isColorful) {
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor(0, RandomColor.SaturationType.RANDOM, RandomColor.Luminosity.RANDOM);

        if (isColorfulBg) {
            setStatusBar(color);
            toolbar.setBackgroundColor(color);

//            triangulationDrawable = new TriangulationDrawable(0xbbfbfbfb);
            triangulationDrawable = new TriangulationDrawable(color);
            findViewById(android.R.id.content).setBackground(triangulationDrawable);

            tvTopTitle.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar(ContextCompat.getColor(this, R.color.colorPrimary));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

            findViewById(android.R.id.content).setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));

            tvTopTitle.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
        }

//        quickDaysAdapter.notifyDataSetChanged();
    }

    private void initListData() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(MainActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager1);
//        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this, VERTICAL_LIST, R.color.colorGrayLight));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

//        daysAdapter = new DaysAdapter(MainActivity.this, listDays);
//        recyclerView.setAdapter(daysAdapter);

        quickDaysAdapter = new QuickDaysAdapter(R.layout.layout_item_days, listDays);
        recyclerView.setAdapter(quickDaysAdapter);

        quickDaysAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                Intent intent = new Intent(MainActivity.this, SwipeDetailActivity.class);
                intent.putExtra(DetailActivity.ID, listDays.get(position).getId());
                intent.putExtra(DetailActivity.POSITION, position);
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
//                        (MainActivity.this, view,"detail_title")
//                        .toBundle());
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
                        (MainActivity.this, Pair.create(view, "detail_title"), Pair.create(view, "detail_date"), Pair.create(view, "detail_days"))
                        .toBundle());
//                (MainActivity.this, view, "detail_title")
//                (MainActivity.this, Pair.create(view, "detail_date"), Pair.create(view, "detail_days"))
            }
        });
    }

    private void initTopData() {
        DaysData data;
        List<DaysData> list = readTop4DB();
        if (list == null || 0 == list.size()) {
            data = listDays.get(0);
        } else {
            data = list.get(0);
        }

        tvTopTitle.setText(data.getTitle());

        Date date = DateFormatUtil.convertStr2Date(data.getDate(), DateFormatUtil.sdfDate1);
        if (1 == DateUtil.compareDate(date, new Date())) {
            tvTopDate.setText("目标日：" + data.getDate());

            tvTopDays.setTextColor(getResources().getColor(R.color.colorBlue));
//            tvTopDays.setTextColors(new int[]{R.color.colorBlue});

            tvTopUnit.setTextColor(getResources().getColor(R.color.colorBlue));
        } else {
            tvTopDate.setText("起始日：" + data.getDate());

            tvTopDays.setTextColor(getResources().getColor(R.color.colorAccent));
//            tvTopDays.setTextColors(new int[]{R.color.colorAccent});

            tvTopUnit.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        int days = DateUtil.betweenDays(date, new Date());
        tvTopDays.setText(String.valueOf(days));
//        tvTopDays.setNumber(days);

        tvTopUnit.setText(data.getUnit());
    }

    private void writeOne2DB(final DaysData data) {
        try {
            MyApplication.getDaoSession(this).runInTx(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getDaoSession(MainActivity.this).getDaysDataDao().insertOrReplace(data);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeAll2DB(final List<DaysData> data) {
        try {
            MyApplication.getDaoSession(this).runInTx(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    for (DaysData entity : data) {
                        MyApplication.getDaoSession(MainActivity.this).getDaysDataDao().insertOrReplace(entity);
                        size = size + 1;

                        if (size >= data.size()) {
                            ToastUtil.showToast(MainActivity.this, "数据存储成功");
                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<DaysData> readTop4DB() {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .where(DaysDataDao.Properties.IsTop.eq(true))
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();

        return list;
    }

    private void readOne4DB(Long id) {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .where(DaysDataDao.Properties.Id.eq(id))
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();
    }

    private void deleteAll4DB() {
        try {
            MyApplication.getDaoSession(this).runInTx(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getDaoSession(MainActivity.this).getDaysDataDao().deleteAll();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<DaysData> readAll4DB() {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
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

        setIsInstall();

        resetData();
    }

    private void resetData() {
        listDays.clear();
        listDays = readAll4DB();

        if (listDays == null || listDays.size() == 0) {
            testData();

            Toasty.info(MainActivity.this, "无数据，已为您恢复默认数据").show();
        } else {
            List<DaysData> listDaysPrevious = new ArrayList<>();
            List<DaysData> listDaysFuture = new ArrayList<>();
            for (DaysData daysData : listDays) {
                Date date = DateFormatUtil.convertStr2Date(daysData.getDate(), DateFormatUtil.sdfDate1);
                if (1 == DateUtil.compareDate(date, new Date())) {
                    listDaysFuture.add(daysData);
                    Collections.sort(listDaysFuture, compFuture);
                } else {
                    listDaysPrevious.add(daysData);
                    Collections.sort(listDaysPrevious, compPrevious);
                }
            }
            listDays.clear();
            listDays.addAll(listDaysFuture);
            listDays.addAll(listDaysPrevious);
        }

        setIsInstall();

//        SystemOutUtil.sysOut("listDays.size()-->" + listDays.size());
        initTopData();
        initListData();
    }

    Comparator compPrevious = new Comparator() {
        public int compare(Object o1, Object o2) {
            DaysData data1 = (DaysData) o1;
            DaysData data2 = (DaysData) o2;

            Date date1 = DateFormatUtil.convertStr2Date(data1.getDate(), DateFormatUtil.sdfDate1);
            Date date2 = DateFormatUtil.convertStr2Date(data2.getDate(), DateFormatUtil.sdfDate1);

            return DateUtil.compareDate(date2, date1);
        }
    };

    Comparator compFuture = new Comparator() {
        public int compare(Object o1, Object o2) {
            DaysData data1 = (DaysData) o1;
            DaysData data2 = (DaysData) o2;

            Date date1 = DateFormatUtil.convertStr2Date(data1.getDate(), DateFormatUtil.sdfDate1);
            Date date2 = DateFormatUtil.convertStr2Date(data2.getDate(), DateFormatUtil.sdfDate1);

            return DateUtil.compareDate(date1, date2);
        }
    };

    private void go2Apps() {
        Intent intent = new Intent(MainActivity.this, AppsActivity.class);
        startActivity(intent);
    }

    private void showEasterEgg() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(MainActivity.this);
        builder.title("我是彩蛋");
        builder.content(getString(R.string.easter_egg_content) + VersionUtil.getVersionName(MainActivity.this));
        builder.positiveText("确定");
        builder.negativeText("取消");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });

        builder.build().show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MainActivity.this.getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.id_action_add) {
                Intent intent = new Intent();
                intent.putExtra(EditActivity.EDIT_TYPE, 200);
                intent.setClass(MainActivity.this, EditActivity.class);
                startActivity(intent);
            } else if (id == R.id.id_action_output) {
                onExport();
            } else if (id == R.id.id_action_recovery) {
                File filePath = getSDCardFolderPath("Export");
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                final File file = new File(filePath.getAbsolutePath(), "LoveDay.xlsx");
                if (file.exists()) {
                    String strDateTime = DateFormatUtil.convertTimestamp2Str(file.lastModified(), DateFormatUtil.sdfDateTime20);
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(MainActivity.this);
                    builder.title("温馨提示");
                    String content = "恢复 " + "<font color=#E61A6B>" + strDateTime + "</font>" + " 数据后，将清除现有的所有数据，替换为所恢复的数据";
                    builder.content(Html.fromHtml(content));
                    builder.positiveText("恢复数据");
                    builder.negativeText("取消");
                    builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            onImport(file.getAbsolutePath());
                        }
                    });
                    builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    });

                    builder.build().show();
                } else {
                    Toasty.error(MainActivity.this, "没有备份记录可恢复").show();
                }
            } else if (id == R.id.id_action_setting) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }

            return true;
        }
    };

    private void onImport(String filePath) {
        try {
            long t1 = System.currentTimeMillis();

//            AssetManager asset = getAssets();
//            InputStream excelStream = asset.open("users.xls");

            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);

            ExcelManager excelManager = new ExcelManager();
            List<ExcelDaysData> list = excelManager.fromExcel(fis, ExcelDaysData.class);

            long t2 = System.currentTimeMillis();
            double time = (t2 - t1) / 1000.0D;
//            Toast.makeText(this, "读到Entity个数:" + list.size() + "\n用时:" + time + "秒", Toast.LENGTH_SHORT).show();

            listDays.clear();
            for (ExcelDaysData excelDaysData1 : list) {
                DaysData daysData1 = new DaysData();
                daysData1.setTitle(excelDaysData1.getTitle());
                daysData1.setDate(excelDaysData1.getDate());
                daysData1.setDays(excelDaysData1.getDays());
                daysData1.setUnit(excelDaysData1.getUnit());
                daysData1.setIsTop(Boolean.valueOf(excelDaysData1.getIsTop()));

                listDays.add(daysData1);
            }

            deleteAll4DB();
            writeAll2DB(listDays);
            resetData();
        } catch (Exception e) {
            Toast.makeText(this, "读取备份异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void onExport() {
        File file = null;
        try {
            long t1 = System.currentTimeMillis();

            List<ExcelDaysData> list = new ArrayList<>();
            for (DaysData daysData1 : listDays) {
                ExcelDaysData excelDataEntity1 = new ExcelDaysData();
                excelDataEntity1.setTitle(daysData1.getTitle());
                excelDataEntity1.setDate(daysData1.getDate());
                excelDataEntity1.setDays(daysData1.getDays());
                excelDataEntity1.setUnit(daysData1.getUnit());
                excelDataEntity1.setIsTop(String.valueOf(daysData1.getIsTop()));

                list.add(excelDataEntity1);
            }

            File filePath = getSDCardFolderPath("Export");
            if (!filePath.exists()) {
                filePath.mkdirs();
            }

//            String fileName = DateFormatUtil.getCurrentDateTime(DateFormatUtil.sdfDate60) + "_" +
//                    DateFormatUtil.getCurrentDateTime(DateFormatUtil.sdfTime40);
            file = new File(filePath.getAbsolutePath(), "LoveDay.xlsx");
            if (file.exists()) {
                FileUtil.deleteDir(file);
            }

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String excelFilePath = file.getAbsolutePath();
            Log.i("excelFilePath-->", excelFilePath);

            ExcelManager excelManager = new ExcelManager();
            OutputStream excelStream = new FileOutputStream(excelFilePath);

            boolean success = excelManager.toExcel(excelStream, list);
            long t2 = System.currentTimeMillis();

            //------------
//            String cachePath = getCacheDir().toString() + "/export";
//            File dir2 = new File(cachePath);
//            if (!dir2.exists()) {
//                dir2.mkdirs();
//            }
//            OutputStream cache = new FileOutputStream(cachePath + "/users.xls");
//            boolean success2 = excelManager.toExcel(cache, users);
            //------------

            double time = (t2 - t1) / 1000.0D;
            if (success) {
                Toast.makeText(this, "备份成功", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                Alerter.create(this)
                        .setText("备份失败")
                        .setBackgroundColor(R.color.colorAccent)
                        .setDuration(500)
                        .show();

                FileUtil.deleteFile(file);
            }
        } catch (Exception e) {
//            Toast.makeText(this, "保存异常", Toast.LENGTH_SHORT).show();
            Alerter.create(this)
                    .setText("备份异常")
                    .setBackgroundColor(R.color.colorAccent)
                    .setDuration(500)
                    .show();

            FileUtil.deleteFile(file);

            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (200 == event.message) {
            resetData();
        }
    }

    private boolean checkIsFirst() {
        Constant.bIsFirst = PreferencesUtil.getPrefBoolean(this, Constant.IS_FIRST, true);
        return Constant.bIsFirst;
    }

    private void setIsInstall() {
        PreferencesUtil.setPrefBoolean(this, Constant.IS_FIRST, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isColorfulBg) {
            triangulationDrawable.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (isColorfulBg) {
            triangulationDrawable.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setTintColor(color);
        }
    }

}
