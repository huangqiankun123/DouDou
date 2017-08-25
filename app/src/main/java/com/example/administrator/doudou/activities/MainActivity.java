package com.example.administrator.doudou.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.doudou.BaseActivity;
import com.example.administrator.doudou.R;
import com.example.administrator.doudou.modules.news.NewsMainFragment;
import com.example.administrator.doudou.modules.settings.ChoosePhotoActivity;
import com.example.administrator.doudou.modules.video.videohome.mvp.VideoHomeFragment;

import com.example.administrator.doudou.modules.zhihu.home.mvp.ZhihuDailyFragment;
import com.example.administrator.doudou.utils.BlurImageUtils;
import com.example.administrator.doudou.utils.ViewUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.bottom_navigation)
    BottomNavigationBar bottomNavigation;
    @BindView(R.id.main_content)
    LinearLayout mainContent;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.drawer)
    DrawerLayout drawer;


    private Fragment mCurrentFrag; //当前的fragment
    private FragmentManager fm;
    private Fragment mZhihuFragment;
    private Fragment mNewsFragment;
    private Fragment mBubbleFragment;
    private final int ACTION_GET_PIC = 10;
    private CircleImageView userimager;
    private LinearLayout circle_header_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fm = getSupportFragmentManager();
        initView();
    }

    private void initView() {
        //初始化 主要的fragment 的
        mZhihuFragment = new ZhihuDailyFragment();
        mBubbleFragment = new VideoHomeFragment();
        mNewsFragment = new NewsMainFragment();
        initBottomNavigation();
        switchContent(mZhihuFragment);
        initNavigation();


    }

    private void initNavigation() {
        /**
         * 这是获取到navigation的头部的方法
         */
        circle_header_ll = (LinearLayout) navigation.getHeaderView(0).findViewById(R.id.circle_header_ll);
        userimager = (CircleImageView) navigation.getHeaderView(0).findViewById(R.id.userimage);
        Picasso.with(this)
                .load("file://" + ViewUtils.getAppFile(this, "images/user.png"))
                .error(getResources().getDrawable(R.mipmap.userimage))
                .into(userimager, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap overlay = BlurImageUtils.blur(userimager, 3, 3);
                        circle_header_ll.setBackground(new BitmapDrawable(getResources(), overlay));
                    }

                    @Override
                    public void onError() {
                        Bitmap overlay = BlurImageUtils.blur(userimager, 3, 3);
                        circle_header_ll.setBackground(new BitmapDrawable(getResources(), overlay));
                    }
                });

        userimager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
//                Toast.makeText(MainActivity.this, "点击了我的头", Toast.LENGTH_SHORT).show();
            }


        });
        /**
         * navigation 的菜单部分
         */
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_favorite:
                        Toast.makeText(MainActivity.this, "点击了我的收藏", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_download:
                        Toast.makeText(MainActivity.this, "点击了我的视频", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(MainActivity.this, "点击了我的分享应用", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_clean:
                        Toast.makeText(MainActivity.this, "点击了我的清理缓存", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this, "点击了我的关于", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;


                }
                //点击就关闭了
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });


    }

    /**
     * 点击头像 进行头像的切换
     */
    private void takePhoto() {
        Intent intent = new Intent(MainActivity.this, ChoosePhotoActivity.class);
        startActivityForResult(intent, ACTION_GET_PIC);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_GET_PIC && data != null) {
            Bitmap bitmap = data.getExtras().getParcelable("data");
            userimager.setImageBitmap(bitmap);
            Bitmap overlay = BlurImageUtils.blur(userimager, 3, 3);
            circle_header_ll.setBackground(new BitmapDrawable(getResources(), overlay));
            saveUserImage(bitmap);

        }
    }

    private void saveUserImage(Bitmap bitmap) {
        // 保存头像到sdcard
        FileOutputStream fos;
        try {
            File file = new File(ViewUtils.getAppFile(this, "images"));
            File image = new File(ViewUtils.getAppFile(this, "images/user.png"));
            if (!file.exists()) {
                file.mkdirs();
                if (!image.exists()) {
                    image.createNewFile();
                }
            }
            fos = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 动态添加fragment，不会重复创建fragment
     *
     * @param to 将要加载的fragment
     */
    private void switchContent(Fragment to) {
        if (mCurrentFrag != to) {//当前的fragment 不为需要将要加载的fragment
            //将要添加的fragment没有添加
            if (!to.isAdded()) { //如果该Fragment对象被添加到了它的Activity中，那么它返回true，否则返回false。
                if (mCurrentFrag != null) { //当前的fragment不为null ，所以将隐藏当前的fragment
                    //当前的fragment 没有添加到activity
                    fm.beginTransaction().hide(mCurrentFrag).commit();
                }
                //将将要加载的fragment的添加
                fm.beginTransaction().add(R.id.content, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                fm.beginTransaction().hide(mCurrentFrag).show(to).commit();
            }
            mCurrentFrag = to;

        }

    }


    private void initBottomNavigation() {
        //设置导航栏背景模式 setBackgroundStyle（）
        //设置BottomNavigationItem颜色 setActiveColor, setInActiveColor, setBarBackgroundColor
        /**
         * 组合
         * MODE_FIXED+BACKGROUND_STYLE_STATIC效果
         * MODE_FIXED+BACKGROUND_STYLE_RIPPLE效果
         * MODE_SHIFTING+BACKGROUND_STYLE_STATIC效果
         * MODE_SHIFTING+BACKGROUND_STYLE_RIPPLE效果
         */
        bottomNavigation
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                    .setMode(BottomNavigationBar.MODE_FIXED)
                .addItem(new BottomNavigationItem(R.mipmap.ic_home, getString(R.string.zhihustory)).setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_view_headline, getString(R.string.nav_01_title)).setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_live_tv, getString(R.string.nav_02_title)).setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(0)
                .setTabSelectedListener(this)
                .initialise();
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                switchContent(mZhihuFragment);
                break;
            case 1:
                switchContent(mNewsFragment);
                break;
            case 2:
                switchContent(mBubbleFragment);
                break;

        }

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Long firstTime = 0L;
    @Override
    public void onBackPressed() {
        //int drawerGravity 直接给navigation 就可以了
        if (drawer.isDrawerOpen(findViewById(R.id.navigation))){
            drawer.closeDrawers();
//        else
//            super.onBackPressed();
            return;
        }

        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime >1500){
            Toast.makeText(MainActivity.this,getString(R.string.back_again_exit),Toast.LENGTH_LONG).show();
            firstTime = secondTime;
        }else {
            System.exit(0);
        }

    }

}
