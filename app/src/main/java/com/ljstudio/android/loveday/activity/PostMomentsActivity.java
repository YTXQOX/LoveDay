package com.ljstudio.android.loveday.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.eventbus.MessageEvent;
import com.ljstudio.android.loveday.utils.ChineseNameGenerator;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.NetworkUtil;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.utils.VersionUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PostMomentsActivity extends AppCompatActivity {

    @BindView(R.id.id_post_moments_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_post_moments_content)
    TextInputEditText tvContent;
    @BindView(R.id.id_post_moments_hint)
    TextView tvHint;
    @BindView(R.id.id_post_moments_image)
    ImageView ivImage;
    @BindView(R.id.id_post_moments_image_delete)
    ImageView ivDelete;
    @BindView(R.id.id_post_moments_post)
    Button tvSave;

    private AlertDialog loadingDialog;

    private String userName;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private List<String> selectMediaPath = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_moments);

        ButterKnife.bind(this);

        toolbar.setTitle("新发布");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);

        userName = PreferencesUtil.getPrefString(PostMomentsActivity.this, Constant.USER_NAME, "");
        if (TextUtils.isEmpty(userName)) {
            String generatedName = ChineseNameGenerator.getInstance().generate();
            Log.i("generatedName-->", generatedName);
            PreferencesUtil.setPrefString(PostMomentsActivity.this, Constant.USER_NAME, generatedName);
            userName = PreferencesUtil.getPrefString(PostMomentsActivity.this, Constant.USER_NAME, getResources().getString(R.string.app_name));
        }

        toolbar.setNavigationOnClickListener(view -> PostMomentsActivity.this.finish());

        setStatusBar(ContextCompat.getColor(this, R.color.colorPrimary));

        createLoadingDialog();

        tvContent.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            tvHint.setText(s.length() + "/41");
            if (s.length() > 41) {
                tvHint.setTextColor(getResources().getColor(R.color.colorRed));
            } else {
                tvHint.setTextColor(getResources().getColor(R.color.colorGrayLight));
            }
        }
    };

    @OnClick(R.id.id_post_moments_image)
    public void select(View view) {
        selectMedia.clear();
        selectMediaPath.clear();

        PictureSelector.create(PostMomentsActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(R.style.pic)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
//                .enableCrop()// 是否裁剪 true or false
//                .compress()// 是否压缩 true or false
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
//                .compressSavePath(getPath())//压缩图片保存地址
//                .freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer()// 是否圆形裁剪 true or false
//                .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .openClickSound()// 是否开启点击声音 true or false
                .selectionMedia(selectMedia)// 是否传入已选图片 List<LocalMedia> list
//                .previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality()// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .rotateEnabled() // 裁剪是否可旋转图片 true or false
//                .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
//                .videoQuality()// 视频录制质量 0 or 1 int
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

//    private PictureConfig.OnSelectResultCallback resultCallback = resultList -> {
//        selectMedia = resultList;
//        if (selectMedia.size() != 0) {
//            for (LocalMedia localMedia : selectMedia) {
//                if (localMedia.isCompressed()){
//                    // 注意：如果压缩过，在上传的时候，取 media.getCompressPath(); // 压缩图compressPath
//                    selectMediaPath.add(localMedia.getCompressPath());
//                } else {
//                    // 注意：没有压缩过，在上传的时候，取 media.getPath(); // 原图path
//                    selectMediaPath.add(localMedia.getPath());
//                }
//            }
//
//            ivDelete.setVisibility(View.VISIBLE);
//            Glide.with(PostMomentsActivity.this).load(selectMediaPath.get(0)).into(ivImage);
//        }
//    };

    private void createLoadingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostMomentsActivity.this, R.style.AlertDialogStyle);
        final View dialogView = LayoutInflater.from(PostMomentsActivity.this).inflate(R.layout.layout_loading_progress_dialog, null);
        dialogBuilder.setView(dialogView);
        loadingDialog = dialogBuilder.create();
    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "图片"), 200);
    }

    private void upload(final String strContent) {
        loadingDialog.show();

        String path = selectMediaPath.get(0);
        try {
            AVFile file = AVFile.withAbsoluteLocalPath("moments_" + System.currentTimeMillis() + ".png", path);

            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    send2Moments(strContent, file.getUrl());
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.id_post_moments_image_delete)
    public void delete(View view) {
//        selectMedia.clear();
        selectMediaPath.clear();

        ivDelete.setVisibility(View.GONE);
        Glide.with(PostMomentsActivity.this).load(R.mipmap.ic_add).into(ivImage);
    }

    @OnClick(R.id.id_post_moments_post)
    public void save(View view) {
        String strContent = tvContent.getText().toString().trim();

        if (TextUtils.isEmpty(strContent)) {
            Toasty.error(this, "请写入寄语哦").show();
            return;
        }

        if (strContent.length() > 41) {
            Toasty.error(this, "字数最多41个字哦").show();
            return;
        }

        upload(strContent);
    }

    private void send2Moments(String content, String url) {
        if (NetworkUtil.checkNetworkOnly(PostMomentsActivity.this)) {
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

                        AVObject saveAV = new AVObject(Constant.DB_MOMENTS);
                        saveAV.put("userId", imei);
                        saveAV.put("id", System.currentTimeMillis());
                        saveAV.put("content", content);
                        saveAV.put("url", url);
                        saveAV.put("date", DateFormatUtil.getCurrentDate(DateFormatUtil.sdfDateTime20));
                        saveAV.put("nickname", userName);

                        saveAV.put("info_manufacturer", strManufacturer);
                        saveAV.put("info_device", strDevice);
                        saveAV.put("info_barnd", strBrand);
                        saveAV.put("info_model", strModel);
                        saveAV.put("info_language", language);
                        saveAV.put("info_version", VersionUtil.getVersionName(PostMomentsActivity.this));
                        saveAV.put("info_system", strVersionRelease);
                        saveAV.put("info_platform", "android");

                        /**
                         * 保存数据
                         */
                        saveAV.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                loadingDialog.dismiss();

                                if (e == null) {
                                    // 存储成功
                                    Toasty.success(PostMomentsActivity.this, "成功发布到时光甜甜圈").show();

                                    EventBus.getDefault().post(new MessageEvent(800));

                                    PostMomentsActivity.this.finish();
                                } else {
                                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                                }
                            }
                        });
                    })
                    .onDenied(permissions -> {
                        new MaterialDialog.Builder(PostMomentsActivity.this)
                                .title("权限设置")
                                .negativeText("取消")
                                .positiveText("去设置")
                                .content("发布时光甜甜圈需要开启手机识别码权限(READ_PHONE_STATE)获取您的 IMEI 作为您的唯一ID")
                                .onPositive((dialog, which) -> {
                                    getAppDetailSettingIntent(PostMomentsActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    selectMedia = selectList;
                    if (selectMedia.size() != 0) {
                        for (LocalMedia localMedia : selectMedia) {
                            if (localMedia.isCompressed()) {
//                    // 注意：如果压缩过，在上传的时候，取 media.getCompressPath(); // 压缩图compressPath
                                selectMediaPath.add(localMedia.getCompressPath());
                            } else {
//                    // 注意：没有压缩过，在上传的时候，取 media.getPath(); // 原图path
                                selectMediaPath.add(localMedia.getPath());
                            }
                        }
//
                        ivDelete.setVisibility(View.VISIBLE);
                        Glide.with(PostMomentsActivity.this).load(selectMediaPath.get(0)).into(ivImage);
                    }
                    break;
            }
        }
    }

    @SuppressLint("MissingPermission")
    private String getImei() {
        String imei = "imei";
        try {
            final TelephonyManager manager = (TelephonyManager) PostMomentsActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
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
