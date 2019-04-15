package com.ljstudio.android.loveday.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.adapter.MomentsAdapter;
import com.ljstudio.android.loveday.adapter.SpaceItemDecoration;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.MomentsData;
import com.ljstudio.android.loveday.eventbus.MessageEvent;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.DateUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoren on 2018-6-5 14:26
 * Usage
 */

public class MomentsActivity extends AppCompatActivity {

    public static final String IMEI = "IMEI";

    @BindView(R.id.id_moments_toolbar)
    Toolbar toolBar;
    @BindView(R.id.id_moments_list)
    RecyclerView rvMoments;

    private List<MomentsData> momentsDataList = new ArrayList<>();
    private MomentsAdapter momentsAdapter;
    private AlertDialog loadingDialog;

    private String strImei;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);

        ButterKnife.bind(this);

        toolBar.setTitle(R.string.moments);
        toolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolBar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolBar);
        toolBar.setOnMenuItemClickListener(onMenuItemClick);

        toolBar.setNavigationOnClickListener(view -> MomentsActivity.this.finish());

        strImei = getIntent().getStringExtra(IMEI);

        EventBus.getDefault().register(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(MomentsActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);

        StaggeredGridLayoutManager staggeredGridLayoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rvMoments.setLayoutManager(staggeredGridLayoutManager1);
//        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this, VERTICAL_LIST, R.color.colorGrayLight));
//        rvWelfare.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        SpaceItemDecoration decoration = new SpaceItemDecoration(16);
        rvMoments.addItemDecoration(decoration);

        createLoadingDialog();

        initActualData(Constant.DB_MOMENTS);
    }

    private void initActualData(String db) {
        momentsDataList.clear();
        loadingDialog.show();

        AVQuery<AVObject> query = new AVQuery<>(db);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null != list && 0 != list.size()) {
                    for (AVObject object : list) {
                        SystemOutUtil.sysOut("object-->" + object.toString());

                        MomentsData momentsData1 = new MomentsData();
                        momentsData1.setObjectId(object.getString("objectId"));
                        momentsData1.setUserId(object.getString("userId"));
                        momentsData1.setId(object.getLong("id"));
                        momentsData1.setUrl(object.getString("url"));
                        momentsData1.setContent(object.getString("content"));
                        momentsData1.setNickname(object.getString("nickname"));
                        momentsData1.setDate(object.getString("date"));

                        AVQuery<AVObject> query = new AVQuery<>(Constant.DB_MOMENTS_LIKE);
                        query.whereEqualTo("id", momentsData1.getId());
                        query.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                loadingDialog.dismiss();

                                String userIds = "";
                                int likeNum = 0;
                                if (null != list && 0 != list.size()) {
                                    userIds = list.get(0).getString("userIds");
                                    if (!TextUtils.isEmpty(userIds)) {
                                        String[] splitUserIds = userIds.split(",");
                                        likeNum = splitUserIds.length;
                                    }
                                }

                                momentsData1.setLikes(userIds);
                                momentsData1.setLikeNum(likeNum);

                                SystemOutUtil.sysOut("momentsData1-->" + momentsData1.toString());
                                momentsDataList.add(momentsData1);

                                momentsAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

                Collections.sort(momentsDataList, comp);
//                momentsAdapter = new SquareAdapter(SquareActivity.this, momentsDataList);
                momentsAdapter = new MomentsAdapter(MomentsActivity.this, momentsDataList, strImei);
//                momentsAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//                momentsAdapter.isFirstOnly(false);
                rvMoments.setAdapter(momentsAdapter);
            }
        });
    }

    private void createLoadingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MomentsActivity.this, R.style.AlertDialogStyle);
        final View dialogView = LayoutInflater.from(MomentsActivity.this).inflate(R.layout.layout_loading_progress_dialog, null);
        dialogBuilder.setView(dialogView);
        loadingDialog = dialogBuilder.create();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MomentsActivity.this.getMenuInflater().inflate(R.menu.moments_menu, menu);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (800 == event.message) {
            initActualData(Constant.DB_MOMENTS);
        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = item -> {
        int id = item.getItemId();
        if (id == R.id.id_action_moments) {
            Intent intent = new Intent();
            intent.setClass(MomentsActivity.this, PostMomentsActivity.class);
            startActivity(intent);
        }

        return true;
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private Comparator comp = (o1, o2) -> {
        MomentsData data1 = (MomentsData) o1;
        MomentsData data2 = (MomentsData) o2;

        return DateUtil.compareDate(DateFormatUtil.convertStr2Date(data1.getDate(), DateFormatUtil.sdfDateTime20),
                DateFormatUtil.convertStr2Date(data2.getDate(), DateFormatUtil.sdfDateTime20));

//        if (data1.getDate() > data2.getDate()) {
//            return 1;
//        } else if (data1.getDate() < data2.getDate()) {
//            return -1;
//        } else {
//            return 0;
//        }
    };

}
