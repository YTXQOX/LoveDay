package com.ljstudio.android.loveday.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ljstudio.android.loveday.BuildConfig;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.DetailData;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.views.fallingview.FallingView;
import com.ljstudio.android.loveday.views.fonts.FontsManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by guoren on 2017/10/7 10:01
 * Usage
 */

public class SwipeDetailAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DetailData> datas = new ArrayList<>();
    private int cardWidth;
    private int cardHeight;

    public SwipeDetailAdapter(Context context, List<DetailData> datas, int cardWidth, int cardHeight) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.datas = datas;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
    }

    public void addAll(Collection<DetailData> collection) {
        if (isEmpty()) {
            datas.addAll(collection);
            notifyDataSetChanged();
        } else {
            datas.addAll(collection);
        }
    }

    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return datas.isEmpty();
    }

    public void remove(int index) {
        if (index > -1 && index < datas.size()) {
            datas.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public DetailData getItem(int position) {
        if (datas == null || datas.size() == 0) return null;
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DetailData detailData = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_swipe_detail_item, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);

            convertView.getLayoutParams().width = cardWidth;
//            convertView.getLayoutParams().height = cardHeight;

            holder.tvTitle = (TextView) convertView.findViewById(R.id.id_swipe_detail_days_title);
            holder.layoutDays = (LinearLayout) convertView.findViewById(R.id.id_swipe_detail_days_layout);
            holder.tvDays = (TextView) convertView.findViewById(R.id.id_swipe_detail_days_days);
            holder.tvStart = (TextView) convertView.findViewById(R.id.id_swipe_detail_days_start);
//            holder.bgLayout = (RelativeLayout) convertView.findViewById(R.id.id_swipe_detail_layout);
            holder.fallingView = (FallingView) convertView.findViewById(R.id.id_swipe_detail_falling_view);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(detailData.getTitle());

        Date date = DateFormatUtil.convertStr2Date(detailData.getDate(), DateFormatUtil.sdfDate1);
        int days = DateUtil.betweenDays(date, new Date());
        holder.tvDays.setText(String.valueOf(days));

        FontsManager.initFormAssets(mContext, "fonts/gtw.ttf");
        FontsManager.changeFonts(holder.tvDays);

        if (1 == DateUtil.compareDate(date, new Date())) {
            holder.tvDays.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
            holder.tvStart.setText("目标日：" + detailData.getDate());
        } else {
            holder.tvDays.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.tvStart.setText("起始日：" + detailData.getDate());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        if (BuildConfig.LOG_DEBUG) {
            Log.i("DetailActivity", month + "");
        }

        int type = getSeason(month);
        setSeasonBg(holder, type);
        setMonthBg(holder, month);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            holder.tvDays.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Animator animator = ViewAnimationUtils.createCircularReveal(holder.tvDays,
//                                holder.tvDays.getWidth() / 2, holder.tvDays.getHeight() / 2, 0, holder.tvDays.getWidth());
//                        animator.setDuration(2222);
//                        animator.start();
//                    }
//                }
//            });
//        }

        holder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strContent;
                String strButton;
                String strInfo;
                boolean isColorfulBg = PreferencesUtil.getPrefBoolean(mContext, Constant.COLORFUL_BG, false);
                if (isColorfulBg) {
                    strContent = "即将关闭首页炫彩界面，嘤嘤嘤~~~";
                    strButton = "关闭";
                    strInfo = "已关闭炫彩界面，将在下次启动时生效";
                } else {
                    strContent = "即将开启首页炫彩界面，吼吼吼~~~";
                    strButton = "开启";
                    strInfo = "已开启炫彩界面，将在下次启动时生效";
                }

                showColorfulBg(isColorfulBg, strContent, strButton, strInfo);
            }
        });

        return convertView;
    }

    private int getSeason(int month) {
        int type;
        if (1 == month || 2 == month || 12 == month) {
            type = 4;   // winter
        } else if (3 == month || 4 == month || 5 == month) {
            type = 1;   // spring
        } else if (6 == month || 7 == month || 8 == month) {
            type = 2;   // summer
        } else if (9 == month || 10 == month || 11 == month) {
            type = 3;   // autumn
        } else {
            type = 1;   // default spring
        }

//        date.getMonth();
//        Random random = new Random();
//        type = random.nextInt(5);
        return type;
    }

    private void setSeasonBg(ViewHolder viewHolder, int type) {
        switch (type) {
            case 1:
//                viewHolder.bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                viewHolder.fallingView.setImageResource(R.mipmap.ic_spring_flower);
                break;
            case 2:
//                viewHolder.bgLayout.setBackgroundResource(R.mipmap.ic_summer);
                viewHolder.fallingView.setImageResource(R.mipmap.ic_summer_flower);
                break;
            case 3:
//                viewHolder.bgLayout.setBackgroundResource(R.mipmap.ic_autumn);
                viewHolder.fallingView.setImageResource(R.mipmap.ic_autumn_leaf);
                break;
            case 4:
//                viewHolder.bgLayout.setBackgroundResource(R.mipmap.ic_winter);
                viewHolder.fallingView.setImageResource(R.mipmap.ic_winter_snow);
                break;
            default:
//                viewHolder.bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                viewHolder.fallingView.setImageResource(R.mipmap.ic_spring_flower);
                break;
        }
    }

    private void setMonthBg(ViewHolder viewHolder, int month) {
        switch (month) {
            case 1:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_01);
                break;
            case 2:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_02);
                break;
            case 3:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_03);
                break;
            case 4:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_04);
                break;
            case 5:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_05);
                break;
            case 6:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_06);
                break;
            case 7:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_07);
                break;
            case 8:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_08);
                break;
            case 9:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_09);
                break;
            case 10:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_10);
                break;
            case 11:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_11);
                break;
            case 12:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_12);
                break;
            default:
                viewHolder.layoutDays.setBackgroundResource(R.mipmap.google_calendar_01);
                break;
        }
    }

    private void showColorfulBg(final boolean isColorfulBg, String content, String btn, final String info) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        builder.title("我是第二彩蛋");
        builder.content(content);
        builder.positiveText(btn);
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                PreferencesUtil.setPrefBoolean(mContext, Constant.COLORFUL_BG, !isColorfulBg);
                dialog.dismiss();

                Toasty.info(mContext, info).show();
            }
        });

        builder.build().show();
    }

    public static class ViewHolder {
        public TextView tvTitle;
        public LinearLayout layoutDays;
        public TextView tvDays;
        public TextView tvStart;
//        public RelativeLayout bgLayout;
        public FallingView fallingView;
    }
}
