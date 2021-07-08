package com.ljstudio.android.loveday.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.ljstudio.android.loveday.utils.VersionUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.id_setting_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_version)
    TextView tvVersion;
    @BindView(R.id.id_setting_update_layout)
    RelativeLayout updateLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        setStatusBarColor(R.color.colorPrimary);
//        setImmerseStatusBar(R.color.colorPrimary);

        toolbar.setTitle(R.string.menu_setting);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });

        tvVersion.setText(VersionUtil.getVersionName(SettingActivity.this).toString());

        updateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpdate();
            }
        });

    }

    private void checkUpdate() {
        ToastUtil.toastShortCenter(SettingActivity.this,"当前版本已是最新");
//        if (NetworkUtil.checkNetworkOnly(SettingActivity.this)) {
//            UpdateHelperUtil.getInstance(SettingActivity.this).check4UpdateLeanCloud(SettingActivity.this, true, Constant.DB_UPDATE, true);
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setImmerseStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
