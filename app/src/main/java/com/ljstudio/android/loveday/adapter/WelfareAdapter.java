package com.ljstudio.android.loveday.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.WelfareData;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoren on 2018-6-26 16:57
 * Usage
 */
public class WelfareAdapter extends BaseQuickAdapter<WelfareData, WelfareAdapter.ViewHolder> {

    private List<WelfareData> data = new ArrayList<>();
    private Context context;


    public WelfareAdapter(Context context, List<WelfareData> data) {
        super(R.layout.layout_welfare_item, data);

        this.context = context;
        this.data = data;
    }

    public WelfareAdapter(Context context, List<WelfareData> data, int layoutResId) {
        super(R.layout.layout_welfare_item, data);

        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(ViewHolder viewHolder, WelfareData item) {
        viewHolder.tvTitle.setText(item.getTitle());
        viewHolder.tvContent.setText(item.getSlogan());
        viewHolder.tvAction.setText(item.getAction());

        viewHolder.tvAction.setOnClickListener(v -> {
            if (isApkInstalled(context, Constant.ALIPAY_APP)) {
                Log.i("copy-->", item.getContent());
                copy(context, item.getContent());

                open3APP(context, Constant.ALIPAY_APP);

//                Intent intent = new Intent();
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                ComponentName comp = new ComponentName(Constant.ALIPAY_APP, "com.tencent.mm.ui.tools.ShareImgUI");
//                intent.setComponent(comp);
//                context.startActivity(intent);
            } else {
                Alerter.create((Activity) context)
                        .setText("未安装支付宝户端")
                        .setBackgroundColor(R.color.colorAccent)
                        .setDuration(800)
                        .show();
            }
        });
    }

    private void copy(Context context, String content) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (!TextUtils.isEmpty(content)) {
            cmb.setPrimaryClip(ClipData.newPlainText("love_day-copy", content));
        }
    }

    private String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getPrimaryClip().toString();
    }

    private boolean open3APP(Context context, String packageName) {
        Context pkgContext = getPackageContext(context, packageName);
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (pkgContext != null && intent != null) {
            pkgContext.startActivity(intent);
            return true;
        }
        return false;
    }

    private Context getPackageContext(Context context, String packageName) {
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境
            try {
                pkgContext = context.createPackageContext(packageName,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    private Intent getAppOpenIntentByPackageName(Context context, String packageName) {
        // MainActivity完整名
        String mainAct = null;
        // 根据包名寻找MainActivity
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

        List<ResolveInfo> list = pkgMag.queryIntentActivities(intent, 0);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                Log.i("packageName-->", info.activityInfo.name);
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
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

    public static class ViewHolder extends BaseViewHolder {
        private View rootView;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvAction;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;

            this.tvTitle = rootView.findViewById(R.id.id_welfare_item_title);
            this.tvContent = rootView.findViewById(R.id.id_welfare_item_content);
            this.tvAction = rootView.findViewById(R.id.id_welfare_item_action);
        }
    }

}
