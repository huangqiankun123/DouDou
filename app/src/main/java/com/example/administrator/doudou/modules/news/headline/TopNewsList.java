package com.example.administrator.doudou.modules.news.headline;

import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.modules.news.NewsBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by PandaQ on 2016/9/20.
 * email : 767807368@qq.com
 */

public class TopNewsList {

    @SerializedName(Constants.NETEASY_NEWS_HEADLINE)

    private ArrayList<NewsBean> mTopNewsArrayList;

    public ArrayList<NewsBean> getTopNewsArrayList() {
        return mTopNewsArrayList;
    }
}
