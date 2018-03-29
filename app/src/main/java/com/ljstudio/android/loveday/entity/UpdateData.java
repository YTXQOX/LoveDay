package com.ljstudio.android.loveday.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guoren on 2017/2/27 17:14
 * Usage
 */

public class UpdateData extends ResponseData implements Parcelable {

//   "app_id": "9",
//   "version_code": "1",
//   "version_name": "1.0",
//   "apk_url": "https://app.qipai.com/seven/version/pandian.php",
//   "update_description": "盘点APP有新版本，快来升级",

    private int app_id;
    private int version_code;
    private String version_name;
    private String apk_url;
    private String update_description;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.app_id);
        dest.writeInt(this.version_code);
        dest.writeString(this.version_name);
        dest.writeString(this.apk_url);
        dest.writeString(this.update_description);
    }

    public UpdateData() {
    }

    protected UpdateData(Parcel in) {
        this.app_id = in.readInt();
        this.version_code = in.readInt();
        this.version_name = in.readString();
        this.apk_url = in.readString();
        this.update_description = in.readString();
    }

    public static final Creator<UpdateData> CREATOR = new Creator<UpdateData>() {
        @Override
        public UpdateData createFromParcel(Parcel source) {
            return new UpdateData(source);
        }

        @Override
        public UpdateData[] newArray(int size) {
            return new UpdateData[size];
        }
    };


    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getUpdate_description() {
        return update_description;
    }

    public void setUpdate_description(String update_description) {
        this.update_description = update_description;
    }

    @Override
    public String toString() {
        return "UpdateData{" +
                "app_id=" + app_id +
                ", version_code=" + version_code +
                ", version_name='" + version_name + '\'' +
                ", apk_url='" + apk_url + '\'' +
                ", update_description='" + update_description + '\'' +
                '}';
    }
}
