package com.ljstudio.android.loveday.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.eventbus.MessageEvent;
import com.ljstudio.android.loveday.greendao.DaysDataDao;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.ljstudio.android.loveday.views.SwitchView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    @BindView(R.id.id_days_edit_save)
    Button tvSave;

    private int mEditType;

    private DatePickerDialog datePickerDialog;
    private String strEventTitle;
    private String strEventDate;
    private boolean isTop;

    private DaysData daysData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        toolbar.setTitle("新增事件");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);

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
            toolbar.setTitle("新增时光の记忆");
            tvTime.setText(DateFormatUtil.getCurrentDate(DateFormatUtil.sdfDate1));
            tvTop.setTextColor(getResources().getColor(R.color.colorGrayLight));
            Calendar calendar = Calendar.getInstance();
            datePickerDialog = DatePickerDialog.newInstance(EditActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        } else if (400 == mEditType) {
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

        mSwitchView.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean isChecked) {
                isTop =  isChecked;
                if (isChecked) {
                    tvTop.setTextColor(getResources().getColor(R.color.colorGray));
                } else {
                    tvTop.setTextColor(getResources().getColor(R.color.colorGrayLight));
                }

            }
        });
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
                    }

                    EditActivity.this.finish();
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

}
