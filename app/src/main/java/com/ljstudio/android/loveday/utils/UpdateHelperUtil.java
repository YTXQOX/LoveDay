package com.ljstudio.android.loveday.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.api.ApiService;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.UpdateData;
import com.yanzhenjie.permission.AndPermission;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class UpdateHelperUtil {
    private static final int DEFAULT_TIMEOUT = 5;

    private static String FILE_NAME = "";

    private static Activity activity = null;
    private static UpdateHelperUtil updateHelperUtil = null;
    private NotificationManager nmDownload;
    private PendingIntent piDownload;

    private static final int UPDATE_NULL = 100;
    private static final int UPDATE_YES = 101;
    private static final int UPDATE_NO = 102;
    private static final int DOWNLOAD_ING = 103;
    private static final int DOWNLOAD_FAILED = 104;
    private static final int DOWNLOAD_SUCCESS = 105;
    private static final int NETWORK_ERROR = 199;

    private static int mAutoFinished = -1;

    private Dialog mDownloadDialog;
    private static NumberProgressBar progressBar;
    private MaterialDialog materialDialog;

    private RequestCall requestDownload;
    private MyHandler UIHandler;

    private boolean bUpdate = false;
    private static int mProgress;
    private VersionData versionData;

    private UpdateData updateData;
    private List<UpdateData> listData = new ArrayList<>();


    public UpdateHelperUtil(Activity mContext) {
        this.activity = mContext;
    }

    public static UpdateHelperUtil getInstance(Activity context) {

        if (updateHelperUtil == null) {
            updateHelperUtil = new UpdateHelperUtil(context);
        }

        return updateHelperUtil;
    }

    /**
     * UI 更新
     */
    private static class MyHandler extends Handler {
        private WeakReference<Activity> mActicity;

        private MyHandler(Activity activity) {
            mActicity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActicity.get();
            if (activity != null) {
                switch (msg.what) {
                    case UPDATE_YES:
//				aboutCustomerDialog();
                        break;
                    case UPDATE_NULL:
                        break;
                    case UPDATE_NO:
                        ToastUtil.toastShort(UpdateHelperUtil.activity, "当前版本已是最新");
                        break;
                    case DOWNLOAD_ING:
                        progressBar.setProgress(mProgress);
                        break;
                    case DOWNLOAD_FAILED:
                        ToastUtil.toastShort(UpdateHelperUtil.activity, "下载失败");
                        break;
                    case DOWNLOAD_SUCCESS:
                        installApk();
                        break;
                    case NETWORK_ERROR:
                        ToastUtil.toastShort(UpdateHelperUtil.activity, "网络连接出错");
                        break;
                }
            }
        }
    }

    /**
     * 检查是否更新
     * 是否弹出更新内容窗口  true or false
     */
    public boolean check4Update(final Context mContext, final boolean bPopup, final boolean bShowToast) {
        UIHandler = new MyHandler(activity);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl("https://app.qipai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        retrofit.create(ApiService.class).update("requestVersionUpgrade", "version")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionData>() {
                    Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(VersionData responseData) {
                        Log.i("ResponseData-->", responseData.toString());

                        if ("200".equals(responseData.getCode())) {
                            versionData = responseData;
                            int localVersionCode = (int) VersionUtil.getVersionCode(UpdateHelperUtil.activity);
                            if (Integer.valueOf(responseData.getData().getVersion_code()) > localVersionCode) {
                                bUpdate = true;

                                if (bPopup) {
                                    showUpdateDialog(mContext);
                                }
                            } else {
                                if (bShowToast) {
                                    UIHandler.sendEmptyMessage(UPDATE_NO);
                                } else {
                                    UIHandler.sendEmptyMessage(UPDATE_NULL);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return bUpdate;
    }

    public boolean check4UpdateLeanCloud(final Context mContext, final boolean bPopup, final String db, final boolean isToast) {
        updateData = null;
        listData.clear();

        UIHandler = new MyHandler(activity);

        AVQuery<AVObject> query = new AVQuery<>(db);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject object : list) {
                    UpdateData updateData1 = new UpdateData();
                    updateData1.setCode(object.getInt(Constant.CODE));
                    updateData1.setMessage(object.getString(Constant.MESSAGE));
                    updateData1.setApp_id(object.getInt(Constant.APP_ID_CODE));
                    updateData1.setVersion_code(object.getInt(Constant.VERSION_CODE));
                    updateData1.setVersion_name(object.getString(Constant.VERSION_NAME));
                    updateData1.setUpdate_description(object.getString(Constant.UPDATE_DESCRIPTION));
                    updateData1.setApk_url(object.getString(Constant.APK_URL));

                    if (object.getBoolean(Constant.IS_ONLINE) && 2 == object.getInt(Constant.APP_ID_CODE)) {
                        listData.add(updateData1);
                    }
                }

                if (listData.size() != 0) {
                    updateData = listData.get(0);
                    Log.i("updateData-->", updateData.toString());
                    if (200 == updateData.getCode()) {
                        int localVersionCode = (int) VersionUtil.getVersionCode(UpdateHelperUtil.activity);
                        if (updateData.getVersion_code() > localVersionCode) {
                            bUpdate = true;

                            if (bPopup) {
                                showUpdateDialog(mContext);
                            }
                        } else {
                            if (isToast) {
                                UIHandler.sendEmptyMessage(UPDATE_NO);
                            }
                        }
                    }
                } else {
                    if (isToast) {
                        UIHandler.sendEmptyMessage(UPDATE_NO);
                    }
                }
            }
        });

        return bUpdate;
    }

    /**
     * 提示更新信息
     */
    private void showUpdateDialog(final Context mContext) {
        materialDialog = new MaterialDialog.Builder(mContext)
                .title("新版本")
                .negativeText("取消")
                .positiveText("确定")
                .content(updateData.getUpdate_description())
                .onPositive((dialog, which) -> {
                    FILE_NAME = "时光机_" + System.currentTimeMillis() + ".apk";

//                File apkFile = FileUtil.getSDCardFolderPath(FILE_NAME);
//                if (apkFile.exists()) {
//                    installApk();
//                } else {
                    showDownloadDialog(mContext, FILE_NAME);
//                    downloadFile(FILE_NAME);
//                }

                    materialDialog.dismiss();
                })
                .onNegative((dialog, which) -> {
                    materialDialog.dismiss();
                })
                .show();

        new Handler().postDelayed(() -> {
            if (!((Activity) mContext).isFinishing()) {
                //show dialog
                materialDialog.show();
            }
        }, 100L);
    }

    /**
     * 下载对话框
     */
    private void showDownloadDialog(Context mContext, String fileName) {
        Builder builder = new Builder(mContext);
//		builder.setTitle(R.string.soft_updating);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_update_download, null);
        progressBar = view.findViewById(R.id.id_update_progress);
        TextView tvHint = view.findViewById(R.id.id_update_hint);
        tvHint.setVisibility(View.GONE);

        builder.setView(view);
        builder.setCancelable(false);
//        取消更新
        builder.setNegativeButton("取消", (dialog, which) -> {
            dialog.dismiss();

            // 取消下載
            if (null != requestDownload) {
                requestDownload.cancel();
            }
            UIHandler.sendEmptyMessage(DOWNLOAD_FAILED);
        });

        mDownloadDialog = builder.create();
        mDownloadDialog.show();

        downloadFile(fileName);
    }

    /**
     * 下载
     */
    private void downloadFile(final String fileName) {
        requestDownload = OkHttpUtils.get().url(updateData.getApk_url()).build();
//        String path = getSDCardFolderPath("Download").getAbsolutePath();
        String path = "/data/data/" + activity.getPackageName() + "/files/download/";
        requestDownload.execute(new FileCallBack(path, fileName) {
            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);

                // 通知栏显示
//                showNotification(2, activity.getResources().getString(R.string.app_name), progress, fileName);

                mProgress = (int) (100 * progress);
                UIHandler.sendEmptyMessage(DOWNLOAD_ING);
            }

            @Override
            public boolean validateReponse(Response response, int id) {
                return super.validateReponse(response, id);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                UIHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                mDownloadDialog.dismiss();
            }

            @Override
            public void onResponse(File response, int id) {
                UIHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
                mDownloadDialog.dismiss();
            }
        });
    }

    /**
     * 通知栏消息
     */
    private void showNotification(int notifyType, String strTitle, float progress, String fileName) {
        nmDownload = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
//		Intent notifyIntent = new Intent(activity, LoginActivity.class);
//		notifyIntent.putExtra("title", strTitle);
//		notifyIntent.putExtra("content", strTitle);

        int k = (int) (progress * 100);

        if (100 == k) {
            // 升级 intent
            Intent notifyIntent = new Intent();
            notifyIntent.setAction("android.intent.action.VIEW");
            notifyIntent.addCategory("android.intent.category.DEFAULT");
            File file = new File(getSDCardFolderPath("Download"), fileName);
            notifyIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//            activity.startActivity(notifyIntent);
//            activity.finish();

            piDownload = PendingIntent.getActivity(activity, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

//		-API level 16
//		noti = new Notification.Builder(activity)
//		.setContentTitle("新共享").setContentTitle(data)
//		.setSmallIcon(R.drawable.ic_launcher)
//		.build();

//		notifyUpload = new Notification();
//		notifyUpload.icon = R.drawable.icon;
//		notifyUpload.tickerText = "文管大师+";
//		notifyUpload.defaults |= Notification.DEFAULT_SOUND;
//		notifyUpload.defaults |= Notification.DEFAULT_VIBRATE;
//		notifyUpload.defaults |= Notification.DEFAULT_LIGHTS;
//		notifyUpload.flags |= Notification.FLAG_AUTO_CANCEL;
//		noti.flags |= Notification.FLAG_INSISTENT;
//		notifyUpload.setLatestEventInfo(Cloud2Baidu.this, strTitle, strTitle , piUpload);
//		nmUpload.notify(Notification_CLOUD_ID_UPLOAD, notifyUpload);

        //custom sytle
        Notification notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags = Notification.FLAG_NO_CLEAR;

        if (notifyType == 1) {
            notification.tickerText = "文件上传";
        }
        if (notifyType == 2) {
            notification.tickerText = "文件下载";
        }

        RemoteViews mRemoteViews = new RemoteViews(activity.getPackageName(), R.layout.layout_update_notification);
        mRemoteViews.setImageViewResource(R.id.id_notify_icon, R.mipmap.ic_launcher);
        mRemoteViews.setTextViewText(R.id.id_notify_msg_title, strTitle);
//        mRemoteViews.setTextViewText(R.id.id_notify_msg_ing_total, "(" + k + "%) " + strTotal + "M");
        mRemoteViews.setTextViewText(R.id.id_notify_msg_ing_total, "(" + k + "%)");
        mRemoteViews.setProgressBar(R.id.id_notify_msg_progressbar, 100, k, false);
        if (notifyType == 1) {
            if (k == 100 || mAutoFinished == 200) {
                mRemoteViews.setTextViewText(R.id.id_notify_msg_ing_status, "上传成功");
                notification.flags = Notification.FLAG_AUTO_CANCEL;
            } else {
                mRemoteViews.setTextViewText(R.id.id_notify_msg_ing_status, "正在上传...");
            }
        }
        if (notifyType == 2) {
            if (k == 100 || mAutoFinished == 200) {
                mRemoteViews.setTextViewText(R.id.id_notify_msg_ing_status, "下载完成,点击升级");
                notification.flags = Notification.FLAG_AUTO_CANCEL;
            } else {
                mRemoteViews.setTextViewText(R.id.id_notify_msg_ing_status, "正在下载...");
            }
        }

        notification.contentView = mRemoteViews;
        if (100 == k) {
            notification.contentIntent = piDownload;
        }
//        int id = (int) System.currentTimeMillis();
        int id = 1024;
        nmDownload.notify(id, notification);
    }

    /**
     * 安装APK文件
     */
    private static void installApk() {
//        String path = getSDCardFolderPath("Download");
        String path = "/data/data/" + activity.getPackageName() + "/files/download/";
        File apkFile = new File(path, FILE_NAME);
        if (!apkFile.exists()) {
            return;
        }

        AndPermission.with(activity)
                .install()
                .file(apkFile)
                .onGranted(file -> {
                    // App is allowed to install apps.
                    // 通过Intent安装APK文件
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                        Uri apkUri = FileProvider.getUriForFile(activity, "com.ljstudio.android.puzzleppg.provider.LJProvider", apkFile);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//                      intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                })
                .onDenied(file -> {
                    // App is refused to install apps.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startInstallPermissionSettingActivity();
                    } else {
                        Toasty.error(activity, "安装应用权限关闭").show();
                    }
                })
                .start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, 1);
    }

    private static File getSDCardFolderPath(String filePathName) {
        File folderFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "LJSTUDIO" + File.separator
                + "Android" + File.separator
                + "qipai" + File.separator
                + "iWork" + File.separator
                + filePathName + File.separator);
        return folderFile;
    }

}
