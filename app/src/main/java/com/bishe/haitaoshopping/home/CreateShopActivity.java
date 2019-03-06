package com.bishe.haitaoshopping.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.titlebar.TitleBar;

public class CreateShopActivity extends AppCompatActivity {

    private TitleBar titleBar;
    private EditText etBrand;
    private EditText etWebSite;
    private EditText etType;
    private EditText etExpress;
    private EditText etDiscount;
    private EditText etTitle;
    private EditText etSubtitle;

    private Button btnBrand;
    private Button btnType;
    private Button btnWebsite;

    private TextView btnConfirmCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        Utils.setMStatusStyle(this);
        init();
        View decorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));

    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    private void init() {
        titleBar = findViewById(R.id.create_shop_title_bar);
        etBrand = findViewById(R.id.et_brand);
        etWebSite = findViewById(R.id.et_website);
        etType = findViewById(R.id.et_type);
        etExpress = findViewById(R.id.et_express);
        etDiscount = findViewById(R.id.et_discount);
        etTitle = findViewById(R.id.et_title);
        etSubtitle = findViewById(R.id.et_sub_title);
        btnBrand = findViewById(R.id.btn_brand);
        btnType = findViewById(R.id.btn_type);
        btnWebsite = findViewById(R.id.btn_website);
        btnConfirmCreate = findViewById(R.id.btn_confirm_create);
        titleBar.setTitle("发起拼单");
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
