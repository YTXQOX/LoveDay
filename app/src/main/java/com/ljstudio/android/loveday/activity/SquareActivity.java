package com.ljstudio.android.loveday.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.adapter.SquareAdapter;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.SquareData;
import com.ljstudio.android.loveday.entity.WelfareData;
import com.ljstudio.android.loveday.utils.SystemOutUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoren on 2018-6-5 14:26
 * Usage
 */

public class SquareActivity extends AppCompatActivity {

    public static final String IMEI = "IMEI";

    @BindView(R.id.id_square_toolbar)
    Toolbar toolBar;
    @BindView(R.id.id_square_list)
    RecyclerView rvSquare;

    private List<SquareData> squareDataList = new ArrayList<>();
    private SquareAdapter squareAdapter;
    private AlertDialog loadingDialog;

    private String strImei;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

        ButterKnife.bind(this);

        toolBar.setTitle(R.string.square);
        toolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolBar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolBar);

        toolBar.setNavigationOnClickListener(view -> SquareActivity.this.finish());

        strImei = getIntent().getStringExtra(IMEI);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(SquareActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvSquare.setLayoutManager(layoutManager1);
//        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this, VERTICAL_LIST, R.color.colorGrayLight));
//        rvWelfare.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        createLoadingDialog();

        initActualData(Constant.DB_SQUARE);
    }

    private void initActualData(String db) {
        loadingDialog.show();

        AVQuery<AVObject> query = new AVQuery<>(db);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null != list && 0 != list.size()) {
                    for (AVObject object : list) {
                        SquareData squareData1 = new SquareData();
                        squareData1.setObjectId(object.getString("objectId"));
                        squareData1.setUserId(object.getString("userId"));
                        squareData1.setId(object.getLong("id"));
                        squareData1.setTitle(object.getString("title"));
                        squareData1.setDate(object.getString("date"));
                        squareData1.setDays(object.getString("days"));
                        squareData1.setUnit(object.getString("unit"));
                        squareData1.setTop(object.getBoolean("isTop"));
                        squareData1.setNickname(object.getString("nickname"));

                        AVQuery<AVObject> query = new AVQuery<>(Constant.DB_SQUARE_LIKE);
                        query.whereEqualTo("id", squareData1.getId());
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

                                squareData1.setLikes(userIds);
                                squareData1.setLikeNum(likeNum);

                                SystemOutUtil.sysOut("squareData1-->" + squareData1.toString());
                                squareDataList.add(squareData1);

                                squareAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

//                Collections.sort(squareDataList, comp);
//                squareAdapter = new SquareAdapter(SquareActivity.this, squareDataList);
                squareAdapter = new SquareAdapter(SquareActivity.this, squareDataList, strImei);
//                squareAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//                squareAdapter.isFirstOnly(false);
                rvSquare.setAdapter(squareAdapter);
            }
        });
    }

    private void createLoadingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SquareActivity.this, R.style.AlertDialogStyle);
        final View dialogView = LayoutInflater.from(SquareActivity.this).inflate(R.layout.layout_loading_progress_dialog, null);
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
