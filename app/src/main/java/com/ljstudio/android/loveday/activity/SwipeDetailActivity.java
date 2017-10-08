package com.ljstudio.android.loveday.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.adapter.SwipeDetailAdapter;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.entity.DetailData;
import com.ljstudio.android.loveday.eventbus.MessageEvent;
import com.ljstudio.android.loveday.greendao.DaysDataDao;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.ScreenShotUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.ljstudio.android.loveday.views.swipefling.SwipeFlingAdapterView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nekocode.triangulation.TriangulationDrawable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SwipeDetailActivity extends AppCompatActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener, View.OnClickListener {

    public static final String ID = "id";
    public static final String POSITION = "position";
    public static final String RESULT_ID = "RESULT_ID";
    private static final int REQUEST_CODE_EDIT = 1024;

    @BindView(R.id.id_swipe_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_swipe_detail_layout)
    RelativeLayout bgLayout;
    @BindView(R.id.id_swipe_detail_swipe_view)
    SwipeFlingAdapterView swipeFlingAdapterView;

    private List<DetailData> listDetailData = new ArrayList<>();

    private List<DaysData> listDays = new ArrayList<>();
    private DaysData daysData;

    private Disposable mDisposable;
    private TriangulationDrawable triangulationDrawable;

    private SwipeDetailAdapter swipeDetailAdapter;
    private int cardWidth;
    private int cardHeight;

    private Long id;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_detail);

        ButterKnife.bind(this);

        toolbar.setTitle("时光の记忆");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeDetailActivity.this.finish();
            }
        });

        setStatusBar(ContextCompat.getColor(this, R.color.colorPrimary));

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));

        id = getIntent().getLongExtra(ID, -1);
        position = getIntent().getIntExtra(POSITION, -1);

        swipeDetailAdapter = new SwipeDetailAdapter(SwipeDetailActivity.this, listDetailData, cardWidth, cardHeight);
        if (swipeFlingAdapterView != null) {
            swipeFlingAdapterView.setIsNeedSwipe(true);
            swipeFlingAdapterView.setFlingListener(this);
            swipeFlingAdapterView.setOnItemClickListener(this);

            swipeFlingAdapterView.setAdapter(swipeDetailAdapter);
        }

        initOneData(id);
        initAllData();
    }

    private void initOneData(Long id) {
        listDays.clear();
        listDays = readOne4DB(id);
        daysData = listDays.get(0);

        SystemOutUtil.sysOut("daysData.toString()====>" + daysData.toString());
    }

    private void initAllData() {
        List<DaysData> list = readAll4DB();
        list.remove(daysData);
        list.add(0, daysData);

        for (DaysData data : list) {
            DetailData detailData1 = new DetailData();

            Date date = DateFormatUtil.convertStr2Date(daysData.getDate(), DateFormatUtil.sdfDate1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            int type = getSeason(month);

            detailData1.setId(data.getId());
            detailData1.setMonthBackground(getMonthBg(month));
            detailData1.setFalling(getFalling(type));
            detailData1.setTitle(data.getTitle());
            detailData1.setDate(data.getDate());
            detailData1.setDays(data.getDays());
            detailData1.setUnit(data.getUnit());
            detailData1.setTop(data.getIsTop());

            listDetailData.add(detailData1);
        }

        Date date = DateFormatUtil.convertStr2Date(listDetailData.get(0).getDate(), DateFormatUtil.sdfDate1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int type = getSeason(month);
        setSeasonBg(type);

        SystemOutUtil.sysOut("listDetailData.get(0).toString()====>" + listDetailData.get(0).toString());

        swipeDetailAdapter.addAll(listDetailData);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SwipeDetailActivity.this.getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.id_action_edit) {
                Intent intent = new Intent();
                intent.putExtra(EditActivity.EDIT_TYPE, 400);
                intent.putExtra(EditActivity.ID, daysData.getId());
                intent.setClass(SwipeDetailActivity.this, EditActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            } else if (id == R.id.id_action_delete) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(SwipeDetailActivity.this);
                builder.title("小温馨");
                builder.content("这是个重要的日子，真的要忘记她吗");
                builder.positiveText("删除");
                builder.negativeText("取消");
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        deleteOne4DB(daysData.getId());
                        dialog.dismiss();
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

                builder.build().show();
            } else if (id == R.id.id_action_share) {
//                String strPath = ScreenShotUtil.screenShotBitmap(DetailActivity.this);
//                List<String> list = new ArrayList<>();
//                list.add(strPath);
//                share(list, "爱の记忆", daysData.getTitle());

                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        String strPath = ScreenShotUtil.screenShotBitmap(SwipeDetailActivity.this);
                        if (!TextUtils.isEmpty(strPath)) {
                            emitter.onNext(strPath);

                            emitter.onComplete();
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(String s) {
                        SystemOutUtil.sysOut("s-->" + s);
                        List<String> list = new ArrayList<>();
                        list.add(s);

                        share(list, "爱の记忆", daysData.getTitle());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                });
            }

            return true;
        }
    };

    private void writeOne2DB(final DaysData data) {
        try {
            MyApplication.getDaoSession(this).runInTx(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getDaoSession(SwipeDetailActivity.this).getDaysDataDao().insertOrReplace(data);
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
                        MyApplication.getDaoSession(SwipeDetailActivity.this).getDaysDataDao().insertOrReplace(entity);
                        size = size + 1;

                        if (size >= data.size()) {
                            ToastUtil.showToast(SwipeDetailActivity.this, "数据存储成功");
                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<DaysData> readOne4DB(Long id) {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .where(DaysDataDao.Properties.Id.eq(id))
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();

        return list;
    }

    private List<DaysData> readAll4DB() {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();

        return list;
    }

    private void deleteOne4DB(Long id) {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        dao.deleteByKeyInTx(id);

        EventBus.getDefault().post(new MessageEvent(200));

        SwipeDetailActivity.this.finish();
    }

    private void share(List<String> listPath, String strSubject, String strText) {
        String filePath;
        int size = listPath.size();

        if (size == 0) {
            Toast.makeText(this, "请选择要分享的图片", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Uri> uris = new ArrayList<>();
            for (String path : listPath) {
                filePath = path;
                Uri uri = Uri.parse("file:///" + filePath);
                uris.add(uri);
            }

            boolean multiple = uris.size() > 1;
            Intent intent = new Intent(multiple ? Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND);
            intent.setType("image/png");
//            intent.putExtra(Intent.EXTRA_SUBJECT, strSubject);
//            intent.putExtra(Intent.EXTRA_TEXT, strText);
            if (multiple) {
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, "爱の分享"));
        }
    }

    private int getSeason(int month) {
        int type;
        if (1 == month || 2 == month || 12 == month) {
            type = 4;   // winter
        } else if (3 == month || 4 == month || 5 == month) {
            type = 1;   // spring
        } else if (6 == month || 7 == month || 8 == month) {
            type = 2;   // summer
        } else if (9 == month || 10 == month || 11 == month) {
            type = 3;   // autumn
        } else {
            type = 1;   // default spring
        }

//        date.getMonth();
//        Random random = new Random();
//        type = random.nextInt(5);
        return type;
    }

    private int getFalling(int type) {
        switch (type) {
            case 1:
                return R.mipmap.ic_spring_flower;
            case 2:
                return R.mipmap.ic_summer_flower;
            case 3:
                return R.mipmap.ic_autumn_leaf;
            case 4:
                return R.mipmap.ic_winter_snow;
            default:
                return R.mipmap.ic_spring_flower;
        }
    }

    private int getSeasonBg(int type) {
        switch (type) {
            case 1:
                return R.mipmap.ic_spring;
            case 2:
                return R.mipmap.ic_summer;
            case 3:
                return R.mipmap.ic_autumn;
            case 4:
                return R.mipmap.ic_winter;
            default:
                return R.mipmap.ic_spring;
        }
    }

    private int getMonthBg(int month) {
        switch (month) {
            case 1:
                return R.mipmap.google_calendar_01;
            case 2:
                return R.mipmap.google_calendar_02;
            case 3:
                return R.mipmap.google_calendar_03;
            case 4:
                return R.mipmap.google_calendar_04;
            case 5:
                return R.mipmap.google_calendar_05;
            case 6:
                return R.mipmap.google_calendar_06;
            case 7:
                return R.mipmap.google_calendar_07;
            case 8:
                return R.mipmap.google_calendar_08;
            case 9:
                return R.mipmap.google_calendar_09;
            case 10:
                return R.mipmap.google_calendar_10;
            case 11:
                return R.mipmap.google_calendar_11;
            case 12:
                return R.mipmap.google_calendar_12;
            default:
                return R.mipmap.google_calendar_01;
        }
    }

    private void setSeasonBg(int type) {
        switch (type) {
            case 1:
                bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                break;
            case 2:
                bgLayout.setBackgroundResource(R.mipmap.ic_summer);
                break;
            case 3:
                bgLayout.setBackgroundResource(R.mipmap.ic_autumn);
                break;
            case 4:
                bgLayout.setBackgroundResource(R.mipmap.ic_winter);
                break;
            default:
                bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                break;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_EDIT) {
                long mResultId = data.getLongExtra(RESULT_ID, -1);

                initOneData(mResultId);

                swipeDetailAdapter.clear();
                listDetailData.clear();
                initAllData();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        triangulationDrawable.start();
    }

    @Override
    protected void onStop() {
//        triangulationDrawable.stop();
        super.onStop();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {

    }

    @Override
    public void removeFirstObjectInAdapter() {
        swipeDetailAdapter.remove(0);

        Date date = DateFormatUtil.convertStr2Date(listDetailData.get(0).getDate(), DateFormatUtil.sdfDate1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int type = getSeason(month);
        setSeasonBg(type);

        daysData.setId(listDetailData.get(0).getId());
        daysData.setTitle(listDetailData.get(0).getTitle());
        daysData.setDate(listDetailData.get(0).getDate());
        daysData.setDays(listDetailData.get(0).getDays());
        daysData.setUnit(listDetailData.get(0).getUnit());
        daysData.setIsTop(listDetailData.get(0).getIsTop());

        SystemOutUtil.sysOut("daysData.toString()====>" + daysData.toString());
        SystemOutUtil.sysOut("listDetailData.get(0).toString()====>" + listDetailData.get(0).toString());
    }

    @Override
    public void onLeftCardExit(Object dataObject) {
        SystemOutUtil.sysOut("onLeftCardExit====>" + ((DetailData) dataObject).getTitle());
    }

    @Override
    public void onRightCardExit(Object dataObject) {
        SystemOutUtil.sysOut("onRightCardExit====>" + ((DetailData) dataObject).getTitle());
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 3) {
            initAllData();
        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {
        SystemOutUtil.sysOut("progress====>" + progress + "====scrollXProgress====>" + scrollXProgress);

        if (100.0f == progress) {

        }

    }
}
