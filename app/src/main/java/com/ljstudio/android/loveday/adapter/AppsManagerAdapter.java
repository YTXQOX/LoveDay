package com.ljstudio.android.loveday.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.entity.AppsData;

import java.util.List;


public class AppsManagerAdapter extends BaseQuickAdapter<AppsData, AppsManagerAdapter.ViewHolder> {

    public AppsManagerAdapter(List<AppsData> data) {
        super(R.layout.layout_app_item, data);
    }

    public AppsManagerAdapter(int layoutResId, List<AppsData> data) {
        super(R.layout.layout_app_item, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, AppsData item) {
        viewHolder.addOnClickListener(R.id.id_item_copy);
        viewHolder.addOnClickListener(R.id.id_item_open);

        viewHolder.imgApp.setImageDrawable(item.getAppIcon());
        viewHolder.apkName.setText(item.getAppLabel());
        viewHolder.pkgName.setText("包名: " + item.getPkgName());
        viewHolder.apkSize.setText(item.getTotalSize());
        viewHolder.apkVersion.setText(item.getmVersion());
        viewHolder.sigmd5.setText("MD5: " + item.getSigmd5());
    }

    public static class ViewHolder extends BaseViewHolder {
        private View rootView;
        private ImageView imgApp;
        private TextView tvCopy;
        private TextView tvOpen;
        private TextView apkName;
        private TextView pkgName;
        private TextView apkSize;
        private TextView apkVersion;
        private TextView sigmd5;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.imgApp = (ImageView) rootView.findViewById(R.id.id_item_app_image);
            this.tvCopy = (TextView) rootView.findViewById(R.id.id_item_copy);
            this.tvOpen = (TextView) rootView.findViewById(R.id.id_item_open);
            this.apkName = (TextView) rootView.findViewById(R.id.apkName);
            this.pkgName = (TextView) rootView.findViewById(R.id.pkgName);
            this.apkSize = (TextView) rootView.findViewById(R.id.apkSize);
            this.apkVersion = (TextView) rootView.findViewById(R.id.apkVersion);
            this.sigmd5 = (TextView) rootView.findViewById(R.id.sigmd5);
        }
    }

}
