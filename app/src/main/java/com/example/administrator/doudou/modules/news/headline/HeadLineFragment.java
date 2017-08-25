package com.example.administrator.doudou.modules.news.headline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.doudou.BaseFragment;
import com.example.administrator.doudou.R;
import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.modules.news.NewImp;
import com.example.administrator.doudou.modules.news.NewsBean;
import com.example.administrator.doudou.modules.news.NewsListAdapter;
import com.example.administrator.doudou.modules.news.newsdetail.NewsDetailActivity;
import com.example.administrator.doudou.rxbus.RxBus;
import com.example.administrator.doudou.rxbus.RxConstants;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/4/26.
 */
public class HeadLineFragment extends BaseFragment implements NewImp.View, NewsListAdapter.OnItemClickListener {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;
    Unbinder unbinder;
    private Context context;
    private Disposable mDisposable;
    private ArrayList<NewsBean> newsBeanList = new ArrayList<>();
    private HeadLinePresenter mPresenter = new HeadLinePresenter(this);
    private NewsListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.head_line_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context=this.getContext();
        layoutManager = new LinearLayoutManager(context);
           recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        initView();
        Log.i("nihao","HeadLineFragment ---->onCreateView");
        return view;
    }

    private void initView() {


        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshNews();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });



        refreshNews();

        mPresenter.loadCache();

    }

    @Override
    public void onResume() {
        super.onResume();
//        onHiddenChanged(false);
    }

    @Override
    public void onPause() {
        super.onPause();
//        recyclerView.setPullRefreshEnabled(false);
        mPresenter.dispose();
//        onHiddenChanged(true);
    }

    @Override
    public void showRefreshBar() {
        showProgressDialog("给我等着");
    }

    @Override
    public void hideRefreshBar() {
        dismissLoadScheduleDialog();
    }

    @Override
    public void refreshNews() {
        mPresenter.refreshNews();
    }

    @Override
    public void refreshNewsFail(String errorMsg) {
        recyclerView.refreshComplete();
    }

    @Override
    public void refreshNewsSuccessed(ArrayList<NewsBean> topNews) {
        dismissLoadScheduleDialog();
        newsBeanList.clear();
        newsBeanList.addAll(topNews);



        if (adapter == null){
            adapter = new NewsListAdapter(context,newsBeanList);

            recyclerView.setAdapter(adapter);

        }else {
           adapter.setData(newsBeanList);
        }

        adapter.setOnItemClickListener(this);
        recyclerView.refreshComplete();
    }

    @Override
    public void loadMoreNews() {
        mPresenter.loadMore();
    }

    @Override
    public void loadMoreFail(String errorMsg) {
        recyclerView.loadMoreComplete();
    }

    @Override
    public void loadMoreSuccessed(ArrayList<NewsBean> topNewses) {

        newsBeanList.addAll(topNewses);
        recyclerView.loadMoreComplete();
    }

    @Override
    public void loadAll() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.i("nihao","HeadLineFragment ---->onDestroyView");
        adapter=null;
    }

    @Override
    public void onItemClick(View view, int position) {
        NewsBean newsBean = newsBeanList.get(position);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(HeadLineFragment.this.getActivity(),NewsDetailActivity.class);
        bundle.putString(Constants.BUNDLE_KEY_TITLE,newsBean.getTitle());
        bundle.putString(Constants.BUNDLE_KEY_ID, newsBean.getDocid());
        bundle.putString(Constants.BUNDLE_KEY_IMG_URL, newsBean.getImgsrc());
        bundle.putString(Constants.BUNDLE_KEY_HTML_URL, newsBean.getUrl());
        intent.putExtras(bundle);
        String transitionName = getString(R.string.top_news_img);
        Pair pairImg =new Pair<>(view.findViewById(R.id.news_image),transitionName);
        ActivityOptionsCompat transitionActivityOptions =  ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),pairImg);
        startActivity(intent, transitionActivityOptions.toBundle());

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden ){
            recyclerView.setPullRefreshEnabled(false);
        }
        if (!hidden){
            RxBus.getDefault()
                    .toObservableWithCode(RxConstants.BACK_PRESSED_CODE,String.class)
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDisposable=d;
                        }

                        @Override
                        public void onNext(String value) {
                            if (value.equals(RxConstants.BACK_PRESSED_DATA) && recyclerView!=null){
                                layoutManager.smoothScrollToPosition(recyclerView,null,0);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
    }
}