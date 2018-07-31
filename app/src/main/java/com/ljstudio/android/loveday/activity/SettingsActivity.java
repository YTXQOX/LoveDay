package com.ljstudio.android.loveday.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.utils.ChineseNameGenerator;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.utils.ScreenUtil;
import com.tapadoo.alerter.Alerter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guoren on 2018-6-5 14:26
 * Usage
 */

public class SettingsActivity extends AppCompatActivity {

    private final static String ADMIN = "admin";

    @BindView(R.id.id_setting_toolbar)
    Toolbar toolBar;
    @BindView(R.id.id_user_avatar)
    CircleImageView avatar;
    @BindView(R.id.id_user_name)
    TextView tvUserName;

    @BindView(R.id.id_setting_about_app_layout)
    RelativeLayout layoutAboutApp;

    @BindView(R.id.id_setting_welfare_layout)
    RelativeLayout layoutWelfare;
    @BindView(R.id.id_setting_reward_layout)
    RelativeLayout layoutReward;

    private String userName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        toolBar.setTitle(R.string.settings);
        toolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolBar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolBar);

        userName = PreferencesUtil.getPrefString(SettingsActivity.this, Constant.USER_NAME, "");
        if (TextUtils.isEmpty(userName)) {
            String generatedName = ChineseNameGenerator.getInstance().generate();
            Log.i("generatedName-->", generatedName);
            PreferencesUtil.setPrefString(SettingsActivity.this, Constant.USER_NAME, generatedName);
            userName = PreferencesUtil.getPrefString(SettingsActivity.this, Constant.USER_NAME, getResources().getString(R.string.app_name));
        }
        tvUserName.setText(userName);

//        String generatedEmail = EmailAddressGenerator.getInstance().generate();
//        Log.i("generatedEmail-->", generatedEmail);
//        String hash = MD5Util.md5Hex(generatedEmail);
//        Log.i("hash-->", hash);
//        String url = "https://www.gravatar.com/avatar/" + hash;
//        Log.i("url-->", url);
//        Glide.with(this).load(url).into(avatar);

        toolBar.setNavigationOnClickListener(view -> SettingsActivity.this.finish());

    }

    @OnClick(R.id.id_setting_welfare_layout)
    public void welfare(View view) {
        Intent intent = new Intent(SettingsActivity.this, WelfareActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.id_setting_reward_layout)
    public void showReward(View view) {
        showRewardDialog();
    }

    @OnClick(R.id.id_user_avatar)
    public void go2Apps() {
        Intent intent = new Intent(SettingsActivity.this, AppsActivity.class);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private String getImei() {
        String imei = "imei";
        try {
            final TelephonyManager manager = (TelephonyManager) SettingsActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
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

    /**
     * 赞赏 dialog
     */
    private void showRewardDialog() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this);
        final View dialogView = LayoutInflater.from(SettingsActivity.this)
                .inflate(R.layout.layout_reward_dialog, null);
//        dialogBuilder.setView(dialogView);

        TextView tvSend = dialogView.findViewById(R.id.id_reward_code_send);
        tvSend.setOnClickListener(v -> {
            send(v);
        });

        android.support.v7.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, ScreenUtil.dip2px(SettingsActivity.this, 350));
        dialog.setContentView(dialogView);
    }

    @OnClick(R.id.id_setting_about_app_layout)
    public void update(View view) {
        Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void send(View view) {
//        "com.tencent.mm.ui.tools.ShareImgUI"发送给联系人
//        "com.tencent.mm.ui.tools.AddFavoriteUI"添加到收藏
//        "com.tencent.mm.ui.tools.ShareToTimeLineUI" 发送到朋友圈

//        File shareFile = new File(shareFilePath);
//        Uri fileUri = Uri.fromFile(shareFile);

        if (isApkInstalled(SettingsActivity.this, Constant.WECHAT_APP)) {
            String path = getResourcesUri(R.mipmap.ic_wallet_reward_code);
            Uri fileUri = Uri.parse(path);

            Intent intent = new Intent();
            ComponentName comp = new ComponentName(Constant.WECHAT_APP, "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            startActivity(intent);
        } else {
            Alerter.create(SettingsActivity.this)
                    .setText("未安装微信客户端")
                    .setBackgroundColor(R.color.colorAccent)
                    .setDuration(800)
                    .show();
        }
    }

    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

    private boolean isApkInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
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
