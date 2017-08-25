package com.example.administrator.doudou.api;

import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.modules.news.headline.TopNewsList;
import com.example.administrator.doudou.modules.news.health.HealthNewsList;
import com.example.administrator.doudou.modules.news.military.MilitaryNewsList;
import com.example.administrator.doudou.modules.news.relaxing.RelaxNewsList;
import com.example.administrator.doudou.modules.news.sports.SportNewsList;
import com.example.administrator.doudou.modules.news.technology.TecNewsList;
import com.example.administrator.doudou.modules.news.travel.TravelNewsList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by Administrator on 2017/5/4.
 */

public interface NetEasyNewsApi {
    //头条的数据
    @GET("list/"+ Constants.NETEASY_NEWS_HEADLINE+"/{index}-20.html")
    Observable<TopNewsList> getTopNews(@Path("index") String index);
    //科技
    @GET("list/"+ Constants.NETEASY_NEWS_TEC+"/{index}-20.html")
    Observable<TecNewsList> getTecNews(@Path("index") String index);
    //体育
    @GET("list/"+Constants.NETEASY_NEWS_SPORT + "/{index}-20.html")
    Observable<SportNewsList> getSportNews(@Path("index") String index);
    //健康
    @GET("list/"+Constants.NETEASY_NEWS_HEALTH + "/{index}-20.html")
    Observable<HealthNewsList> getRecommendNews(@Path("index") String index);
    //轻松一刻
    @GET("list/"+Constants.NETEASY_NEWS_DADA + "/{index}-20.html")
    Observable<RelaxNewsList> getDadaNews(@Path("index") String index);
    //军事
    @GET("list/"+Constants.NETEASY_NEWS_MILITARY + "/{index}-20.html")
    Observable<MilitaryNewsList> getMilitaryNews(@Path("index") String index);
    //旅游
    @GET("list/"+Constants.NETEASY_NEWS_TRAVEL + "/{index}-20.html")
    Observable<TravelNewsList> getTravelNews(@Path("index") String index);

    @GET("{id}/full.html")
    Observable<ResponseBody> getNewsContent(@Path("id") String id);
}
