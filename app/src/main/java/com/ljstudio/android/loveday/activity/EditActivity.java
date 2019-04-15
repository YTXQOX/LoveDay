package com.ljstudio.android.loveday.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.AllParameter;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.eventbus.MessageEvent;
import com.ljstudio.android.loveday.greendao.AllParameterDao;
import com.ljstudio.android.loveday.greendao.DaysDataDao;
import com.ljstudio.android.loveday.utils.ChineseNameGenerator;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.NetworkUtil;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.ljstudio.android.loveday.utils.VersionUtil;
import com.ljstudio.android.loveday.views.SwitchView;
import com.ljstudio.android.loveday.views.datetimepicker.date.DatePickerDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String DATE_PICKER_TAG = "date_picker";
    public static final String EDIT_TYPE = "edit_type";
    public static final String ID = "id";

    @BindView(R.id.id_edit_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_days_edit_title_text)
    TextInputEditText tvTitle;
    @BindView(R.id.id_days_edit_date_text)
    TextView tvTime;
    @BindView(R.id.id_days_edit_top_text)
    TextView tvTop;
    @BindView(R.id.id_days_edit_switch)
    SwitchView mSwitchView;
    @BindView(R.id.id_days_edit_square_layout)
    RelativeLayout layoutSquare;
    @BindView(R.id.id_days_edit_square_text)
    TextView tvSquare;
    @BindView(R.id.id_days_edit_square_text_hint)
    TextView tvSquareHint;
    @BindView(R.id.id_days_edit_square_switch)
    SwitchView mSquareSwitchView;
    @BindView(R.id.id_days_edit_save)
    Button tvSave;

    private int mEditType;

    private DatePickerDialog datePickerDialog;
    private String strEventTitle;
    private String strEventDate;
    private boolean isTop;
    private boolean isSend2Square;

    private DaysData daysData;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        toolbar.setTitle("新增事件");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);

        userName = PreferencesUtil.getPrefString(EditActivity.this, Constant.USER_NAME, "");
        if (TextUtils.isEmpty(userName)) {
            String generatedName = ChineseNameGenerator.getInstance().generate();
            Log.i("generatedName-->", generatedName);
            PreferencesUtil.setPrefString(EditActivity.this, Constant.USER_NAME, generatedName);
            userName = PreferencesUtil.getPrefString(EditActivity.this, Constant.USER_NAME, getResources().getString(R.string.app_name));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity.this.finish();
            }
        });

        setStatusBar(ContextCompat.getColor(this, R.color.colorPrimary));

