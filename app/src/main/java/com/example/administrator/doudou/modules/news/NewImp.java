package com.example.administrator.doudou.modules.news;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface NewImp {
    interface View{
        void showRefreshBar();//1 显示加载数据 bar (dailog)

        void hideRefreshBar();  //2 隐藏 bar (dailog)

        void refreshNews();   //3 加载数据

        void refreshNewsFail(String errorMsg); // 4 加载数据失败

        void refreshNewsSuccessed(ArrayList<NewsBean> topNews); //5 加载数据成功  获取到数据

        void loadMoreNews(); //6 加载更多

        void loadMoreFail(String errorMsg); //7 加载更多失败

        void loadMoreSuccessed(ArrayList<NewsBean> topNewses); //8 加载更多成功 获取到数据

        void loadAll();
    }

    interface Presenter{
        void refreshNews(); // 1 获取到数据

        void loadMore();   //2  加载更多

        void loadCache();  // 3 缓存
    }
}
