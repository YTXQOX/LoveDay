package com.ljstudio.android.loveday.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by guoren on 2017/8/2 09:09
 * Usage
 */

@Entity
public class AllParameter {

    @Id
    private Long id;

    private String paramId;
    private String paramName;
    private String valueType;
    private int len;
    private String dateTime;
    private String value;

    @Generated(hash = 1194016542)
    public AllParameter(Long id, String paramId, String paramName, String valueType,
            int len, String dateTime, String value) {
        this.id = id;
        this.paramId = paramId;
        this.paramName = paramName;
        this.valueType = valueType;
        this.len = len;
        this.dateTime = dateTime;
        this.value = value;
    }

    @Generated(hash = 94499330)
    public AllParameter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "AllParameter{" +
                "id=" + id +
                ", paramId='" + paramId + '\'' +
                ", paramName='" + paramName + '\'' +
                ", valueType='" + valueType + '\'' +
                ", len=" + len +
                ", dateTime='" + dateTime + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
