package com.ljstudio.android.loveday.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.ljstudio.android.loveday.utils.WaterMarkUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkViewActivity extends AppCompatActivity {

    @BindView(R.id.id_mark_view_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_mark_image_name)
    TextInputEditText etMarkImageName;
    @BindView(R.id.id_mark_image_text)
    TextInputEditText etMarkImageText;
//    @BindView(R.id.id_mark_button)
//    Button btnMark;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_view);

        ButterKnife.bind(this);

        toolbar.setTitle("水印");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> MarkViewActivity.this.finish());

        setStatusBar(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @OnClick(R.id.id_mark_button)
    public void setMark(View view) {
        String strImageName = etMarkImageName.getText().toString().trim();
        String strMarkText = etMarkImageText.getText().toString().trim();

        if (TextUtils.isEmpty(strImageName)) {
            ToastUtil.showToast(MarkViewActivity.this, "请输入图片名称");
            return;
        }

        String path = WaterMarkUtil.getImageFilePath(strImageName);
        SystemOutUtil.sysOut("path==>" + path);
        if (!((new File(path)).exists())) {
            ToastUtil.showToast(MarkViewActivity.this, "图片不存在");
            return;
        }

        if (TextUtils.isEmpty(strMarkText)) {
            ToastUtil.showToast(MarkViewActivity.this, "请输入水印文字");
            return;
        }

        WaterMarkUtil.addWatermarkBitmap(MarkViewActivity.this, WaterMarkUtil.getLocalBitmap(path), strImageName, strMarkText, 0, 0);
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
