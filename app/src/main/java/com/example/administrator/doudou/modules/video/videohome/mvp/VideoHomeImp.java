package com.example.administrator.doudou.modules.video.videohome.mvp;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface VideoHomeImp {
    interface View{
        void refreshData();

        void refreshSuccess(ArrayList<RetDataBean.ListBean> value);

//        void refreshSuccess(ArrayList<VideoBean.RetBean.ListBean> value);

        void refreshFail(String errCode, String errMsg);

        void showProgressBar();

        void hideProgressBar();


    }

    interface Presenter{
        void loadData();

        void loadCache();
    }
}
