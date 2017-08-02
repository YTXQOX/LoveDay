package com.ljstudio.android.loveday.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljstudio.android.loveday.MyApplication;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.constants.Constant;
import com.ljstudio.android.loveday.entity.TestData;
import com.ljstudio.android.loveday.greendao.TestDataDao;
import com.ljstudio.android.loveday.utils.PreferencesUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoren on 2017/5/10 11:07
 * Usage
 */

public class TestActivity extends AppCompatActivity {

    public static final String CLASS_LIST = "class_list";
    public static final String VERSION_LIST = "version_list";
    public static final String NODE_LIST = "node_list";

    @BindView((R.id.id_test_text1))
    TextView textView1;
    @BindView((R.id.id_test_text2))
    TextView textView2;
    @BindView((R.id.id_test_text3))
    TextView textView3;

    private List<TestData> listItems1 = new ArrayList<>();
    private List<TestData> listItems2 = new ArrayList<>();
    private List<TestData> listItems3 = new ArrayList<>();

    private String value1;
    private String value2;
    private String value3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

        if (checkIsInit()) {
            initData();
        }
        initView();

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                for (TestData testData : listItems1) {
                    list.add(testData.getName());
                }
                final String[] items = list.toArray(new String[list.size()]);

                Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                        setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textView1.setText(items[which]);

                                value1 = readOne4DB(items[which]).get(0).getValue();
                                listItems2 = readOne4DB(value1, CLASS_LIST);

