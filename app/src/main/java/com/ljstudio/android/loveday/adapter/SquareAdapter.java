package com.ljstudio.android.loveday.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.SquareData;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.views.LabelView;
import com.ljstudio.android.loveday.views.like.LikeNumView;
import com.ljstudio.android.loveday.views.like.LikeView;

import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by guoren on 2018-6-26 16:57
 * Usage
 */
public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SquareData> data;
    private String imei;


    public SquareAdapter(Context context, List<SquareData> listData, String imei) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.data = listData;
        this.imei = imei;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.layout_square_item, viewGroup, false);

        SquareAdapter.ViewHolder viewHolder = new SquareAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SquareData itemData = data.get(i);

        viewHolder.tvName.setText(itemData.getNickname());
        viewHolder.tvTitle.setText(itemData.getTitle());

        Date date = DateFormatUtil.convertStr2Date(itemData.getDate(), DateFormatUtil.sdfDate1);
        String dayHint = "还有 ";
        String dateHint = "目标日：";

        int days = DateUtil.betweenDays(date, new Date());
        if (1 == DateUtil.compareDate(date, new Date())) {
            viewHolder.tvDays.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
            viewHolder.tvDate.setTextColor(mContext.getResources().getColor(R.color.colorBlue));

            dayHint = "还有 ";
            dateHint = "目标日：";
        } else {
            viewHolder.tvDays.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            viewHolder.tvDate.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

            dayHint = "已过去 ";
            dateHint = "起始日：";
        }

        viewHolder.tvDays.setText(dayHint + String.valueOf(days) + "天");
        viewHolder.tvDate.setText(dateHint + itemData.getDate());

        viewHolder.likeNum.setNum(itemData.getLikeNum());

        if (itemData.getLikes().contains(imei)) {
            viewHolder.like.setCheckedWithoutAnimator(true);
            viewHolder.like.setEnabled(false);
        } else {
            viewHolder.like.setOnClickListener(v -> {
//                viewHolder.like.toggle();
                viewHolder.like.setChecked(true);
                viewHolder.like.setEnabled(false);

//            viewHolder.likeNum.setNum(num + 1);
                viewHolder.likeNum.changeLike(false);

                saveLike(itemData);
            });
        }
    }

    /**
     * 保存数据
     */
    private void saveLike(SquareData squareData) {
        AVObject saveAV = new AVObject(Constant.DB_SQUARE_LIKE);

        String ids = squareData.getLikes();
        StringBuilder buffer = new StringBuilder();
        if (!TextUtils.isEmpty(ids)) {
            buffer.append(",").append(ids);

            AVQuery<AVObject> deleteAV = new AVQuery<>(Constant.DB_SQUARE_LIKE);
            deleteAV.whereEqualTo("id", squareData.getId());
            deleteAV.deleteAllInBackground(new DeleteCallback() {
                @Override
                public void done(AVException e) {

                }
            });
        }

        buffer.append(",").append(imei);
        String userIds = buffer.deleteCharAt(0).toString();
        SystemOutUtil.sysOut("saveLike()-->userIds-->" + userIds);

        saveAV.put("id", squareData.getId());
        saveAV.put("userIds", userIds);
//        saveAV.put("userIds", Arrays.asList(imei));

        saveAV.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 存储成功
                    Toasty.success(mContext, "点赞成功").show();
                } else {
                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                    Toasty.error(mContext, "点赞失败").show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends BaseViewHolder {
        private View rootView;
        private LabelView tvName;
        private TextView tvTitle;
        private TextView tvDays;
        private TextView tvDate;
        private LikeView like;
        private LikeNumView likeNum;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;

            this.tvName = rootView.findViewById(R.id.id_square_item_name);
            this.tvTitle = rootView.findViewById(R.id.id_square_item_title);
            this.tvDays = rootView.findViewById(R.id.id_square_item_days);
            this.tvDate = rootView.findViewById(R.id.id_square_item_date);
            this.like = rootView.findViewById(R.id.id_square_item_like);
            this.likeNum = rootView.findViewById(R.id.id_square_item_like_num);
        }
    }

}
