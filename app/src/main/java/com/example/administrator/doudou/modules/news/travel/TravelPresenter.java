package com.example.administrator.doudou.modules.news.travel;

import com.example.administrator.doudou.BasePresenter;
import com.example.administrator.doudou.MyApplication;
import com.example.administrator.doudou.api.ApiManager;
import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.disklrucache.DiskCacheManager;
import com.example.administrator.doudou.modules.news.NewImp;
import com.example.administrator.doudou.modules.news.NewsBean;
import com.example.administrator.doudou.modules.news.headline.TopNewsList;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/4.
 */

public class TravelPresenter extends BasePresenter implements NewImp.Presenter {

    private NewImp.View mNewsListFrag;

    public TravelPresenter(NewImp.View mNewsListFrag) {
        this.mNewsListFrag = mNewsListFrag;
    }
    private int currentIndex;

    @Override
    public void refreshNews() {
        mNewsListFrag.showRefreshBar();
        currentIndex =0;
        ApiManager.getInstence().getTopNewsServie().getTravelNews(currentIndex+"")
                .map(new Function<TravelNewsList, ArrayList<NewsBean>>() {
                    @Override
                    public ArrayList<NewsBean> apply(TravelNewsList travelNewsList) throws Exception {
                        return travelNewsList.getTrivelNewsArrayList();
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<NewsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(ArrayList<NewsBean> value) {
                        DiskCacheManager manager=new DiskCacheManager(MyApplication.getContext(), Constants.CACHE_NEWS_FILE);
                        manager.put(Constants.CACHE_TRAVEL_NEWS,value);
                        currentIndex+=20;
                        mNewsListFrag.hideRefreshBar();
                        mNewsListFrag.refreshNewsSuccessed(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mNewsListFrag.hideRefreshBar();
                        mNewsListFrag.refreshNewsFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mNewsListFrag.hideRefreshBar();
                    }
                });

    }

    @Override
    public void loadMore() {
        ApiManager.getInstence().getTopNewsServie().getTravelNews(currentIndex+"")
                .map(new Function<TravelNewsList, ArrayList<NewsBean>>() {
                    @Override
                    public ArrayList<NewsBean> apply(TravelNewsList travelNewsList) throws Exception {
                        return travelNewsList.getTrivelNewsArrayList();
                    }


                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<NewsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(ArrayList<NewsBean> value) {
                        currentIndex+=20;
                        mNewsListFrag.loadMoreSuccessed(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mNewsListFrag.loadMoreFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void loadCache() {
        DiskCacheManager manager = new DiskCacheManager(MyApplication.getContext(),Constants.CACHE_NEWS_FILE);
        ArrayList<NewsBean> topNews= manager.getSerializable(Constants.CACHE_TRAVEL_NEWS);
        if (topNews!=null){
            mNewsListFrag.refreshNewsSuccessed(topNews);
        }
    }


}
