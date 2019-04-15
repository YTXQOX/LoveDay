package com.ljstudio.android.loveday.utils;

/**
 * Created by guoren on 2017/5/3 16:23
 * Usage
 */
public class VersionData {

    /**
     * code : 200
     * message : sucess
     * datatype : null
     * data : {"id":"3","app_id":"3","version_code":"10","version_name":"1.0","type":"1","apk_url":"https://app.qipai.com/qiyun/version/iwork.apk","upgrade_point":"iwork有新的版本，快来升级啦！","status":"0","create_time":"2018-06-14 09:09:38","update_time":"2018-06-14 09:09:41"}
     * limit : null
     */

    private String code;
    private String message;
    private Object datatype;
    private DataBean data;
    private Object limit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDatatype() {
        return datatype;
    }

    public void setDatatype(Object datatype) {
        this.datatype = datatype;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getLimit() {
        return limit;
    }

    public void setLimit(Object limit) {
        this.limit = limit;
    }

    public static class DataBean {
        /**
         * id : 3
         * app_id : 3
         * version_code : 10
         * version_name : 1.0
         * type : 1
         * apk_url : https://app.qipai.com/qiyun/version/iwork.apk
         * upgrade_point : iwork有新的版本，快来升级啦！
         * status : 0
         * create_time : 2018-06-14 09:09:38
         * update_time : 2018-06-14 09:09:41
         */

        private String id;
        private String app_id;
        private String version_code;
        private String version_name;
        private String type;
        private String apk_url;
        private String upgrade_point;
        private String status;
        private String create_time;
        private String update_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getApk_url() {
            return apk_url;
        }

        public void setApk_url(String apk_url) {
            this.apk_url = apk_url;
        }

        public String getUpgrade_point() {
            return upgrade_point;
        }

        public void setUpgrade_point(String upgrade_point) {
            this.upgrade_point = upgrade_point;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }

    @Override
    public String toString() {
        return "VersionData{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", datatype=" + datatype +
                ", data=" + data +
                ", limit=" + limit +
                '}';
    }
}





