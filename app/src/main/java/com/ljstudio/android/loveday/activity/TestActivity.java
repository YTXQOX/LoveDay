package com.ljstudio.android.loveday.activity;

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

    private List<TestData> listItems1 = new ArrayList<>();
    private List<TestData> listItems2 = new ArrayList<>();
    private List<TestData> listItems3 = new ArrayList<>();

    private String strFirstListName;
    private String strSecondListName;
    private String strThirdListName;

    private String value1;
    private String value2;
    private String value3;


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
            final List<TestKeyData> listKey = gson.fromJson(strData, new TypeToken<List<TestKeyData>>() {
            }.getType());

            List<TestKeyData> tempListKey = new ArrayList<>();
            List<TestKeyData> dateListKey = new ArrayList<>();
            List<TestKeyData> numListKey = new ArrayList<>();
            for (TestKeyData testKeyData1 : listKey) {
                if (!testKeyData1.getList_name().equals(YEAR_LIST) &&
                        !testKeyData1.getList_name().equals(MONTH_LIST) &&
                        !testKeyData1.getList_name().equals(NUM_LIST)) {
                    tempListKey.add(testKeyData1);
                }

                if (testKeyData1.getList_name().equals(YEAR_LIST) ||
                        testKeyData1.getList_name().equals(MONTH_LIST)) {
                    dateListKey.add(testKeyData1);
                }

                if (testKeyData1.getList_name().equals(NUM_LIST)) {
                    numListKey.add(testKeyData1);
                }
            }

            List<TestData> listAll = new ArrayList<>();
            JSONObject object = jsonObject.optJSONObject(RESULT).optJSONObject(EXTEND);

            final List<Integer> listDate = gson.fromJson(object.optString(dateListKey.get(0).getList_name()), new TypeToken<List<Integer>>() {
            }.getType());

            final List<Integer> listNum = gson.fromJson(object.optString(numListKey.get(0).getList_name()), new TypeToken<List<Integer>>() {
            }.getType());

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(TestActivity.this);
            layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager1);
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
            TestKeyAdapter testKeyAdapter = new TestKeyAdapter(R.layout.layout_test_item, listKey, listDate, listNum);
            recyclerView.setAdapter(testKeyAdapter);

            testKeyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
                                        String.valueOf(items[which]);
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
                                        String.valueOf(items[which]);
                                    }
                                }).create();
                        alertDialog.show();
                    } else if (listKey.get(position).getList_name().equals(strFirstListName)) {
                        List<String> list = new ArrayList<>();
                        for (TestData testData : listItems1) {
                            list.add(testData.getName());
                        }
                        final String[] items = list.toArray(new String[list.size()]);

                        Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                                setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        value1 = readOne4DB(items[which]).get(0).getValue();
                                        listItems2 = readOne4DB(value1, strFirstListName);

                                        initSecondView(value1, strFirstListName);
                                    }
                                }).create();

                        alertDialog.show();
                    } else if (listKey.get(position).getList_name().equals(strSecondListName)) {
                        List<String> list2 = new ArrayList<>();
                        for (TestData testData : listItems2) {
                            list2.add(testData.getName());
                        }
                        final String[] items2 = list2.toArray(new String[list2.size()]);

                        Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                                setItems(items2, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String.valueOf(items2[which]);

                                        value2 = readOne4DB(items2[which]).get(0).getValue();
                                        listItems3 = readOne4DB(value2, strSecondListName);

                                        initThirdView(value2, strSecondListName);
                                    }
                                }).create();

                        alertDialog.show();
                    } else if (listKey.get(position).getList_name().equals(strThirdListName)) {
                        List<String> list3 = new ArrayList<>();
                        for (TestData testData : listItems3) {
                            list3.add(testData.getName());
                        }
                        final String[] items3 = list3.toArray(new String[list3.size()]);

                        Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                                setItems(items3, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String.valueOf(items3[which]);
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

        initFirstView();
    }

    private void initFirstView() {
        boolean isDefault = false;
        String strDefaultName = "";

        listItems1 = readFatherNodeNull();
        List<TestData> list1 = readFatherNodeNull();
        for (TestData testData : list1) {
            if (testData.getRecommend().equals("1")) {
                isDefault = true;
                value1 = testData.getValue();
                strDefaultName = testData.getName();
                break;
            }
        }

        if (!isDefault) {
            value1 = list1.get(0).getValue();
            strDefaultName = list1.get(0).getName();
        }

        strFirstListName = list1.get(0).getList_name();
        listItems2 = readOne4DB(value1, strFirstListName);

        initSecondView(value1, list1.get(0).getList_name());
    }

    private void initSecondView(String value, String father_node) {
        boolean isDefault = false;
        String strDefaultName = "";

        List<TestData> list2 = readOne4DB(value, father_node);
        for (TestData testData : list2) {
            if (testData.getRecommend().equals("1")) {
                isDefault = true;
                value2 = testData.getValue();
                strDefaultName = testData.getName();
                break;
            }
        }

        if (!isDefault) {
            value2 = list2.get(0).getValue();
            strDefaultName = list2.get(0).getName();
        }

        strSecondListName = list2.get(0).getList_name();
        listItems3 = readOne4DB(value2, strSecondListName);

        initThirdView(value2, strSecondListName);
    }

    private void initThirdView(String value, String father_node) {
        boolean isDefault = false;
        String strDefaultName = "";

        List<TestData> list3 = readOne4DB(value, father_node);
        for (TestData testData : list3) {
            if (testData.getRecommend().equals("1")) {
                isDefault = true;
                value3 = testData.getValue();
                strDefaultName = testData.getName();
                break;
            }
        }

        if (!isDefault) {
            value3 = list3.get(0).getValue();
            strDefaultName = list3.get(0).getName();
        }

        strThirdListName = list3.get(0).getList_name();
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
