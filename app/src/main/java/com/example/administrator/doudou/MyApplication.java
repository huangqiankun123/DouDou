package com.example.administrator.doudou;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tencent.smtt.sdk.QbSdk;


public class MyApplication extends Application {
    public static  MyApplication instance;

    public MyApplication() {
    }

//    public static MyApplication getInstance(){
//        if (instance==null){
//            synchronized (MyApplication.class){
//                if (instance ==null){
//                    instance = new MyApplication();
//                }
//            }
//        }
//        return instance;
//
//    }
    @Override
    public void onCreate() {
        super.onCreate();

        if (instance ==null){
            instance= this;
        }
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        //预加载腾讯浏览服务 X5 内核
        QbSdk.initX5Environment(getContext(), null);


    }


    public static Context getContext() {
        return instance.getApplicationContext();
    }

    /**
     * 获取应用的版本号
     *
     * @return 应用版本号，默认返回1
     */
    public static int getAppVersionCode() {
        Context context = getContext();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}

