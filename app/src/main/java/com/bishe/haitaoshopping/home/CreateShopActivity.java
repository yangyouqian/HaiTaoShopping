package com.bishe.haitaoshopping.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.model.Shop;

public class CreateShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        Utils.setMStatusStyle(this);
        init();
    }

    private void init() {

    }

    public void add(View view) {
        for (int i = 0; i < Constant.shopTypes.length; i++) {
            AVObject object = new AVObject("ShopType");
            object.put("name", Constant.shopTypes[i]);
            object.put("pinyin", Constant.shopTypesPinyin[i]);
            object.saveInBackground();
        }
//        AVObject brand = new AVObject("ShopBrand");
//        brand.put("name", "魅可(Mac)");
//        brand.saveInBackground();
//
//        AVObject brand1 = new AVObject("ShopBrand");
//        brand1.put("name", "Colourpop");
//        brand1.saveInBackground();
//
//        AVObject brand2 = new AVObject("ShopBrand");
//        brand2.put("name", "Too faced");
//        brand2.saveInBackground();
//        Shop shop = new Shop();
//        shop.setBrand("Mac");
//        shop.setType("口红");
//        shop.setWebsite("官网");
//        shop.setExpress(null);
//        shop.setDiscount("8折");
//        shop.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if (e == null) {
//                    Utils.showToast(CreateShopActivity.this, "发起拼单成功");
//                } else {
//                    Utils.showToast(CreateShopActivity.this, e.getMessage());
//                }
//            }
//        });
    }
}
