package com.example.administrator.doudou.api;

import com.example.administrator.doudou.modules.zhihu.home.mvp.ZhiHuDaily;
import com.example.administrator.doudou.modules.zhihu.zhihudetail.ZhihuStoryContent;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface ZhihuDailyApi {

    //获取最近的日报
    @GET("news/latest")
    Observable<ZhiHuDaily> getLatestZhihuDaily();

    //获取某一时间之前的日报（本例用于加载更多）
    @GET("news/before/{date}")
    Observable<ZhiHuDaily> getZhihuDaily(@Path("date") String date);


    @GET("news/{id}")
    Observable<ZhihuStoryContent> getStoryContent(@Path("id") String id);


}
