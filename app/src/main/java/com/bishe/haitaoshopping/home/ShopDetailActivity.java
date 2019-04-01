package com.bishe.haitaoshopping.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avos.avoscloud.AVObject;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;

public class ShopDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        Utils.setMStatusStyle(this);
    }
}
