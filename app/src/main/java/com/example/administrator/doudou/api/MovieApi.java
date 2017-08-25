package com.example.administrator.doudou.api;

import com.example.administrator.doudou.modules.video.videohome.MovieResponse;
import com.example.administrator.doudou.modules.video.videohome.mvp.RetDataBean;
import com.example.administrator.doudou.modules.video.videohome.mvp.VideoBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface MovieApi {
    /**
     * 获取首页数据 json
     * @return
     */
    @GET("homePageApi/homePage.do")
    Observable<MovieResponse<RetDataBean>>  getHomePage();

//    @GET("homePageApi/homePage.do")
//    Observable<VideoBean.RetBean>  getHomePage();

    /**
     *获取电影详情 json
     * @param mediaId
     * @return
     */
    @GET("videoDetailApi/videoDetail.do")
    Observable<VideoBean.RetBean.ListBean.ChildListBean> getMovieDaily(@Query("mediaId") String mediaId);


}
