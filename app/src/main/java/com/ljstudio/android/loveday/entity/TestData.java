package com.ljstudio.android.loveday.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by guoren on 2017/8/2 09:09
 * Usage
 */

@Entity
public class TestData {

    @Id
    private Long id;

    private String value;
    private String name;
    private String fid;
    private String father_node;
    private String recommend;

    @Generated(hash = 836796389)
    public TestData(Long id, String value, String name, String fid,
            String father_node, String recommend) {
        this.id = id;
        this.value = value;
        this.name = name;
        this.fid = fid;
        this.father_node = father_node;
        this.recommend = recommend;
    }

    @Generated(hash = 1580692206)
    public TestData() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFather_node() {
        return father_node;
    }

    public void setFather_node(String father_node) {
        this.father_node = father_node;
    }

    public String isRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getRecommend() {
        return this.recommend;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", name='" + name + '\'' +
                ", fid='" + fid + '\'' +
                ", father_node='" + father_node + '\'' +
                ", recommend='" + recommend + '\'' +
                '}';
    }
}
