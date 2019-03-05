package com.bishe.haitaoshopping;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.bishe.haitaoshopping.model.Shop;

/**
 * Created by yhviews on 2019/3/1.
 */

public class MyLeanCloudApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AVObject.registerSubclass(Shop.class);
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"KY3ey67wou6y9lq4qMja1BOg-gzGzoHsz","ncWRgQRlYdRLDrXg1VKBgFAL");
        AVOSCloud.setDebugLogEnabled(true);
    }
}
