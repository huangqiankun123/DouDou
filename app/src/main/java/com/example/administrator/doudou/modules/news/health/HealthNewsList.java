package com.example.administrator.doudou.modules.news.health;

import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.modules.news.NewsBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by PandaQ on 2016/9/20.
 * email : 767807368@qq.com
 */

public class HealthNewsList {

    @SerializedName(Constants.NETEASY_NEWS_HEALTH)

    private ArrayList<NewsBean> mHealthNewsArrayList;

    public ArrayList<NewsBean> getRecNewsArrayList() {
        return mHealthNewsArrayList;
    }
}
