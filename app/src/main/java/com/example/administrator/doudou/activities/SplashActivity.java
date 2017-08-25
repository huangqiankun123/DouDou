package com.example.administrator.doudou.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.doudou.BaseActivity;
import com.example.administrator.doudou.R;
import com.example.administrator.doudou.utils.ViewUtils;
import com.squareup.picasso.Picasso;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/19.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.iv_splast_image)
    CircleImageView ivSplastImage;
    @BindView(R.id.countdown)
    TextView countdown;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏(在setContentView之前设置)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //获取到values/array 资源下的图片
        String[] images = getResources().getStringArray(R.array.splash_background);
        /**
         * Random random = new Random();
         random.nextInt(100); //100是不包含在内的，只产生0~99之间的数。
         */
        int position = new Random().nextInt(images.length - 1) / images.length;
        Picasso.with(this).load(images[position]).into(ivBackground);
        Picasso.with(this).load("file//:" + ViewUtils.getAppFile(this, "/images/user.png"))
                .error(getResources().getDrawable(R.mipmap.userimage))
                .into(ivSplastImage);

        downTime();



    }

    //做一个定时器

    public void downTime() {
        CountDownTimer timer = new CountDownTimer(5500, 1000) {
            @Override
            public void onTick(long l) {


                countdown.setText(String.format(getResources().getString(R.string.countdown),
                        (int) (l / 1000 + 0.1)));
                countdown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                        return;
                    }
                });


            }

            @Override
            public void onFinish() {
                if (!SplashActivity.this.isFinishing()) {
                    countdown.setText(String.format(getResources().getString(R.string.countdown), 0));
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    return;
                }

            }
        };
        timer.start();
    }
}
