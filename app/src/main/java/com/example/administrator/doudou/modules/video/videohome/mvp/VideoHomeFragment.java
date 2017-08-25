package com.example.administrator.doudou.modules.video.videohome.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.doudou.BaseFragment;
import com.example.administrator.doudou.R;
import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.widget.GlideImageLoader;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
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

public class VideoHomeFragment extends BaseFragment implements VideoHomeImp.View {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;
    Unbinder unbinder;
    private Context context;
    private VideoHomePresenter mPresenter = new VideoHomePresenter(this);
    private Banner banner;
    private ArrayList<RetDataBean.ListBean> toplistBean = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.video_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context =this.getActivity();

        initView();
        return view;
    }

    private void initView() {

        recyclerView.setLayoutManager(new GridLayoutManager(context,2));

        View recycler_header = LayoutInflater.from(context).inflate(R.layout.recyclerview_header, null, false);
        banner = (Banner) recycler_header.findViewById(R.id.banner);

        recyclerView.addHeaderView(recycler_header);



        refreshData();
        mPresenter.loadCache();



    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.dispose();
    }

    @Override
    public void refreshData() {
        mPresenter.loadData();
    }
    List<String> images = new ArrayList<>();
    List<VideoBean.RetBean.ListBean.ChildListBean> childList = new ArrayList<>();
    @Override
    public void refreshSuccess(ArrayList<RetDataBean.ListBean> value) {
        for (RetDataBean.ListBean listBean : value) {
            if (listBean.getShowType().equals("IN")) {
                toplistBean.addAll(value);

            }

//        VideoBean.RetBean.ListBean banners = value.get(0);
            RetDataBean.ListBean banners = value.get(0);
            if (Constants.SHOW_TYPE_BANNER.equals(banners.getShowType())) {//就是banner的数据
                for (RetDataBean.ListBean.ChildListBean childListBean : banners.getChildList()) {
                    images.add(childListBean.getPic());
                }

                //设置banner样式
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //设置图片集合
                banner.setImages(images);
                //设置banner动画效果
                banner.setBannerAnimation(Transformer.Default);
                //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
                //设置自动轮播，默认为true
                banner.isAutoPlay(true);
                //设置轮播时间
                banner.setDelayTime(1500);
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                banner.start();


            }

//        toplistBean.addAll(value);
            VideoHomeAdapter adapter = new VideoHomeAdapter(context, toplistBean);
            recyclerView.setAdapter(adapter);

        }


    }

    @Override
    public void refreshFail(String errCode, String errMsg) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
