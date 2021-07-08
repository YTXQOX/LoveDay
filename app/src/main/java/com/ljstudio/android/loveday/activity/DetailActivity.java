package com.ljstudio.android.loveday.activity;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ljstudio.android.loveday.BuildConfig;
import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.eventbus.MessageEvent;
import com.ljstudio.android.loveday.greendao.DaysDataDao;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.utils.ScreenShotUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.ljstudio.android.loveday.views.HoldPressHelper;
import com.ljstudio.android.loveday.views.SprayView;
import com.ljstudio.android.loveday.views.fallingview.FallingView;
import com.ljstudio.android.loveday.views.fonts.FontsManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nekocode.triangulation.TriangulationDrawable;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    public static final String ID = "id";
    public static final String POSITION = "position";
    public static final String RESULT_ID = "RESULT_ID";
    private static final int REQUEST_CODE_EDIT = 1024;

    @BindView(R.id.id_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_detail_spray_view)
    SprayView sprayView;
    @BindView(R.id.id_detail_days_title)
    TextView tvTitle;
    @BindView(R.id.id_detail_days_layout)
    LinearLayout layoutDays;
    @BindView(R.id.id_detail_days_days)
    TextView tvDays;
    @BindView(R.id.id_detail_days_start)
    TextView tvStart;
    @BindView(R.id.id_detail_layout)
    RelativeLayout bgLayout;
    @BindView(R.id.id_detail_falling_view)
    FallingView fallingView;

    private List<DaysData> listDays = new ArrayList<>();
    private DaysData daysData;

    private Disposable mDisposable;
    private TriangulationDrawable triangulationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        toolbar.setTitle("时光の记忆");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity.this.finish();
            }
        });

        setStatusBar(ContextCompat.getColor(this, R.color.colorPrimary));

        Long id = getIntent().getLongExtra(ID, -1);
        initData(id);

        HoldPressHelper.addHoldPressListener(findViewById(R.id.id_detail_days_title), new HoldPressHelper.OnHoldPressListener() {
            @Override
            public void onTouchDown(View v) {

            }

            @Override
            public void onHold(View v) {
                sprayView.makeBody();
                sprayView.startScroller();
            }

            @Override
            public void onTouchUp(View v) {

            }
        }, 300);

    }

    private void initData(Long id) {
        listDays.clear();
        listDays = readOne4DB(id);
        daysData = listDays.get(0);

        tvTitle.setText(daysData.getTitle());

        Date date = DateFormatUtil.convertStr2Date(daysData.getDate(), DateFormatUtil.sdfDate1);
        final int days = DateUtil.betweenDays(date, new Date());
        tvDays.setText(String.valueOf(days));

        tvDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, PreviewShowActivity.class);
                intent.putExtra(PreviewShowActivity.CONTENT, tvTitle.getText().toString());
                startActivity(intent);
            }
        });
        FontsManager.initFormAssets(this, "fonts/gtw.ttf");
        FontsManager.changeFonts(tvDays);

        if (1 == DateUtil.compareDate(date, new Date())) {
            tvDays.setTextColor(getResources().getColor(R.color.colorBlue));
            tvStart.setText("目标日：" + daysData.getDate());
        } else {
            tvDays.setTextColor(getResources().getColor(R.color.colorAccent));
            tvStart.setText("起始日：" + daysData.getDate());
        }

//        RandomColor randomColor = new RandomColor();
//        int color = randomColor.randomColor();
//        triangulationDrawable = new TriangulationDrawable(color);
//        triangulationDrawable = new TriangulationDrawable(0xbbfbfbfb);
//        tvDays.setBackground(triangulationDrawable);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        if (BuildConfig.LOG_DEBUG) {
            Log.i("DetailActivity", month + "");
        }

        int type = getSeason(month);
        setSeasonBg(type);
        setMonthBg(month);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvDays.post(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Animator animator = ViewAnimationUtils.createCircularReveal(tvDays,
                                tvDays.getWidth() / 2, tvDays.getHeight() / 2, 0, tvDays.getWidth());
                        animator.setDuration(2222);
                        animator.start();
                    }
                }
            });
        }

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strContent;
                String strButton;
                String strInfo;
                boolean isColorfulBg = PreferencesUtil.getPrefBoolean(DetailActivity.this, Constant.COLORFUL_BG, false);
                if (isColorfulBg) {
                    strContent = "即将关闭首页炫彩界面，嘤嘤嘤~~~";
                    strButton = "关闭";
                    strInfo = "已关闭炫彩界面，将在下次启动时生效";
                } else {
                    strContent = "即将开启首页炫彩界面，吼吼吼~~~";
                    strButton = "开启";
                    strInfo = "已开启炫彩界面，将在下次启动时生效";
                }

                showColorfulBg(isColorfulBg, strContent, strButton, strInfo);
            }
        });
    }

    private void showColorfulBg(final boolean isColorfulBg, String content, String btn, final String info) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(DetailActivity.this);
        builder.title("我是第二彩蛋");
        builder.content(content);
        builder.positiveText(btn);
        builder.negativeText("取消");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                PreferencesUtil.setPrefBoolean(DetailActivity.this, Constant.COLORFUL_BG, !isColorfulBg);
                dialog.dismiss();

                Toasty.info(DetailActivity.this, info).show();
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
        DetailActivity.this.getMenuInflater().inflate(R.menu.detail_menu, menu);
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
                intent.setClass(DetailActivity.this, EditActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            } else if (id == R.id.id_action_delete) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(DetailActivity.this);
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
                        String strPath = ScreenShotUtil.screenShotBitmap(DetailActivity.this);
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
                    MyApplication.getDaoSession(DetailActivity.this).getDaysDataDao().insertOrReplace(data);
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
                        MyApplication.getDaoSession(DetailActivity.this).getDaysDataDao().insertOrReplace(entity);
                        size = size + 1;

                        if (size >= data.size()) {
                            ToastUtil.showToast(DetailActivity.this, "数据存储成功");
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

        DetailActivity.this.finish();
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
            Intent intent = new Intent(multiple ? android.content.Intent.ACTION_SEND_MULTIPLE : android.content.Intent.ACTION_SEND);
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

    private void setSeasonBg(int type) {
        switch (type) {
            case 1:
                bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                fallingView.setImageResource(R.mipmap.ic_spring_flower);
                break;
            case 2:
                bgLayout.setBackgroundResource(R.mipmap.ic_summer);
                fallingView.setImageResource(R.mipmap.ic_summer_flower);
                break;
            case 3:
                bgLayout.setBackgroundResource(R.mipmap.ic_autumn);
                fallingView.setImageResource(R.mipmap.ic_autumn_leaf);
                break;
            case 4:
                bgLayout.setBackgroundResource(R.mipmap.ic_winter);
                fallingView.setImageResource(R.mipmap.ic_winter_snow);
                break;
            default:
                bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                fallingView.setImageResource(R.mipmap.ic_spring_flower);
                break;
        }
    }

    private void setMonthBg(int month) {
        switch (month) {
            case 1:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_01);
                break;
            case 2:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_02);
                break;
            case 3:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_03);
                break;
            case 4:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_04);
                break;
            case 5:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_05);
                break;
            case 6:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_06);
                break;
            case 7:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_07);
                break;
            case 8:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_08);
                break;
            case 9:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_09);
                break;
            case 10:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_10);
                break;
            case 11:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_11);
                break;
            case 12:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_12);
                break;
            default:
                layoutDays.setBackgroundResource(R.mipmap.google_calendar_01);
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
                long mResultType = data.getLongExtra(RESULT_ID, -1);

                initData(mResultType);
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
}
