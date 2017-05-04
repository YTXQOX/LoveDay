package com.ljstudio.android.loveday.adapter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;

import java.util.Date;
import java.util.List;

public class QuickDaysAdapter extends BaseQuickAdapter<DaysData, BaseViewHolder> {

    public QuickDaysAdapter(@LayoutRes int layoutResId, @Nullable List<DaysData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DaysData item) {

        helper.setText(R.id.id_days_item_title, item.getTitle());

        Date date = DateFormatUtil.convertStr2Date(item.getDate(), DateFormatUtil.sdfDate1);
        if (1 == DateUtil.compareDate(date, new Date())) {
            helper.setBackgroundColor(R.id.id_days_item_days_layout, mContext.getResources().getColor(R.color.colorBlue));
        } else {
            helper.setBackgroundColor(R.id.id_days_item_days_layout, mContext.getResources().getColor(R.color.colorAccent));
        }
        int days = DateUtil.betweenDays(date, new Date());
        helper.setText(R.id.id_days_item_days, String.valueOf(days));

        helper.setText(R.id.id_days_item_unit, item.getUnit());
    }
}