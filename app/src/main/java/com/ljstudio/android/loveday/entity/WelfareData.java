package com.ljstudio.android.loveday.entity;

/**
 * Created by guoren on 2017/2/27 17:14
 * Usage
 */

public class WelfareData {

    private String title;
    private String content;
    private String slogan;
    private String action;
    private String url;
    private String belong;
    private int order;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    @Override
    public String toString() {
        return "WelfareData{" +
                "title='" + title + '\'' +
                ", slogan='" + slogan + '\'' +
                ", content='" + content + '\'' +
                ", order='" + order + '\'' +
                ", belong='" + belong + '\'' +
                ", action='" + action + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
