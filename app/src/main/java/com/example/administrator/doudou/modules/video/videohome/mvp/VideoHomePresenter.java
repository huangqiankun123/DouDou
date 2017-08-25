package com.example.administrator.doudou.modules.video.videohome.mvp;

import android.util.Log;

import com.example.administrator.doudou.BasePresenter;
import com.example.administrator.doudou.MyApplication;
import com.example.administrator.doudou.api.ApiManager;
import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.disklrucache.DiskCacheManager;
import com.example.administrator.doudou.modules.video.videohome.MovieResponse;


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
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/4.
 */

public class VideoHomePresenter extends BasePresenter implements VideoHomeImp.Presenter {

    private VideoHomeImp.View mVideoHomeFrag;

    public VideoHomePresenter(VideoHomeImp.View mVideoHomeFrag) {
        this.mVideoHomeFrag = mVideoHomeFrag;
    }


    /**
     * 加载视频主页的json数据
     */
    public void loadData() {
        mVideoHomeFrag.showProgressBar();
        ApiManager.getInstence()
                .getMovieServie()
                .getHomePage()
                .flatMap(new Function<MovieResponse<RetDataBean>, Observable<RetDataBean.ListBean>>() {
                    @Override
                    public Observable<RetDataBean.ListBean> apply(MovieResponse<RetDataBean> response) {
                        return Observable.fromIterable(response.getData().getList());
                    }
                })
                //去广告
                .filter(new Predicate<RetDataBean.ListBean>() {
                    @Override
                    public boolean test(RetDataBean.ListBean listBean) throws Exception {
                        String showType = listBean.getShowType();
                        return Constants.SHOW_TYPE_IN.equals(showType) || Constants.SHOW_TYPE_BANNER.equals(showType);
                    }
                })
                .toList()
                //将 List 转为ArrayList 缓存存储 ArrayList Serializable对象
                .map(new Function<List<RetDataBean.ListBean>, ArrayList<RetDataBean.ListBean>>() {
                    @Override
                    public ArrayList<RetDataBean.ListBean> apply(List<RetDataBean.ListBean> listBeen) {
                        ArrayList<RetDataBean.ListBean> arr = new ArrayList<RetDataBean.ListBean>();
                        arr.addAll(listBeen);
                        DiskCacheManager manager = new DiskCacheManager(MyApplication.getContext(), Constants.CACHE_VIDEO_FILE);
                        manager.put(Constants.CACHE_VIDEO, arr);
                        return arr;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<RetDataBean.ListBean>>() {


                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<RetDataBean.ListBean> value) {
                        mVideoHomeFrag.refreshSuccess(value);
                        mVideoHomeFrag.hideProgressBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mVideoHomeFrag.hideProgressBar();
                        mVideoHomeFrag.refreshFail(Constants.ERRO, e.getMessage());
                    }

                });

    }

//        ApiManager.getInstence()
//                .getMovieServie()
//                .getHomePage()
//                .flatMap(new Function<VideoBean.RetBean, ObservableSource<VideoBean.RetBean.ListBean>>() {
//                    @Override
//                    public ObservableSource<VideoBean.RetBean.ListBean> apply(VideoBean.RetBean retBean) throws Exception {
//                            return Observable.fromIterable(retBean.getList());
//                    }
//                })

//                .map(new Function<VideoBean.RetBean, List<VideoBean.RetBean.ListBean>>() {
//                    @Override
//                    public List<VideoBean.RetBean.ListBean> apply(VideoBean.RetBean retBean) throws Exception {
//                        return retBean.getList();
//                    }
//                })
//                .flatMap(new Function<List<VideoBean.RetBean.ListBean>, ObservableSource<VideoBean.RetBean.ListBean>>() {
//                    @Override
//                    public ObservableSource<VideoBean.RetBean.ListBean> apply(List<VideoBean.RetBean.ListBean> listBeens) throws Exception {
//
//                        return Observable.fromIterable(listBeens);
//                    }
//                })

//                .filter(new Predicate<VideoBean.RetBean.ListBean>() {
//                    @Override
//                    public boolean test(VideoBean.RetBean.ListBean listBean) throws Exception {
//                        String showType = listBean.getShowType();
//
//                        return Constants.SHOW_TYPE_IN.equals(showType) || Constants.SHOW_TYPE_BANNER.equals(showType);
//                    }
//                })
//                .toList()
//                .map(new Function<List<VideoBean.RetBean.ListBean>, ArrayList<VideoBean.RetBean.ListBean>>() {
//                    @Override
//                    public ArrayList<VideoBean.RetBean.ListBean> apply(List<VideoBean.RetBean.ListBean> listBeen) throws Exception {
//                        ArrayList<VideoBean.RetBean.ListBean> arr = new ArrayList<>();
//                        arr.addAll(listBeen);
//                        DiskCacheManager manager = new DiskCacheManager(MyApplication.getContext(),Constants.CACHE_VIDEO_FILE);
//                        manager.put(Constants.CACHE_VIDEO,arr);
//                        return arr;
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<ArrayList<VideoBean.RetBean.ListBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        addDisposable(d);
//                    }
//
//                    @Override
//                    public void onSuccess(ArrayList<VideoBean.RetBean.ListBean> value) {
//                        mVideoHomeFrag.hideProgressBar();
//                        mVideoHomeFrag.refreshSuccess(value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mVideoHomeFrag.hideProgressBar();
//                        mVideoHomeFrag.refreshFail(Constants.ERRO, e.getMessage());
//                    }
//                });





    /**
     * 加载缓存
     */
    @Override
    public void loadCache() {
    final DiskCacheManager manager = new DiskCacheManager(MyApplication.getContext(),Constants.CACHE_VIDEO_FILE);
      Observable.create(new ObservableOnSubscribe<ArrayList<RetDataBean.ListBean>>() {
          @Override
          public void subscribe(ObservableEmitter<ArrayList<RetDataBean.ListBean>> e) throws Exception {
              ArrayList<RetDataBean.ListBean> arrbean = manager.getSerializable(Constants.CACHE_VIDEO);
              e.onNext(arrbean);
          }
      }).subscribe(new Observer<ArrayList<RetDataBean.ListBean>>() {
          @Override
          public void onSubscribe(Disposable d) {
              addDisposable(d);
          }

          @Override
          public void onNext(ArrayList<RetDataBean.ListBean> value) {
              if (value!=null){
                  mVideoHomeFrag.refreshSuccess(value);
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
