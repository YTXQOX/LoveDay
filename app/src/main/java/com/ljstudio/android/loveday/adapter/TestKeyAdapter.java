package com.ljstudio.android.loveday.adapter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.entity.TestKeyData;

import java.util.List;

public class TestKeyAdapter extends BaseQuickAdapter<TestKeyData, BaseViewHolder> {

    private List<Integer> listDate;
    private List<Integer> listNum;

    public TestKeyAdapter(@LayoutRes int layoutResId, @Nullable List<TestKeyData> data, List<Integer> listDate, List<Integer> listNum) {
        super(layoutResId, data);
        this.listDate = listDate;
        this.listNum = listNum;
    }

    @Override
    protected void convert(BaseViewHolder helper, TestKeyData item) {
        helper.setText(R.id.id_test_item_title, item.getName());
        helper.setText(R.id.id_test_item_content, item.getList_name());
    }

}