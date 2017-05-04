package com.ljstudio.android.loveday.activity;

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

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.eventbus.MessageEvent;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ljstudio.android.loveday.greendao.DaysDataDao;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String DATE_PICKER_TAG = "date_picker";

    @BindView(R.id.id_edit_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_days_edit_title_text)
    TextInputEditText tvTitle;
    @BindView(R.id.id_days_edit_date_text)
    TextView tvTime;
    @BindView(R.id.id_days_edit_save)
    Button tvSave;

    private DatePickerDialog datePickerDialog;
    private String strEventTitle;
    private String strEventDate;


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

        tvTime.setText(DateFormatUtil.getCurrentDate(DateFormatUtil.sdfDate1));

        Calendar calendar = Calendar.getInstance();

        datePickerDialog = DatePickerDialog.newInstance(EditActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
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
        if (TextUtils.isEmpty(strEventTitle)) {
            ToastUtil.showToast(EditActivity.this, "请填写事件");
            return;
        }

        DaysData data = new DaysData();
        data.setTitle(strEventTitle);
        data.setDate(strEventDate);
        data.setDays("1");
        data.setUnit("天");
        data.setIsTop(false);
        writeOne2DB(data);
    }

    private void writeOne2DB(final DaysData data) {
        try {
            MyApplication.getDaoSession(this).runInTx(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getDaoSession(EditActivity.this).getDaysDataDao().insertOrReplace(data);

                    EventBus.getDefault().post(new MessageEvent(200));

                    ToastUtil.showToast(EditActivity.this, "添加成功");
                    EditActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readOne4DB(Long id) {
        final DaysDataDao dao = MyApplication.getDaoSession(this).getDaysDataDao();
        List<DaysData> list = dao.queryBuilder()
                .where(DaysDataDao.Properties.Id.eq(id))
                .orderAsc(DaysDataDao.Properties.Id)
                .build().list();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        strEventDate = year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + (day < 10 ? "0" + day : day);
        tvTime.setText(strEventDate);
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
