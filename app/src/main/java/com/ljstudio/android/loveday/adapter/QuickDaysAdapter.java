package com.ljstudio.android.loveday.adapter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;
import com.ljstudio.android.loveday.utils.PreferencesUtil;

import java.util.Date;
import java.util.List;

public class QuickDaysAdapter extends BaseQuickAdapter<DaysData, BaseViewHolder> {

    public QuickDaysAdapter(@LayoutRes int layoutResId, @Nullable List<DaysData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DaysData item) {

        helper.setText(R.id.id_days_item_title, item.getTitle());
        helper.setText(R.id.id_days_item_date, item.getDate());

        Date date = DateFormatUtil.convertStr2Date(item.getDate(), DateFormatUtil.sdfDate1);
        if (1 == DateUtil.compareDate(date, new Date())) {
            helper.setBackgroundColor(R.id.id_days_item_days_layout, mContext.getResources().getColor(R.color.colorBlue));
        } else {
            helper.setBackgroundColor(R.id.id_days_item_days_layout, mContext.getResources().getColor(R.color.colorAccent));
        }
        int days = DateUtil.betweenDays(date, new Date());
        helper.setText(R.id.id_days_item_days, String.valueOf(days));

        helper.setText(R.id.id_days_item_unit, item.getUnit());

        boolean isColorfulBg = PreferencesUtil.getPrefBoolean(mContext, Constant.COLORFUL_BG, false);
        if (isColorfulBg) {
            helper.setTextColor(R.id.id_days_item_title, ContextCompat.getColor(mContext, R.color.colorGrayLighter));
            helper.setTextColor(R.id.id_days_item_date, ContextCompat.getColor(mContext, R.color.colorGrayLighter));
        } else {
            helper.setTextColor(R.id.id_days_item_title, ContextCompat.getColor(mContext, R.color.colorGray));
            helper.setTextColor(R.id.id_days_item_date, ContextCompat.getColor(mContext, R.color.colorGray));
        }
    }
}