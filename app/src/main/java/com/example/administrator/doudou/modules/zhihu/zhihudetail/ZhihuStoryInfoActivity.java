package com.example.administrator.doudou.modules.zhihu.zhihudetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.doudou.R;
import com.example.administrator.doudou.activities.ShareActivity;
import com.example.administrator.doudou.config.Constants;
import com.example.administrator.doudou.utils.DensityUtil;
import com.example.administrator.doudou.utils.PicassoTarget;

import com.example.administrator.doudou.utils.x5webview.WebUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/10.
 */

public class ZhihuStoryInfoActivity extends ShareActivity implements ZhiHuDetailContract.View{
    @BindView(R.id.story_img)
    ImageView storyImg;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.pb_load_story)
    ProgressBar pbLoadStory;
    @BindView(R.id.zhihudaily_webview)
    WebView zhihudailyWebview;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private String story_id;

    private ZhihuStoryInfoPresenter mPresenter = new ZhihuStoryInfoPresenter(this);
    private int width;
    private int heigh;
    private String shareUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_story_info);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initView();
        initData();
    }

    private void initView() {
        int[] deviceInfo = DensityUtil.getDeviceInfo(this);
        width = deviceInfo[0];
        heigh = width * 3 / 5;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(shareUrl)){
                    ZhihuStoryInfoActivity.this.showShare(shareUrl,toolbarTitle.getText().toString());
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        story_id = String.valueOf(bundle.getInt(Constants.BUNDLE_KEY_ID));
        String title = bundle.getString(Constants.BUNDLE_KEY_TITLE);
        //请求
        loadZhihuStory();
        toolbarTitle.setText(title);
        toolbarTitle.setSelected(true);
    }


    @Override
    public void showProgressBar() {
        pbLoadStory.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pbLoadStory.setVisibility(View.GONE);
    }

    @Override
    public void loadZhihuStory() {
        mPresenter.loadStory(story_id);
    }

    @Override
    public void loadFail(String errmsg) {

    }

    @Override
    public void loadSuccess(ZhihuStoryContent zhihuStory) {
        shareUrl = zhihuStory.getShare_url();
        Target target = new PicassoTarget(ZhihuStoryInfoActivity.this,storyImg,toolbarLayout,toolbar,fab);
        storyImg.setTag(target);
        String image = zhihuStory.getImage();
        if (!TextUtils.isEmpty(image)){
            Picasso.with(this).load(image).resize(width,heigh).centerCrop().into(target);
        }
        startPostponedEnterTransition();
        String url = zhihuStory.getShare_url();

        boolean isEmpty = TextUtils.isEmpty(zhihuStory.getBody());
        String body = zhihuStory.getBody();
//        List<String> css = zhihuStory.getCss();
        String[] css =zhihuStory.getCss();
        if (isEmpty){
            zhihudailyWebview.loadUrl(url);
        }else {
            String data = WebUtils.buildHtmlWithCss(body, css, false);
            zhihudailyWebview.loadDataWithBaseURL(WebUtils.BASE_URL,data,WebUtils.MIME_TYPE,WebUtils.ENCODING,WebUtils.FAIL_URL);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.dispose();
    }
}