//        Toasty.Config.getInstance()
//                .setErrorColor(getResources().getColor(R.color.colorRed)) // optional
//                .setInfoColor(getResources().getColor(R.color.colorGreen)) // optional
//                .setSuccessColor(getResources().getColor(R.color.colorBlue)) // optional
//                .setWarningColor(getResources().getColor(R.color.colorBlue)) // optional
//                .setTextColor(getResources().getColor(R.color.colorBlue)) // optional
//                .tintIcon(false) // optional (apply textColor also to the icon)
//                .setToastTypeface(Typeface.DEFAULT) // optional
//                .apply(); // required

        /**
         * 200 新建
         * 400 修改
         */
        mEditType = getIntent().getIntExtra(EDIT_TYPE, 200);
        if (200 == mEditType) {
            layoutSquare.setVisibility(View.VISIBLE);

            toolbar.setTitle("新增时光の记忆");
            tvTime.setText(DateFormatUtil.getCurrentDate(DateFormatUtil.sdfDate1));
            tvTop.setTextColor(getResources().getColor(R.color.colorGrayLight));
            Calendar calendar = Calendar.getInstance();
            datePickerDialog = DatePickerDialog.newInstance(EditActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        } else if (400 == mEditType) {
            layoutSquare.setVisibility(View.GONE);

            Long id = getIntent().getLongExtra(ID, -1);
            daysData = readOne4DB(id).get(0);

            toolbar.setTitle("修改时光の记忆");
            tvTitle.setText(daysData.getTitle());
            tvTitle.setSelection(daysData.getTitle().length());
            tvTime.setText(daysData.getDate());

            isTop = daysData.getIsTop();

            if (daysData.getIsTop()) {
                mSwitchView.setChecked(true);
                tvTop.setTextColor(getResources().getColor(R.color.colorGray));
            } else {
                mSwitchView.setChecked(false);
                tvTop.setTextColor(getResources().getColor(R.color.colorGrayLight));
            }

            Calendar calendar = Calendar.getInstance();
            Date date = DateFormatUtil.convertStr2Date(daysData.getDate(), DateFormatUtil.sdfDate1);
            calendar.setTime(date);
            datePickerDialog = DatePickerDialog.newInstance(EditActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        }

        mSquareSwitchView.setOnCheckedChangeListener((view, isChecked) -> {
            isSend2Square = isChecked;
            if (isChecked) {
                tvSquare.setTextColor(getResources().getColor(R.color.colorGray));
                tvSquareHint.setTextColor(getResources().getColor(R.color.colorGray));
            } else {
                tvSquare.setTextColor(getResources().getColor(R.color.colorGrayLight));
                tvSquareHint.setTextColor(getResources().getColor(R.color.colorGrayLight));
            }
        });

        mSwitchView.setOnCheckedChangeListener((view, isChecked) -> {
            isTop = isChecked;
            if (isChecked) {
                tvTop.setTextColor(getResources().getColor(R.color.colorGray));
            } else {
                tvTop.setTextColor(getResources().getColor(R.color.colorGrayLight));
            }
        });

        readAll4DB();
    }

    private List<AllParameter> readAll4DB() {
        final AllParameterDao dao = MyApplication.getDaoSession(this).getAllParameterDao();
        List<AllParameter> list = dao.queryBuilder()
                .orderAsc(AllParameterDao.Properties.Id)
                .build().list();
        for (AllParameter data : list) {
            SystemOutUtil.sysOut("读-->" + data.toString());
        }
        SystemOutUtil.sysOut("读-done");

        return list;
    }

    @OnClick(R.id.id_days_edit_date_text)
    public void setDate(View view) {
        datePickerDialog.setVibrate(false);
        datePickerDialog.setYearRange(1960, 2030);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    @OnClick(R.id.id_days_edit_save)
    public void saveEvent(View view) {
        strEventTitle = tvTitle.getText().toString().trim();
        strEventDate = tvTime.getText().toString().trim();

        if (TextUtils.isEmpty(strEventTitle)) {
            ToastUtil.showToast(EditActivity.this, "请填写事件");
            return;
        }

        DaysData data = new DaysData();
        data.setId(System.currentTimeMillis());
        data.setTitle(strEventTitle);
        data.setDate(strEventDate);
        data.setDays("1");
        data.setUnit("天");
        data.setIsTop(isTop);

        if (400 == mEditType) {
//            DaysData tempData = readOne4DB(daysData.getId()).get(0);
//            data.setId(daysData.getId());
//            data.setTitle(strEventTitle);
//            data.setDate(strEventDate);
//            data.setDays("1");
//            data.setUnit("天");
//            data.setIsTop(false);
//            writeOne2DB(tempData);
            data.setId(daysData.getId());

            deleteOne4DB(daysData.getId());
        }

        if (isTop) {
            // 将所有isTop置为false
            updateIsTop();
        }

        writeOne2DB(data);
    }

    private void writeOne2DB(final DaysData data) {
        try {
            MyApplication.getDaoSession(this).runInTx(new Runnable() {
                @Override
                public void run() {
                    long id = MyApplication.getDaoSession(EditActivity.this).getDaysDataDao().insertOrReplace(data);

                    EventBus.getDefault().post(new MessageEvent(200));

                    if (400 == mEditType) {
//                        ToastUtil.showToast(EditActivity.this, "修改成功");
                        Toasty.success(EditActivity.this, "修改成功", Toast.LENGTH_SHORT, true).show();

                        Intent intent = new Intent();
                        intent.putExtra(DetailActivity.RESULT_ID, id);
                        setResult(RESULT_OK, intent);
                    } else {
//                        ToastUtil.showToast(EditActivity.this, "添加成功");
                        Toasty.success(EditActivity.this, "添加成功", Toast.LENGTH_SHORT, true).show();

                        if (isSend2Square) {
                            send2Square(data);
                        }
                    }

                    EditActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send2Square(DaysData daysData) {
        if (NetworkUtil.checkNetworkOnly(EditActivity.this)) {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.READ_PHONE_STATE)
                    .onGranted(permissions -> {
                        String imei = getImei();
                        String strManufacturer = Build.MANUFACTURER;
                        String strDevice = Build.DEVICE;
                        String strBrand = Build.BRAND;
                        String strModel = Build.MODEL;
                        String strVersionRelease = Build.VERSION.RELEASE;

                        Locale locale;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            locale = getResources().getConfiguration().getLocales().get(0);
                        } else {
                            locale = getResources().getConfiguration().locale;
                        }
                        String language = locale.getLanguage() + "-" + locale.getCountry();

                        AVObject saveAV = new AVObject(Constant.DB_SQUARE);
                        saveAV.put("userId", imei);
                        saveAV.put("id", daysData.getId());
                        saveAV.put("title", daysData.getTitle());
                        saveAV.put("date", daysData.getDate());
                        saveAV.put("days", daysData.getDays());
                        saveAV.put("unit", "天");
                        saveAV.put("isTop", daysData.getIsTop());
                        saveAV.put("nickname", userName);

                        saveAV.put("info_manufacturer", strManufacturer);
                        saveAV.put("info_device", strDevice);
                        saveAV.put("info_barnd", strBrand);
                        saveAV.put("info_model", strModel);
                        saveAV.put("info_language", language);
                        saveAV.put("info_version", VersionUtil.getVersionName(EditActivity.this));
                        saveAV.put("info_system", strVersionRelease);
                        saveAV.put("info_platform", "android");

                        /**
                         * 删除
                         */
                        AVQuery<AVObject> queryAV = new AVQuery<>(Constant.DB_SQUARE);
                        queryAV.whereEqualTo("id", daysData.getId());
                        SystemOutUtil.sysOut("daysData1.getId()-->" + daysData.getId());
                        queryAV.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (null != list && 0 != list.size()) {
//                                        String cql = "delete from " +  db + " where id=\'" + data.get("id") + "\'";
//                                        Log.i("cql-->", cql);
//                                        AVQuery.doCloudQueryInBackground(cql, new CloudQueryCallback<AVCloudQueryResult>() {
//                                            @Override
//                                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
//                                                // 如果 e 为空，说明保存成功
//                                            }
//                                        });

                                    SystemOutUtil.sysOut("list.size()-->" + list.size());
                                    AVObject.deleteAllInBackground(list, new DeleteCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e != null) {
                                                // 错误
                                                ToastUtil.showToast(EditActivity.this, e.getCode());
                                            } else {
                                                // 成功
                                                /**
                                                 * 保存数据
                                                 */
                                                saveAV.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(AVException e) {
                                                        if (e == null) {
                                                            // 存储成功
                                                            Toasty.success(EditActivity.this, "成功发布到时光广场").show();
                                                        } else {
                                                            // 失败的话，请检查网络环境以及 SDK 配置是否正确
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    /**
                                     * 保存数据
                                     */
                                    saveAV.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e == null) {
                                                // 存储成功
                                                Toasty.success(EditActivity.this, "成功发布到时光广场").show();
                                            } else {
                                                // 失败的话，请检查网络环境以及 SDK 配置是否正确
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    })
                    .onDenied(permissions -> {
                        new MaterialDialog.Builder(EditActivity.this)
                                .title("权限设置")
                                .negativeText("取消")
                                .positiveText("去设置")
                                .content("上传及同步成绩需要开启手机识别码权限(READ_PHONE_STATE)获取您的 IMEI 作为您的唯一ID")
                                .onPositive((dialog, which) -> {
                                    getAppDetailSettingIntent(EditActivity.this);
                                    dialog.dismiss();
                                })
                                .onNegative((dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();

                    })
                    .start();
        }
    }

    @SuppressLint("MissingPermission")
    private String getImei() {
        String imei = "imei";
        try {
            final TelephonyManager manager = (TelephonyManager) EditActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager.getDeviceId() == null || manager.getDeviceId().equals("")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    imei = manager.getDeviceId(0);
                }
            } else {
                imei = manager.getDeviceId();
            }
        } catch (Exception e) {

        }

        return imei;
    }

    private List<DaysData> readOne4DB(Long id) {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .where(DaysDataDao.Properties.Id.eq(id))
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();

        return list;
    }

    private void deleteOne4DB(Long id) {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        dao.deleteByKeyInTx(id);
    }

    private void updateIsTop() {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();

        for (DaysData daysData : list) {
            daysData.setIsTop(false);
            dao.update(daysData);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String strDate = year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + (day < 10 ? "0" + day : day);
        tvTime.setText(strDate);
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

    /**
     * 跳转到权限设置界面
     */
    private void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

}
