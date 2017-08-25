package com.example.administrator.doudou.modules.zhihu.home.mvp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.doudou.BaseFragment;
import com.example.administrator.doudou.R;
import com.example.administrator.doudou.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/18.
 */

public class ZhihuDailyFragment extends BaseFragment implements ZhiHuImp.View, SwipeRefreshLayout.OnRefreshListener {


    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private Context context;
    private ZhiHuPresenter mPresenter = new ZhiHuPresenter(this);

    private List<ZhiHuDaily.StoriesBean> zhiHuDailyList = new ArrayList<>();
    private Banner banner;


    private ZhiHuDailyAdapter adapter;


    //创建image 路径集合
    private List<String> images = new ArrayList<>();
    //创建 title 集合
    private List<String> titles = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.zhihulist_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = this.getActivity();

        mPresenter.loadCache();

        initView();

        return view;
    }

    private void initView() {

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

//        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
//        recyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
//        //设置头布局
//        View recycler_header = LayoutInflater.from(context).inflate(R.layout.recyclerview_header, null, false);
//        banner = (Banner) recycler_header.findViewById(R.id.banner);
//
//
//        recyclerView.addHeaderView(recycler_header);
//        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                mPresenter.refreshZhihuDaily();
//            }
//
//            @Override
//            public void onLoadMore() {
//                mPresenter.loadMoreData();
//            }
//        });

        mPresenter.loadCache();
        refreshData();

        adapter = new ZhiHuDailyAdapter(R.layout.zhihu_story_item,zhiHuDailyList);
        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new ZhiHuDailyAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
////                Toast.makeText(context,zhiHuDailyList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//
//                //跳转到其他界面
//
//                String title = zhiHuDailyList.get(position).getTitle();
//                int id = zhiHuDailyList.get(position).getId();
//
//                Bundle bundle = new Bundle();
//                Intent intent = new Intent(ZhihuDailyFragment.this.getActivity(), ZhihuStoryInfoActivity.class);
//                bundle.putString(Constants.BUNDLE_KEY_TITLE, zhiHuDailyList.get(position).getTitle());
//                bundle.putInt(Constants.BUNDLE_KEY_ID, zhiHuDailyList.get(position).getId());
//                intent.putExtras(bundle);
//                startActivity(intent);
//
//
//            }
//        });


    }

    @Override
    public void onStart() {
        super.onStart();
//        mPresenter.refreshZhihutop();
//        banner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.dispose();
    }

    @Override
    public void onStop() {
        super.onStop();
//        banner.stopAutoPlay();
    }

    @Override
    public void showRefreshBar() {
        showProgressDialog("正在加载");
    }

    @Override
    public void hideRefreshBar() {
        dismissLoadScheduleDialog();
    }

    //请求数据
    @Override
    public void refreshData() {
        //调用请求的方法
        mPresenter.refreshZhihuDaily();

    }


    //请求的结果
    private boolean isfirst = true;

    @Override
    public void refreshSuccessed(ZhiHuDaily stories) {

        zhiHuDailyList.clear();
        zhiHuDailyList.addAll(stories.getStories());
//        images.clear();
//        titles.clear();
        if (isfirst) {

            List<ZhiHuDaily.TopStoriesBean> top_stories = stories.getTop_stories();

            for (ZhiHuDaily.TopStoriesBean top_story : top_stories) {
                images.add(top_story.getImage());
                titles.add(top_story.getTitle());

                //设置banner样式
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //设置图片集合
                banner.setImages(images);
                //设置banner动画效果
                banner.setBannerAnimation(Transformer.Default);
                //设置标题集合（当banner样式有显示title时）
                banner.setBannerTitles(titles);
                //设置自动轮播，默认为true
                banner.isAutoPlay(true);
                //设置轮播时间
                banner.setDelayTime(1500);
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                banner.start();

            }


            isfirst = false;
        }


//        recyclerView.refreshComplete();

        adapter.notifyDataSetChanged();


    }

    //请求的结果
    @Override
    public void refreshFail(String errMsg) {

    }

    @Override
    public void loadMoreData() {
        mPresenter.loadMoreData();
    }

    @Override
    public void loadSuccessed(List<ZhiHuDaily.StoriesBean> stories) {
        zhiHuDailyList.addAll(stories);

    }

    @Override
    public void loadFail(String errMsg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onRefresh() {

    }
}
