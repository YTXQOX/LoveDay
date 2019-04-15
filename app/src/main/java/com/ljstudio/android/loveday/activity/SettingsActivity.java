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
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.utils.ChineseNameGenerator;
import com.ljstudio.android.loveday.utils.NetworkUtil;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.utils.ScreenUtil;
import com.tapadoo.alerter.Alerter;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

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
    @BindView(R.id.id_setting_square_layout)
    RelativeLayout layoutSquare;
    @BindView(R.id.id_setting_moments_layout)
    RelativeLayout layoutMoments;
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

    @OnClick(R.id.id_setting_square_layout)
    public void square(View view) {
        if (NetworkUtil.checkNetworkOnly(SettingsActivity.this)) {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.READ_PHONE_STATE)
                    .onGranted(permissions -> {
                        String imei = getImei();
                        Intent intent = new Intent(SettingsActivity.this, SquareActivity.class);
                        intent.putExtra(SquareActivity.IMEI, imei);
                        startActivity(intent);
                    })
                    .onDenied(permissions -> {
                        new MaterialDialog.Builder(SettingsActivity.this)
                                .title("权限设置")
                                .negativeText("取消")
                                .positiveText("去设置")
                                .content("时光广场需要开启手机状态权限(READ_PHONE_STATE)获取您的IMEI作为您的唯一ID")
                                .onPositive((dialog, which) -> {
                                    getAppDetailSettingIntent(SettingsActivity.this);
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

    @OnClick(R.id.id_setting_moments_layout)
    public void moments(View view) {
        String imei = getImei();
        Intent intent = new Intent(SettingsActivity.this, MomentsActivity.class);
        intent.putExtra(MomentsActivity.IMEI, imei);
        startActivity(intent);
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
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(SettingsActivity.this);
        final View dialogView = LayoutInflater.from(SettingsActivity.this)
                .inflate(R.layout.layout_reward_dialog, null);
//        dialogBuilder.setView(dialogView);

        TextView tvSend = dialogView.findViewById(R.id.id_reward_code_send);
        tvSend.setOnClickListener(v -> {
            send(v);
        });

        androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();
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
