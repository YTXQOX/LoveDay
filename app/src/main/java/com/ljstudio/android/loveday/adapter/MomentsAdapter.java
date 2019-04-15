package com.ljstudio.android.loveday.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.MomentsData;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.views.like.LikeNumView;
import com.ljstudio.android.loveday.views.like.LikeView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

/**
 * Created by guoren on 2018-6-26 16:57
 * Usage
 */
public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MomentsData> data;
    private String imei;


    public MomentsAdapter(Context context, List<MomentsData> listData, String imei) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.data = listData;
        this.imei = imei;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.layout_moments_item, viewGroup, false);

        MomentsAdapter.ViewHolder viewHolder = new MomentsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MomentsData itemData = data.get(i);
        SystemOutUtil.sysOut("imei-->" + imei);
        SystemOutUtil.sysOut("itemData-->" + itemData.toString());

//        String url = "https://i.loli.net/2018/11/09/5be526e0ef701.jpg";
        String url = itemData.getUrl();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.google_calendar_01)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(viewHolder.ivBig);

//        Glide.with(mContext).load(url).into(viewHolder.ivBig);

        viewHolder.tvContent.setText(itemData.getContent());
        viewHolder.tvName.setText(itemData.getNickname());
        viewHolder.tvDate.setText(itemData.getDate());
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
    private void saveLike(MomentsData squareData) {
        AVObject saveAV = new AVObject(Constant.DB_MOMENTS_LIKE);

        String ids = squareData.getLikes();
        StringBuilder buffer = new StringBuilder();
        if (!TextUtils.isEmpty(ids)) {
            buffer.append(",").append(ids);

            AVQuery<AVObject> deleteAV = new AVQuery<>(Constant.DB_MOMENTS_LIKE);
            deleteAV.whereEqualTo("id", squareData.getId());
            deleteAV.deleteAllInBackground(new DeleteCallback() {
                @Override
                public void done(AVException e) {

                }
            });
        }

        buffer.append(",").append(imei);
        String userIds = buffer.deleteCharAt(0).toString();
        SystemOutUtil.sysOut("saveLike()-->id-->" + squareData.getId());
        SystemOutUtil.sysOut("saveLike()-->userIds-->" + userIds);

        saveAV.put("id", squareData.getId());
        saveAV.put("userIds", userIds);

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
        private ImageView ivBig;
        private TextView tvContent;
        private TextView tvDate;
        private TextView tvName;
        private LikeView like;
        private LikeNumView likeNum;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;

            this.ivBig = rootView.findViewById(R.id.id_moments_item_image);
            this.tvContent = rootView.findViewById(R.id.id_moments_item_content);
            this.tvName = rootView.findViewById(R.id.id_moments_item_name);
            this.tvDate = rootView.findViewById(R.id.id_moments_item_date);
            this.like = rootView.findViewById(R.id.id_moments_item_like);
            this.likeNum = rootView.findViewById(R.id.id_moments_item_like_num);
        }
    }

}
