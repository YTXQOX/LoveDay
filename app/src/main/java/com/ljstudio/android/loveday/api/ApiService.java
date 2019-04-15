package com.ljstudio.android.loveday.api;

import com.ljstudio.android.loveday.utils.VersionData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by guoren on 2018/5/14 10:52
 * Usage
 */

public interface ApiService {

    /**
     * login
     */
//    @FormUrlEncoded
//    @POST("uc/user/app/login")
//    Observable<UserData> login(@FieldMap Map<String, String> params);


    /**
     * http://gank.io/api/data/福利/10/1
     */
//    @GET("%E7%A6%8F%E5%88%A9/{count}/{page}")
//    Observable<BeautyData> getBeauty(@Path("count") int count, @Path("page") int page);


    /**
     * https://app.qipai.com/qiyun/index.php?method=requestVersionUpgrade&controller=version
     */
    @GET("qiyun/index.php")
    Observable<VersionData> update(@Query("method") String method, @Query("controller") String controller);


    /**
     * test
     */
    @GET("top250")
    Observable<String> test(@Query("start") int start, @Query("count") int count);

}
