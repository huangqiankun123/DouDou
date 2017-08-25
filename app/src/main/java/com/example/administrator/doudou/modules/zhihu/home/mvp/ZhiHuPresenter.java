package com.example.administrator.doudou.modules.zhihu.home.mvp;

import android.util.Log;

import com.example.administrator.doudou.BasePresenter;
import com.example.administrator.doudou.MyApplication;
import com.example.administrator.doudou.api.ApiManager;
import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.disklrucache.DiskCacheManager;
import com.example.administrator.doudou.widget.recyclerView.BaseItem;
import com.example.administrator.doudou.widget.recyclerView.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ZhiHuPresenter extends BasePresenter implements ZhiHuImp.Presenter{
    public static final String TAG = "zhihu";

    private ZhiHuImp.View mZhiHuDailyFrag;

    private String date;

    public ZhiHuPresenter(ZhiHuImp.View mZhiHuDailyFrag) {
        this.mZhiHuDailyFrag = mZhiHuDailyFrag;
    }


    /**
     * 获取数据  并缓存起来
     */


    @Override
    public void refreshZhihuDaily() {
        mZhiHuDailyFrag.showRefreshBar();
        ApiManager.getInstence()
                .getZhihuService()
                .getLatestZhihuDaily()
                .map(new Function<ZhiHuDaily, ZhiHuDaily>() { //io 线程存储缓存
                    @Override
                    public ZhiHuDaily apply(ZhiHuDaily zhiHuDaily) {
                        DiskCacheManager manager = new DiskCacheManager(MyApplication.getContext(), Constants.CACHE_ZHIHU_FILE);
                        manager.put(Constants.CACHE_ZHIHU_DAILY, zhiHuDaily);
                        return zhiHuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDaily>() {
                    @Override
                    public void onComplete() {
                        mZhiHuDailyFrag.hideRefreshBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mZhiHuDailyFrag.hideRefreshBar();
                        mZhiHuDailyFrag.refreshFail(e.getMessage());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(ZhiHuDaily zhiHuDaily) {
//                        Log.i("nihao","我开始了加载了3");
                        date = zhiHuDaily.getDate();
                        mZhiHuDailyFrag.hideRefreshBar();
                        mZhiHuDailyFrag.refreshSuccessed(zhiHuDaily);
//                        Log.i("nihao","我开始了加载了4");
                    }
                });
    }
    @Override
    public void refreshZhihutop() {
//        mZhiHuDailyFrag.showRefreshBar();
//        ApiManager.getInstence().getZhihuService().getLatestZhihuDaily()
//                .map(new Function<ZhiHuDaily, List<ZhiHuDaily.TopStoriesBean>>() {
//                    @Override
//                    public List<ZhiHuDaily.TopStoriesBean> apply(ZhiHuDaily zhiHuDaily) throws Exception {
//
//                        return zhiHuDaily.getTop_stories();
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<ZhiHuDaily.TopStoriesBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        addDisposable(d);
//                    }
//
//                    @Override
//                    public void onNext(List<ZhiHuDaily.TopStoriesBean> value) {
//                        mZhiHuDailyFrag.hideRefreshBar();
//                        mZhiHuDailyFrag.refreshSuccessedTop(value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mZhiHuDailyFrag.hideRefreshBar();
//                        mZhiHuDailyFrag.refreshTopFail(e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mZhiHuDailyFrag.hideRefreshBar();
//                    }
//                });

    }

    /**
     * 加载更多
     */

    public void loadMoreData() {
       ApiManager.getInstence().getZhihuService()
                .getZhihuDaily(date)
                .map(new Function<ZhiHuDaily, List<ZhiHuDaily.StoriesBean>>() {
                    @Override
                    public List<ZhiHuDaily.StoriesBean> apply(ZhiHuDaily zhiHuDaily) throws Exception {
                        date =zhiHuDaily.getDate();
                        return zhiHuDaily.getStories();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ZhiHuDaily.StoriesBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                       addDisposable(d);
                    }

                    @Override
                    public void onNext(List<ZhiHuDaily.StoriesBean> value) {
                            mZhiHuDailyFrag.loadSuccessed( value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mZhiHuDailyFrag.loadFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        ApiManager.getInstance().getZhihuService()
//                .getZhihuDaily(date)
//                .map(new Function<ZhiHuDaily, List<ZhiHuDaily.StoriesBean>>() {
//                    @Override
//                    public List<ZhiHuDaily.StoriesBean> apply(ZhiHuDaily zhiHuDaily) throws Exception {
//                        date = zhiHuDaily.getDate();
//                        return zhiHuDaily.getStories();
//
//                    }
//                })//将ArrayList<ZhiHuStory> 遍历 到 zhiHuStory 对象
//                .flatMap(new Function<ArrayList<ZhiHuDaily.StoriesBean>, Observable<ZhiHuStory>>() {
//                    @Override
//                    public Observable<ZhiHuStory> apply(ArrayList<ZhiHuStory> zhiHuStories) throws Exception {
//
//                        return Observable.fromIterable(zhiHuStories);
//                    }
//                })
//                .map(new Function<ZhiHuStory, BaseItem>() {
//                    @Override
//                    public BaseItem apply(ZhiHuStory zhiHuStory) throws Exception {
//                        zhiHuStory.setDate(date);
//                        BaseItem<ZhiHuStory>  baseItem = new BaseItem<ZhiHuStory>();
//                        baseItem.setData(zhiHuStory);
//                        return baseItem;
//                    }
//                })
//                .toList()
//                .map(new Function<List<BaseItem>, List<BaseItem>>() {
//                    @Override
//                    public List<BaseItem> apply(List<BaseItem> baseItems) throws Exception {
//                        BaseItem<String> baseItem = new BaseItem<String>();
//                        baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_TAGS);
//                        baseItem.setData(date);
//                        baseItems.add(0,baseItem);
//                        return baseItems;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<List<BaseItem>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        addDisposable(d);
//                    }
//
//                    @Override
//                    public void onSuccess(List<BaseItem> value) {
//                        mZhiHuDailyFrag.loadSuccessed((ArrayList<BaseItem>) value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mZhiHuDailyFrag.loadFail(e.getMessage());
//                    }
//                });

    }


    /**
     * 加载缓存
     */

    public void loadCache() {
        final DiskCacheManager manager = new DiskCacheManager(MyApplication.getContext(), Constants.CACHE_ZHIHU_FILE);
        //创建观察者
        Observable.create(new ObservableOnSubscribe<ZhiHuDaily>() {
            @Override
            public void subscribe(ObservableEmitter<ZhiHuDaily> e) throws Exception {
                ZhiHuDaily zhiHuDaily = manager.getSerializable(Constants.CACHE_ZHIHU_DAILY);
                e.onNext(zhiHuDaily);
            }
        }).subscribe(new Observer<ZhiHuDaily>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(ZhiHuDaily value) {
                if (value!=null){
                    date=value.getDate();
                    mZhiHuDailyFrag.refreshSuccessed(value);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


}
