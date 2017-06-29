package com.ljstudio.android.loveday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.entity.DaysData;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by guoren on 2017/2/27 17:11
 * Usage
 */

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<DaysData> listData;


    public DaysAdapter(Context context, List<DaysData> listData) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_item_days, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DaysAdapter.ViewHolder holder, int position) {
        final DaysData data = listData.get(position);

        holder.tvTitle.setText(data.getTitle());

        Date date = DateFormatUtil.convertStr2Date(data.getDate(), DateFormatUtil.sdfDate1);
        if (1 == DateUtil.compareDate(date, new Date())) {
            holder.layoutItemDays.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlue));
        } else {
            holder.layoutItemDays.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        int days = DateUtil.betweenDays(date, new Date());
        holder.tvDays.setText(String.valueOf(days));

        holder.tvUnit.setText(data.getUnit());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layoutItem;
        public TextView tvTitle;
        public LinearLayout layoutItemDays;
        public TextView tvDays;
        public TextView tvUnit;

        public ViewHolder(View itemView) {
            super(itemView);

            layoutItem = (RelativeLayout) itemView.findViewById(R.id.id_days_item_layout);
            tvTitle = (TextView) itemView.findViewById(R.id.id_days_item_title);
            layoutItemDays = (LinearLayout) itemView.findViewById(R.id.id_days_item_days_layout);
            tvDays = (TextView) itemView.findViewById(R.id.id_days_item_days);
            tvUnit = (TextView) itemView.findViewById(R.id.id_days_item_unit);
        }
    }
}
