package com.ljstudio.android.loveday.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guoren on 2017/10/7 10:05
 * Usage
 */

public class DetailData implements Parcelable {
    private Long id;
    private int monthBackground;
    private int falling;
    private String title;
    private String date;
    private String days;
    private String unit;
    private boolean isTop;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFalling() {
        return falling;
    }

    public void setFalling(int falling) {
        this.falling = falling;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean getIsTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public int getMonthBackground() {
        return monthBackground;
    }

    public void setMonthBackground(int monthBackground) {
        this.monthBackground = monthBackground;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeInt(this.monthBackground);
        dest.writeInt(this.falling);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.days);
        dest.writeString(this.unit);
        dest.writeByte(this.isTop ? (byte) 1 : (byte) 0);
    }

    public DetailData() {
    }

    protected DetailData(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.monthBackground = in.readInt();
        this.falling = in.readInt();
        this.title = in.readString();
        this.date = in.readString();
        this.days = in.readString();
        this.unit = in.readString();
        this.isTop = in.readByte() != 0;
    }

    public static final Creator<DetailData> CREATOR = new Creator<DetailData>() {
        @Override
        public DetailData createFromParcel(Parcel source) {
            return new DetailData(source);
        }

        @Override
        public DetailData[] newArray(int size) {
            return new DetailData[size];
        }
    };

    @Override
    public String toString() {
        return "DetailData{" +
                "id=" + id +
                ", monthBackground=" + monthBackground +
                ", falling=" + falling +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", days='" + days + '\'' +
                ", unit='" + unit + '\'' +
                ", isTop=" + isTop +
                '}';
    }
}
