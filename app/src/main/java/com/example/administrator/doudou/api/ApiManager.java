package com.example.administrator.doudou.api;

import com.example.administrator.doudou.config.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/21.
 * 集中处理Api相关配置的Manager类
 * 先来一个单例再说
 */

public class ApiManager {

    private static  ApiManager sApiManager;
    private static OkHttpClient client;
    private ZhihuDailyApi dailyApi;
    private NetEasyNewsApi netEasyNewsApi;
    private MovieApi movieApi;

    public ApiManager() {
    }

    public static ApiManager getInstence(){
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                        sApiManager = new ApiManager();
                }
            }
        }
        /**
         * OkHttpClient也是Builder结构，可以是设置connectTimout, readTimeout, writeTimout的时间，
         设置Dispatcher, Dns, Cache，Interceptors, NetworkInterceptors等等
         */
        client = new OkHttpClient.Builder()
                //天机拦截器 先不使用
                .addInterceptor(new CustomInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

        return sApiManager;
    }

    /**
     * 封装 配置知乎API
     * 1 先定ZhihuDailyApi 接口
     * 2 使用ertrofit2.0 来编写请求
     * 3 使用rxjava 来编写接口中的方法
     */
    public ZhihuDailyApi getZhihuService(){


        if (dailyApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.ZHIHU_API_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            dailyApi = retrofit.create(ZhihuDailyApi.class);
        }
//        return mDailyApi;

        return dailyApi;
    }

    /**
     * 新闻
     * @return
     */
    public NetEasyNewsApi getTopNewsServie(){
        if (netEasyNewsApi ==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.NETEASY_NEWS_API)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            netEasyNewsApi = retrofit.create(NetEasyNewsApi.class);
        }


        return netEasyNewsApi;
    }

    /**
     * 视频首页
     * @return
     */
    public MovieApi getMovieServie(){
        if (movieApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.MOVIE_API_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            movieApi = retrofit.create(MovieApi.class);
        }

        return movieApi;
    }






}