                                initVersionView(value1, CLASS_LIST);
                            }
                        }).create();

                alertDialog.show();
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                for (TestData testData : listItems2) {
                    list.add(testData.getName());
                }
                final String[] items = list.toArray(new String[list.size()]);

                Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                        setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textView2.setText(items[which]);

                                value2 = readOne4DB(items[which]).get(0).getValue();
                                listItems3 = readOne4DB(value2, VERSION_LIST);

                                initNodeView(value2, VERSION_LIST);
                            }
                        }).create();

                alertDialog.show();
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                for (TestData testData : listItems3) {
                    list.add(testData.getName());
                }
                final String[] items = list.toArray(new String[list.size()]);

                Dialog alertDialog = new AlertDialog.Builder(TestActivity.this).
                        setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textView3.setText(items[which]);
                            }
                        }).create();

                alertDialog.show();
            }
        });
    }

    private void initData() {
        String str = "{\n" +
                "    \"result\": {\n" +
                "        \"extend\": [\n" +
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
                "        \"data\": {\n" +
                "            \"class_list\": [\n" +
                "                {\n" +
                "                    \"value\": \"1\",\n" +
                "                    \"name\": \"企业网站模板\",\n" +
                "                    \"fid\": null,\n" +
                "                    \"father_node\": null,\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"2\",\n" +
                "                    \"name\": \"企业定制官网\",\n" +
                "                    \"fid\": null,\n" +
                "                    \"father_node\": null,\n" +
                "                    \"recommend\": 0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"version_list\": [\n" +
                "                {\n" +
                "                    \"value\": \"创业版\",\n" +
                "                    \"name\": \"创业版\",\n" +
                "                    \"fid\": \"1\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"中小企业版\",\n" +
                "                    \"name\": \"中小企业版\",\n" +
                "                    \"fid\": \"1\",\n" +
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
                "                    \"value\": \"高级版\",\n" +
                "                    \"name\": \"高级版\",\n" +
                "                    \"fid\": \"2\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"尊贵版\",\n" +
                "                    \"name\": \"尊贵版\",\n" +
                "                    \"fid\": \"2\",\n" +
                "                    \"father_node\": \"class_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                }\n" +
                "            ],\n" +
                "            \"node_list\": [\n" +
                "                {\n" +
                "                    \"value\": \"1\",\n" +
                "                    \"name\": \"国内\",\n" +
                "                    \"fid\": \"创业版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"2\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"创业版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"1\",\n" +
                "                    \"name\": \"国内\",\n" +
                "                    \"fid\": \"中小企业版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"2\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"中小企业版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"1\",\n" +
                "                    \"name\": \"国内\",\n" +
                "                    \"fid\": \"标准版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"2\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"标准版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"1\",\n" +
                "                    \"name\": \"国内\",\n" +
                "                    \"fid\": \"高级版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"1\",\n" +
                "                    \"name\": \"国内\",\n" +
                "                    \"fid\": \"尊贵版\",\n" +
                "                    \"father_node\": \"version_list\",\n" +
                "                    \"recommend\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"2\",\n" +
                "                    \"name\": \"国外\",\n" +
                "                    \"fid\": \"尊贵版\",\n" +
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
                "            \"contract_info\": {\n" +
                "                \"name\": \"服务合同\",\n" +
                "                \"url\": \"http://www.xmisp.com\"\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"code\": \"1000\",\n" +
                "    \"msg\": \"操作成功\",\n" +
                "    \"versioninfo\": {\n" +
                "        \"version\": \"1.0.0\",\n" +
                "        \"version_int\": \"1000\",\n" +
                "        \"system\": \"点击网络-建站项目API\",\n" +
                "        \"link\": \"http://www.dj.cn\"\n" +
                "    }\n" +
                "}";

        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject object = jsonObject.optJSONObject("result").optJSONObject("data");
            Gson gson = new Gson();
            List<TestData> listClass = gson.fromJson(object.optString(CLASS_LIST), new TypeToken<List<TestData>>(){}.getType());
            List<TestData> listVersion = gson.fromJson(object.optString(VERSION_LIST), new TypeToken<List<TestData>>(){}.getType());
            List<TestData> listNode = gson.fromJson(object.optString(NODE_LIST), new TypeToken<List<TestData>>(){}.getType());

            SystemOutUtil.sysOut("listClass.size()-->" + listClass.size());
            SystemOutUtil.sysOut("listVersion.size()-->" + listVersion.size());
            SystemOutUtil.sysOut("listNode.size()-->" + listNode.size());
            List<TestData> listAll = new ArrayList<>();
            listAll.addAll(listClass);
            listAll.addAll(listVersion);
            listAll.addAll(listNode);
            SystemOutUtil.sysOut("listAll.size()-->" + listAll.size());
            writeAll2DB(listAll);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        readAll4DB();

        listItems1 = readFatherNodeNull();

        initClassView();
    }

    private void initClassView() {
        boolean isDefault = false;
        String strDefaultName = "";
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

        textView1.setText(strDefaultName);

        listItems2 = readOne4DB(value1, CLASS_LIST);

        initVersionView(value1, CLASS_LIST);
    }

    private void initVersionView(String value, String father_node) {
        boolean isDefault = false;
        String strDefaultName = "";
        List<TestData> list1 = readOne4DB(value, father_node);
        for (TestData testData : list1) {
            if (testData.getRecommend().equals("1")) {
                isDefault = true;
                value2 = testData.getValue();
                strDefaultName = testData.getName();
                break;
            }
        }

        if (!isDefault) {
            value2 = list1.get(0).getValue();
            strDefaultName = list1.get(0).getName();
        }

        textView2.setText(strDefaultName);

        listItems3 = readOne4DB(value2, VERSION_LIST);

        initNodeView(value2, VERSION_LIST);
    }

    private void initNodeView(String value, String father_node) {
        boolean isDefault = false;
        String strDefaultName = "";
        List<TestData> list1 = readOne4DB(value, father_node);
        for (TestData testData : list1) {
            if (testData.getRecommend().equals("1")) {
                isDefault = true;
                value3 = testData.getValue();
                strDefaultName = testData.getName();
                break;
            }
        }

        if (!isDefault) {
            value3 = list1.get(0).getValue();
            strDefaultName = list1.get(0).getName();
        }

        textView3.setText(strDefaultName);
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

                            setIsInit();
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
                .where(TestDataDao.Properties.Father_node.isNull())
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
