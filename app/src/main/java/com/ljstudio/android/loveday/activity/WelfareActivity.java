package com.ljstudio.android.loveday.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.adapter.WelfareAdapter;
import com.ljstudio.android.loveday.entity.WelfareData;

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

public class WelfareActivity extends AppCompatActivity {

    @BindView(R.id.id_welfare_toolbar)
    Toolbar toolBar;
    @BindView(R.id.id_welfare_list)
    RecyclerView rvWelfare;

    private List<WelfareData> welfareDataList = new ArrayList<>();
    private WelfareAdapter welfareAdapter;
    private AlertDialog loadingDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare);

        ButterKnife.bind(this);

        toolBar.setTitle(R.string.welfare);
        toolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolBar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolBar);

        toolBar.setNavigationOnClickListener(view -> WelfareActivity.this.finish());

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(WelfareActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvWelfare.setLayoutManager(layoutManager1);
//        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this, VERTICAL_LIST, R.color.colorGrayLight));
//        rvWelfare.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        createLoadingDialog();

        initActualData("welfare_db");
    }

    private void initActualData(String db) {
        loadingDialog.show();

        AVQuery<AVObject> query = new AVQuery<>(db);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                loadingDialog.dismiss();

                for (AVObject object : list) {
                    WelfareData welfareData1 = new WelfareData();
                    welfareData1.setTitle(object.getString("welfare_title"));
                    welfareData1.setContent(object.getString("welfare_content"));
                    welfareData1.setSlogan(object.getString("welfare_slogan"));
                    welfareData1.setAction(object.getString("welfare_action"));
                    welfareData1.setUrl("");
                    welfareData1.setBelong(object.getString("welfare_belong"));
                    welfareData1.setOrder(object.getInt("welfare_order"));

                    welfareDataList.add(welfareData1);
                }

                Collections.sort(welfareDataList, comp);
                welfareAdapter = new WelfareAdapter(WelfareActivity.this, welfareDataList);
                welfareAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
                welfareAdapter.isFirstOnly(false);
                rvWelfare.setAdapter(welfareAdapter);
            }
        });
    }

    private void createLoadingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WelfareActivity.this, R.style.AlertDialogStyle);
        final View dialogView = LayoutInflater.from(WelfareActivity.this)
                .inflate(R.layout.layout_loading_progress_dialog, null);
        dialogBuilder.setView(dialogView);
        loadingDialog = dialogBuilder.create();
    }

    private Comparator comp = new Comparator() {
        public int compare(Object o1, Object o2) {
            WelfareData data1 = (WelfareData) o1;
            WelfareData data2 = (WelfareData) o2;

            if (data1.getOrder() > data2.getOrder()) {
                return 1;
            } else if (data1.getOrder() < data2.getOrder()) {
                return -1;
            } else {
                return 0;
            }
        }
    };

}
