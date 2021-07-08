package com.ljstudio.android.loveday.activity;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.adapter.AppsManagerAdapter;
import com.ljstudio.android.loveday.adapter.DividerGridItemDecoration;
import com.ljstudio.android.loveday.entity.AppsData;
import com.ljstudio.android.loveday.utils.FileUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.views.loading.LoadingView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AppsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private LoadingView mLoadingView;

    private List<AppsData> mListAppInfo = new ArrayList<>();
    private AppsManagerAdapter adapter;
    private String signNumber;

    private float fDataSize;
    private float fCacheSize;
    private float fCodeSize;
    private float fTotalSize;

    private boolean isShowSysApps = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        setStatusBarColor(R.color.colorPrimary);

        initView();

        mToolbar.setTitle(R.string.apps);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(onMenuItemClick);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppsActivity.this.finish();
            }
        });

        adapter = new AppsManagerAdapter(mListAppInfo);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(AppsActivity.this, R.drawable.divider));
        mRecyclerView.setAdapter(adapter);

        mLoadingView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        Observable<List<AppsData>> observable = Observable.create(new ObservableOnSubscribe<List<AppsData>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppsData>> appInfo) throws Exception {
//                appInfo.onNext(queryAppInfo());
                appInfo.onNext(getPackageInfo(isShowSysApps));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Observer<List<AppsData>> observer = new Observer<List<AppsData>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            //观察者接收到通知,进行相关操作
            public void onNext(List<AppsData> aLong) {
                adapter.notifyDataSetChanged();

                mLoadingView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mLoadingView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        };

        observable.subscribe(observer);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.id_item_copy) {
                    onClickCopy(mListAppInfo.get(position));
                } else if (view.getId() == R.id.id_item_open) {
                    onClickOpen(mListAppInfo.get(position));
                }
            }
        });
    }

    /**
     * // 需要授予 【读取应用列表】权限
     *
     * @param bSystem
     * @return
     */
    private List<AppsData> getPackageInfo(boolean bSystem) {
        List<PackageInfo> packages = AppsActivity.this.getPackageManager().getInstalledPackages(0);
        SystemOutUtil.sysOut("getPackageInfo()==>packages.size()==>" + packages.size());

        if (null != mListAppInfo) {
            mListAppInfo.clear();

            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);

                AppsData tempInfo = new AppsData();
                tempInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
                tempInfo.setAppLabel(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                tempInfo.setPkgName(packageInfo.packageName);
                tempInfo.setmVersion(getVersionName(packageInfo.packageName));
                tempInfo.setSigmd5(getSignMd5Str(packageInfo.packageName));

                //Only display the non-system app info
                if (bSystem) {
                    mListAppInfo.add(tempInfo);
                } else {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        mListAppInfo.add(tempInfo);      // 安装的应用
                    } else {
                        // 系统应用
                    }
                }
            }

            SystemOutUtil.sysOut("getPackageInfo()==>mListAppInfo.size()==>" + mListAppInfo.size());
            return mListAppInfo;
        }
        return null;
    }

    /**
     * // 无需授予任何权限
     *
     * @return
     */
    public List<AppsData> queryAppInfo() {
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(mainIntent, 0);
        // 调用系统排序,根据name排序. 该排序很重要,否则只能显示系统应用,而不能列出第三方应用程序
        Collections.sort(resolveInfo, new ResolveInfo.DisplayNameComparator(pm));
        if (mListAppInfo != null) {
            mListAppInfo.clear();
            for (ResolveInfo reInfo : resolveInfo) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名

                String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName, activityName));
                // 创建一个AppInfo对象，并赋值
                AppsData appInfo = new AppsData();
                try {
                    queryPackageSize(pkgName, appInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                appInfo.setAppLabel(appLabel);
                appInfo.setPkgName(pkgName);
                appInfo.setAppIcon(icon);
                appInfo.setmVersion(getVersionName(pkgName));
                appInfo.setSigmd5(getSignMd5Str(pkgName));
//                appInfo.setIntent(launchIntent);
                mListAppInfo.add(appInfo); // 添加至列表中
            }
            return mListAppInfo;
        }
        return null;
    }

    public void onClickCopy(AppsData appInfo) {
        ClipboardManager var2 = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        StringBuilder var3 = new StringBuilder("应用程序：");
        String var4 = appInfo.getAppLabel();
        var3 = var3.append(var4).append("\n").append("包名：");
        var4 = appInfo.getPkgName();
        var3 = var3.append(var4).append("\n").append("签名：");
        var4 = appInfo.getSigmd5();
        String var5 = var3.append(var4).toString();
        var2.setText(var5);
        Toast.makeText(AppsActivity.this, "应用信息复制成功", Toast.LENGTH_SHORT).show();
    }

    public void onClickOpen(AppsData appInfo) {
        Intent intent = AppsActivity.this.getPackageManager().getLaunchIntentForPackage(appInfo.getPkgName());
        if (intent == null) {
            Toast.makeText(AppsActivity.this, "APP not found!", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.id_recycler_view);
        mToolbar = findViewById(R.id.id_apps_toolbar);
        mLoadingView = findViewById(R.id.id_loading_view);
    }

    private String getVersionName(String packageName) {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = "Version Name: " + packInfo.versionName + "\n" + "Version Code: " + packInfo.versionCode;
        return version != null ? version : "未获取到系统版本号";
    }

    /**
     * MD5加密
     *
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    /**
     * 获取app签名md5值
     */
    public String getSignMd5Str(String packageName) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = encryptionMD5(sign.toByteArray());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void queryPackageSize(String pkgName, AppsData appInfo) throws Exception {
        if (pkgName != null) {
            //使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
            PackageManager pm = getPackageManager();  //得到pm对象
            try {
                //通过反射机制获得该隐藏函数
//                Method getPackageSizeInfo = pm.getClass().getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
//                //调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
//                getPackageSizeInfo.invoke(pm, pkgName, new PkgSizeObserver(appInfo));
                Method getPackageSizeInfo = pm.getClass().getDeclaredMethod("getPackageSizeInfo", String.class,
                        int.class, IPackageStatsObserver.class);
                /**
                 * after invoking, PkgSizeObserver.onGetStatsCompleted() will be called as callback function. <br>
                 * About the third parameter ‘Process.myUid() / 100000’，please check:
                 * <android_source>/frameworks/base/core/java/android/content/pm/PackageManager.java:
                 * getPackageSizeInfo(packageName, UserHandle.myUserId(), observer);
                 */
                getPackageSizeInfo.invoke(pm, pkgName, Process.myUid() / 100000, new PkgSizeObserver(appInfo));
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;  // 抛出异常
            }
        }
    }

    //  aidl文件形成的Bindler机制服务类
    public class PkgSizeObserver extends IPackageStatsObserver.Stub {
        private AppsData appInfo;

        public PkgSizeObserver(AppsData appInfo) {
            this.appInfo = appInfo;
        }

        /*** 回调函数，
         * @param pStats ,返回数据封装在PackageStats对象中
         * @param succeeded  代表回调成功
         */
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                throws RemoteException {
            fCacheSize = pStats.cacheSize; //缓存大小
            fDataSize = pStats.dataSize;  //数据大小
            fCodeSize = pStats.codeSize;  //应用程序大小
            fTotalSize = fCacheSize + fDataSize + fCodeSize;
            appInfo.setTotalSize("缓存：" + FileUtil.getFileSize((long) fCacheSize) + "\n" +
                    "数据：" + FileUtil.getFileSize((long) fDataSize) + "\n" +
                    "应用程序：" + FileUtil.getFileSize((long) fCodeSize));
            Log.i("PkgSizeObserver", "fCacheSize-->" + fCacheSize + " fDataSize-->" + fDataSize + " fCodeSize-->" + fCodeSize);
        }
    }

    //系统函数，字符串转换 long -String (kb)
    private String formateFileSize(long size) {
        return Formatter.formatFileSize(AppsActivity.this, size);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isShowSysApps) {
            menu.findItem(R.id.id_action_show_sys_apps).setVisible(true);
            menu.findItem(R.id.id_action_hidden_sys_apps).setVisible(false);
        } else {
            menu.findItem(R.id.id_action_show_sys_apps).setVisible(false);
            menu.findItem(R.id.id_action_hidden_sys_apps).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AppsActivity.this.getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.id_action_show_sys_apps) {
                if (isShowSysApps) {
                    isShowSysApps = false;
                }
            } else if (id == R.id.id_action_hidden_sys_apps) {
                if (!isShowSysApps) {
                    isShowSysApps = true;
                }
            }

            invalidateOptionsMenu();

//            Toasty.info(AppsActivity.this, "isShowSysApps==>" + isShowSysApps).show();
            getPackageInfo(isShowSysApps);
            adapter.notifyDataSetChanged();

            return true;
        }
    };

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




