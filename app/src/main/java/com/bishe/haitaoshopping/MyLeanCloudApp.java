package com.bishe.haitaoshopping;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.bishe.haitaoshopping.chatkit.LCChatKit;
import com.bishe.haitaoshopping.model.Shop;
import com.bishe.haitaoshopping.model.ShopPrice;


/**
 * Created by yhviews on 2019/3/1.
 */

public class MyLeanCloudApp extends Application {

    private final String APP_ID = "KY3ey67wou6y9lq4qMja1BOg-gzGzoHsz";
    private final String APP_KEY = "ncWRgQRlYdRLDrXg1VKBgFAL";

    @Override
    public void onCreate() {
        super.onCreate();

        AVObject.registerSubclass(Shop.class);
        AVObject.registerSubclass(ShopPrice.class);
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        AVOSCloud.setDebugLogEnabled(true);
    }
}
