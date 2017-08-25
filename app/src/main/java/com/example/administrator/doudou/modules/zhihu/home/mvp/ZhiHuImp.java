package com.example.administrator.doudou.modules.zhihu.home.mvp;

import com.example.administrator.doudou.widget.recyclerView.BaseItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface ZhiHuImp {
    interface View{
        //显示bar
        void showRefreshBar();
        //隐藏bar
        void hideRefreshBar();
        //请求数据（刷新数据）
        void refreshData();

//        void refreshSuccessedTop(List<ZhiHuDaily.TopStoriesBean> topStoriesBean);
//
//        void refreshTopFail(String errmsg);
        // 刷新数据成功
        void refreshSuccessed(ZhiHuDaily stories);
        // 刷新数据失败
        void refreshFail(String errMsg);
        //加载更多
        void loadMoreData();
        //加载更多成功
        void loadSuccessed(List<ZhiHuDaily.StoriesBean> stories);
        //加载更多失败
        void loadFail(String errMsg);
    }

    interface Presenter {
        void refreshZhihuDaily();

        void loadMoreData();

        void loadCache();

        void refreshZhihutop();
    }
}
