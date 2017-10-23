package com.ljstudio.android.loveday.test;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.adapter.TestKeyAdapter;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.TestData;
import com.ljstudio.android.loveday.entity.TestKeyData;
import com.ljstudio.android.loveday.greendao.TestDataDao;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoren on 2017/08/07 15:19
 * Usage
 */

public class TestActivity extends AppCompatActivity {

    public static final String RESULT = "result";
    public static final String DATA = "data";
    public static final String EXTEND = "extend";
    public static final String CLASS_LIST = "class_list";
    public static final String VERSION_LIST = "version_list";
    public static final String NODE_LIST = "node_list";
    public static final String YEAR_LIST = "year_list";
    public static final String MONTH_LIST = "month_list";
    public static final String NUM_LIST = "num_list";

    @BindView((R.id.id_test_recycler_view))
    RecyclerView recyclerView;

    private TestKeyAdapter testKeyAdapter;

    private List<List<TestData>> listItems = new ArrayList<>();
    private List<String> listStrListName = new ArrayList<>();
    private List<String> listValue = new ArrayList<>();

    /**
     * RecyclerView Data
     */
    private List<TestKeyData> listKey = new ArrayList<>();
    private List<TestKeyData> tempListKey = new ArrayList<>();
    private String strDateKey;
    private String strNumKey;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

//        if (checkIsInit()) {
        initData();
//        }
        initView();

//        textView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<String> list = new ArrayList<>();
//                for (TestData testData : listItems1) {
//                    list.add(testData.getName());
//                }
//                final String[] items = list.toArray(new String[list.size()]);
//
//                Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
//                        setItems(items, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                textView1.setText(items[which]);
//
//                                value1 = readOne4DB(items[which]).get(0).getValue();
//                                listItems2 = readOne4DB(value1, CLASS_LIST);
//
//                                initVersionView(value1, CLASS_LIST);
//                            }
//                        }).create();
//
//                alertDialog.show();
//            }
//        });
//
//        textView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<String> list = new ArrayList<>();
//                for (TestData testData : listItems2) {
//                    list.add(testData.getName());
//                }
//                final String[] items = list.toArray(new String[list.size()]);
//
//                Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
//                        setItems(items, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                textView2.setText(items[which]);
//
//                                value2 = readOne4DB(items[which]).get(0).getValue();
//                                listItems3 = readOne4DB(value2, VERSION_LIST);
//
//                                initNodeView(value2, VERSION_LIST);
//                            }
//                        }).create();
//
//                alertDialog.show();
//            }
//        });
//
//        textView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<String> list = new ArrayList<>();
//                for (TestData testData : listItems3) {
//                    list.add(testData.getName());
//                }
//                final String[] items = list.toArray(new String[list.size()]);
//
//                Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
//                        setItems(items, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                textView3.setText(items[which]);
//                            }
//                        }).create();
//
//                alertDialog.show();
//            }
//        });
    }

    private void initData() {
        String str = "{\n" +
                "    \"code\": 1000,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"result\": {\n" +
                "        \"data\": [\n" +
                "            {\n" +
                "                \"name\": \"类型\",\n" +
                "                \"request_key\": \"class_id\",\n" +
                "                \"list_name\": \"class_list\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"版本\",\n" +
                "                \"request_key\": \"version_name\",\n" +
                "                \"list_name\": \"version_list\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"节点\",\n" +
                "                \"request_key\": \"node_id\",\n" +
                "                \"list_name\": \"node_list\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"年限\",\n" +
                "                \"request_key\": \"time_long\",\n" +
                "                \"list_name\": \"year_list\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"数量\",\n" +
                "                \"request_key\": \"product_num\",\n" +
                "                \"list_name\": \"num_list\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"extend\": {\n" +
                "            \"class_list\": [\n" +
                "                {\n" +
                "                    \"value\": \"1\",\n" +
                "                    \"name\": \"企业网站模板\",\n" +
                "                    \"fid\": \"\",\n" +
                "                    \"father_node\": \"\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"2\",\n" +
                "                    \"name\": \"企业定制官网\",\n" +
                "                    \"fid\": \"\",\n" +
                "                    \"father_node\": \"\",\n" +
                "                    \"recommend\": 0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"version_list\": [\n" +
                "                {\n" +
                "                    \"value\": \"尊贵版\",\n" +
                "                    \"name\": \"尊贵版\",\n" +
                "                    \"fid\": \"2\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"创业版\",\n" +
                "                    \"name\": \"创业版\",\n" +
                "                    \"fid\": \"1\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"高级版\",\n" +
                "                    \"name\": \"高级版\",\n" +
                "                    \"fid\": \"2\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"标准版\",\n" +
                "                    \"name\": \"标准版\",\n" +
                "                    \"fid\": \"2\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"中小企业版\",\n" +
                "                    \"name\": \"中小企业版\",\n" +
                "                    \"fid\": \"1\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"node_list\": [\n" +
                "                {\n" +
                "                    \"value\": \"3\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"尊贵版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"4\",\n" +
                "                    \"name\": \"国内\",\n" +
                "                    \"fid\": \"创业版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"3\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"高级版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"4\",\n" +
                "                    \"name\": \"国内\",\n" +
                "                    \"fid\": \"标准版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"3\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"创业版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"4\",\n" +
                "                    \"name\": \"国内\",\n" +
                "                    \"fid\": \"尊贵版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"3\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"标准版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"3\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"中小企业版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                }\n" +
                "            ],\n" +
                "            \"year_list\": [\n" +
                "                1,\n" +
                "                2,\n" +
                "                3,\n" +
                "                4,\n" +
                "                5,\n" +
                "                6,\n" +
                "                7,\n" +
                "                8,\n" +
                "                9,\n" +
                "                10\n" +
                "            ],\n" +
                "            \"num_list\": [\n" +
                "                1,\n" +
                "                2,\n" +
                "                3,\n" +
                "                4,\n" +
                "                5,\n" +
                "                6,\n" +
                "                7,\n" +
                "                8,\n" +
                "                9,\n" +
                "                10\n" +
                "            ],\n" +
                "            \"contract_info\": [\n" +
                "                {\n" +
                "                    \"name\": \"服务合同\",\n" +
                "                    \"url\": \"http://czx.jzcloud.xmisp.com/downs/网站建设产品服务合同 （云建站3.0版-未含设计服务）- 电子版.doc\",\n" +
                "                    \"fid\": \"1\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"服务合同\",\n" +
                "                    \"url\": \"http://czx.jzcloud.xmisp.com/downs/网站建设产品服务合同 （云建站3.0版-含设计服务）- 电子版.doc\",\n" +
                "                    \"fid\": \"2\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        \"versioninfo\": {}\n" +
                "    }\n" +
                "}";

        try {
            JSONObject jsonObject = new JSONObject(str);

            String strData = jsonObject.optJSONObject(RESULT).optString(DATA);
            Gson gson = new Gson();
            listKey = gson.fromJson(strData, new TypeToken<List<TestKeyData>>() {
            }.getType());

            tempListKey = new ArrayList<>();
            List<TestKeyData> dateListKey = new ArrayList<>();
            List<TestKeyData> numListKey = new ArrayList<>();
            for (TestKeyData testKeyData1 : listKey) {
                if (!testKeyData1.getList_name().equals(YEAR_LIST) &&
                        !testKeyData1.getList_name().equals(MONTH_LIST) &&
                        !testKeyData1.getList_name().equals(NUM_LIST)) {
                    tempListKey.add(testKeyData1);
                }

                if (testKeyData1.getList_name().equals(YEAR_LIST) || testKeyData1.getList_name().equals(MONTH_LIST)) {
                    dateListKey.add(testKeyData1);

                    /**年/月限 list_name 值 (year_list 或 month_list)*/
                    strDateKey = testKeyData1.getList_name();
                }

                if (testKeyData1.getList_name().equals(NUM_LIST)) {
                    numListKey.add(testKeyData1);

                    /**数量 list_name 值 (num_list)*/
                    strNumKey = testKeyData1.getList_name();
                }
            }

            List<TestData> listAll = new ArrayList<>();
            JSONObject object = jsonObject.optJSONObject(RESULT).optJSONObject(EXTEND);

            /**得到 年/月限 值*/
            final List<Integer> listDate = gson.fromJson(object.optString(dateListKey.get(0).getList_name()), new TypeToken<List<Integer>>() {
            }.getType());
            /**得到 数量 值*/
            final List<Integer> listNum = gson.fromJson(object.optString(numListKey.get(0).getList_name()), new TypeToken<List<Integer>>() {
            }.getType());

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(TestActivity.this);
            layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager1);
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
            testKeyAdapter = new TestKeyAdapter(R.layout.layout_test_item, listKey);
            recyclerView.setAdapter(testKeyAdapter);

            /**更新 年/月限 默认数据*/
            updateData(strDateKey, "1");
            /**更新 数量 默认数据*/
            updateData(strNumKey, "1");

            testKeyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                    if (listKey.get(position).getList_name().equals(YEAR_LIST) ||
                            listKey.get(position).getList_name().equals(MONTH_LIST)) {
                        List<String> list = new ArrayList<>();
                        for (Integer integer : listDate) {
                            list.add(String.valueOf(integer));
                        }
                        final String[] items = list.toArray(new String[list.size()]);

                        Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                                setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**更新自己数据*/
                                        updateData(listKey.get(position).getList_name(), items[which]);
                                    }
                                }).create();
                        alertDialog.show();
                    } else if (listKey.get(position).getList_name().equals(NUM_LIST)) {
                        List<String> list = new ArrayList<>();
                        for (Integer integer : listNum) {
                            list.add(String.valueOf(integer));
                        }
                        final String[] items = list.toArray(new String[list.size()]);

                        Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                                setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**更新自己数据*/
                                        updateData(listKey.get(position).getList_name(), items[which]);
                                    }
                                }).create();
                        alertDialog.show();
                    } else if (listKey.get(position).getList_name().equals(listStrListName.get(position))) {
                        List<String> list = new ArrayList<>();
                        for (TestData testData : listItems.get(position)) {
                            list.add(testData.getName());
                        }
                        final String[] items1 = list.toArray(new String[list.size()]);

                        Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                                setItems(items1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (position < tempListKey.size() - 1) {
                                            listValue.set(position, readOne4DB(items1[which]).get(0).getValue());
                                            listItems.set(position + 1, readOne4DB(listValue.get(position), listStrListName.get(position)));

                                            /**更新下一级数据*/
                                            update(position + 1);
                                        }

                                        /**更新自己数据*/
                                        updateData(listKey.get(position).getList_name(), items1[which]);
                                    }
                                }).create();

                        alertDialog.show();
                    }
                }
            });

            for (TestKeyData testKeyData1 : tempListKey) {
                List<TestData> listTestData = new ArrayList<>();
                List<TestData> tempListTestData = gson.fromJson(object.optString(testKeyData1.getList_name()), new TypeToken<List<TestData>>() {
                }.getType());
                for (TestData tempTestData1 : tempListTestData) {
                    tempTestData1.setList_name(testKeyData1.getList_name());
                    listTestData.add(tempTestData1);
                }

                listAll.addAll(listTestData);
            }

            SystemOutUtil.sysOut("listAll.size()-->" + listAll.size());
            deleteAll4DB();
            writeAll2DB(listAll);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        readAll4DB();

        update(0);
    }

    private void update(int index) {
        for (int i = index; i < tempListKey.size(); i++) {
            if (0 == i) {
                initFirstView();
            } else if (tempListKey.size() - 1 == i) {
                initLastView(listValue.get(i - 1), listStrListName.get(i - 1));
            } else {
                initMiddleView(listValue.get(i - 1), listStrListName.get(i - 1));
            }
        }
    }

    private void initFirstView() {
        boolean isDefault = false;
        String strDefaultName = "";
        String value1 = "";

        List<TestData> listItems1 = readFatherNodeNull();
        listItems.add(listItems1);

        List<TestData> list1 = readFatherNodeNull();
        for (TestData testData : list1) {
            if (testData.getRecommend().equals("1")) {
                isDefault = true;
                value1 = testData.getValue();
                listValue.add(value1);

                strDefaultName = testData.getName();
                break;
            }
        }

        if (!isDefault) {
            value1 = list1.get(0).getValue();
            listValue.add(value1);

            strDefaultName = list1.get(0).getName();
        }

        String strFirstListName = list1.get(0).getList_name();
        listStrListName.add(strFirstListName);

        List<TestData> listItems2 = readOne4DB(value1, strFirstListName);
        listItems.add(listItems2);

        updateData(strFirstListName, strDefaultName);

//        initMiddleView(value1, strFirstListName);
    }

    private void initMiddleView(String value, String father_node) {
        boolean isDefault = false;
        String strDefaultName = "";
        String value2 = "";

        List<TestData> list2 = readOne4DB(value, father_node);
        for (TestData testData : list2) {
            if (testData.getRecommend().equals("1")) {
                isDefault = true;
                value2 = testData.getValue();
                listValue.add(value2);

                strDefaultName = testData.getName();
                break;
            }
        }

        if (!isDefault) {
            value2 = list2.get(0).getValue();
            listValue.add(value2);

            strDefaultName = list2.get(0).getName();
        }

        String strSecondListName = list2.get(0).getList_name();
        listStrListName.add(strSecondListName);

        List<TestData> listItems3 = readOne4DB(value2, strSecondListName);
        listItems.add(listItems3);

        updateData(strSecondListName, strDefaultName);

//        initLastView(value2, strSecondListName);
    }

    private void initLastView(String value, String father_node) {
        boolean isDefault = false;
        String strDefaultName = "";
        String value3 = "";

        List<TestData> list3 = readOne4DB(value, father_node);
        for (TestData testData : list3) {
            if (testData.getRecommend().equals("1")) {
                isDefault = true;
                value3 = testData.getValue();
                listValue.add(value3);

                strDefaultName = testData.getName();
                break;
            }
        }

        if (!isDefault) {
            value3 = list3.get(0).getValue();
            listValue.add(value3);

            strDefaultName = list3.get(0).getName();
        }

        String strThirdListName = list3.get(0).getList_name();
        listStrListName.add(strThirdListName);

        updateData(strThirdListName, strDefaultName);
    }

    private void updateData(String name, String value) {
        for (int i = 0; i < listKey.size(); i++) {
            TestKeyData testKeyData1 = listKey.get(i);
            if (testKeyData1.getList_name().equals(name)) {
                listKey.get(i).setValue(value);
                break;
            }
        }

        testKeyAdapter.notifyDataSetChanged();
    }

    private void deleteAll4DB() {
        try {
            MyApplication.getDaoSession(this).runInTx(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getDaoSession(TestActivity.this).getTestDataDao().deleteAll();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeAll2DB(final List<TestData> data) {
        try {
            MyApplication.getDaoSession(this).runInTx(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    for (TestData entity : data) {
                        MyApplication.getDaoSession(TestActivity.this).getTestDataDao().insertOrReplace(entity);
                        size = size + 1;

                        if (size >= data.size()) {
                            ToastUtil.showToast(TestActivity.this, "数据存储成功");

//                            setIsInit();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<TestData> readAll4DB() {
        final TestDataDao dao = MyApplication.getDaoSession(this).getTestDataDao();
        List<TestData> list = dao.queryBuilder()
                .build().list();

        for (TestData testData : list) {
            SystemOutUtil.sysOut(testData.toString());
        }
        return list;
    }

    private List<TestData> readOne4DB(String value, String fatherNode) {
        final TestDataDao dao = MyApplication.getDaoSession(this).getTestDataDao();
        List<TestData> list = dao.queryBuilder()
                .where(TestDataDao.Properties.Fid.eq(value))
                .where(TestDataDao.Properties.Father_node.eq(fatherNode))
                .build().list();

        for (TestData testData : list) {
            SystemOutUtil.sysOut(testData.toString());
        }
        return list;
    }

    private List<TestData> readOne4DB(String name) {
        final TestDataDao dao = MyApplication.getDaoSession(this).getTestDataDao();
        List<TestData> list = dao.queryBuilder()
                .where(TestDataDao.Properties.Name.eq(name))
                .build().list();

        for (TestData testData : list) {
            SystemOutUtil.sysOut(testData.toString());
        }

        return list;
    }

    private List<TestData> readFatherNodeNull() {
        final TestDataDao dao = MyApplication.getDaoSession(this).getTestDataDao();
        List<TestData> list = dao.queryBuilder()
                .where(TestDataDao.Properties.Father_node.eq(""))
                .build().list();

        for (TestData testData : list) {
            SystemOutUtil.sysOut(testData.toString());
        }
        return list;
    }

    private boolean checkIsInit() {
        Constant.bIsInit = PreferencesUtil.getPrefBoolean(this, Constant.IS_INIT, true);
        return Constant.bIsInit;
    }

    private void setIsInit() {
        PreferencesUtil.setPrefBoolean(this, Constant.IS_INIT, false);
    }
}
