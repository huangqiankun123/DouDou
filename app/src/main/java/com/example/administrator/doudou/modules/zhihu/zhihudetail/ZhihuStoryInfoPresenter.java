package com.example.administrator.doudou.modules.zhihu.zhihudetail;

import com.example.administrator.doudou.BasePresenter;
import com.example.administrator.doudou.api.ApiManager;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/5/10.
 */

public class ZhihuStoryInfoPresenter extends BasePresenter implements ZhiHuDetailContract.Presenter {

    private ZhiHuDetailContract.View mZhiHuDetailContractImp;

    public ZhihuStoryInfoPresenter(ZhiHuDetailContract.View mZhiHuDetailContractImp) {
        this.mZhiHuDetailContractImp = mZhiHuDetailContractImp;
    }

    @Override
    public void loadStory(String id) {
        mZhiHuDetailContractImp.showProgressBar();
        ApiManager.getInstence().getZhihuService().getStoryContent(id)
               .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuStoryContent>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(ZhihuStoryContent value) {
                        mZhiHuDetailContractImp.loadSuccess(value);
                        mZhiHuDetailContractImp.hideProgressBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mZhiHuDetailContractImp.loadFail(e.getMessage());
                        mZhiHuDetailContractImp.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        mZhiHuDetailContractImp.hideProgressBar();
                    }
                });
    }
}
