package com.example.administrator.doudou.modules.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.doudou.BaseFragment;
import com.example.administrator.doudou.R;
import com.example.administrator.doudou.modules.news.headline.HeadLineFragment;
import com.example.administrator.doudou.modules.news.health.HealthFragment;
import com.example.administrator.doudou.modules.news.military.MilitaryFragment;
import com.example.administrator.doudou.modules.news.relaxing.RelaxFragment;
import com.example.administrator.doudou.modules.news.sports.SportNewsFragment;
import com.example.administrator.doudou.modules.news.technology.TecNewsFragment;
import com.example.administrator.doudou.modules.news.travel.TravelFragment;
import com.youth.banner.transformer.ZoomInTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/18.
 */

public class NewsMainFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.tl_news_tabs)
    TabLayout tlNewsTabs;
    @BindView(R.id.vp_news_list)
    ViewPager vpNewsList;
    Unbinder unbinder;
    private Context context;
    private ArrayList<Fragment> fragmentArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        //添加fragment集合
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new HeadLineFragment());
        fragmentArrayList.add(new TecNewsFragment());
        fragmentArrayList.add(new SportNewsFragment());
        fragmentArrayList.add(new HealthFragment());
        fragmentArrayList.add(new RelaxFragment());
        fragmentArrayList.add(new MilitaryFragment());
        fragmentArrayList.add(new TravelFragment());
        /**
         * 如果应用场景是更多的Fragment，请使用FragmentStatePagerAdapter。
         * 3个fragment时使用 FragmentPagerAdapter
         */
//        MyAdapter adapter = new MyAdapter(getActivity().getSupportFragmentManager());
//        vpNewsList.setAdapter(adapter);
        vpNewsList.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentArrayList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentArrayList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getResources().getStringArray(R.array.news_title)[position];
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
//        vpNewsList.setPageTransformer(false ,new ZoomInTransformer());
        vpNewsList.addOnPageChangeListener(this);
        tlNewsTabs.setupWithViewPager(vpNewsList);
        tlNewsTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset==0 || positionOffsetPixels == 0){
            vpNewsList.setCurrentItem(position);
        }
    }

    @Override
    public void onPageSelected(int position) {
        vpNewsList.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
