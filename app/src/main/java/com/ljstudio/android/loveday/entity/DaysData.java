package com.ljstudio.android.loveday.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by guoren on 2017/5/3 16:23
 * Usage
 */

@Entity
public class DaysData {

    @Id
    private Long id;

    @NotNull
    private String title;
    private String date;
    private String days;
    private String unit;
    private boolean isTop;


    @Generated(hash = 136757265)
    public DaysData(Long id, @NotNull String title, String date, String days,
                    String unit, boolean isTop) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.days = days;
        this.unit = unit;
        this.isTop = isTop;
    }

    @Generated(hash = 519397184)
    public DaysData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDays() {
        return this.days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean getIsTop() {
        return this.isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    @Override
    public String toString() {
        return "DaysData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", days='" + days + '\'' +
                ", unit='" + unit + '\'' +
                ", isTop=" + isTop +
                '}';
    }
}
