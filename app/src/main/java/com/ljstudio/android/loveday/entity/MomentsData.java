package com.ljstudio.android.loveday.entity;

/**
 * Created by guoren on 2017/2/27 17:14
 * Usage
 */

public class MomentsData {

    private String objectId;
    private String userId;
    private Long id;
    private String url;
    private String content;
    private String date;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        return "MomentsData{" +
                "objectId='" + objectId + '\'' +
                ", userId='" + userId + '\'' +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", nickname='" + nickname + '\'' +
                ", likes='" + likes + '\'' +
                ", likeNum=" + likeNum +
                '}';
    }
}
