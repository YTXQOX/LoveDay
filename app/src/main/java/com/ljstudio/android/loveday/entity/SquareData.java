package com.ljstudio.android.loveday.entity;

/**
 * Created by guoren on 2017/2/27 17:14
 * Usage
 */

public class SquareData {

    private String objectId;
    private String userId;
    private Long id;
    private String title;
    private String date;
    private String days;
    private String unit;
    private boolean isTop;
    private String nickname;
    private String likes;
    private int likeNum;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    @Override
    public String toString() {
        return "SquareData{" +
                "objectId='" + objectId + '\'' +
                ", userId='" + userId + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", days='" + days + '\'' +
                ", unit='" + unit + '\'' +
                ", isTop=" + isTop +
                ", nickname='" + nickname + '\'' +
                ", likes='" + likes + '\'' +
                ", likeNum=" + likeNum +
                '}';
    }
}
