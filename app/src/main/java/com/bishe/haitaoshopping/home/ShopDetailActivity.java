package com.bishe.haitaoshopping.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetDataCallback;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.banner.BannerView;
import com.bishe.haitaoshopping.component.titlebar.TitleBar;
import com.bishe.haitaoshopping.model.Shop;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopDetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvSubTitle;
    public TextView tvCreateUserName;
    public TextView tvCreateTime;
    private ImageView ivAvatar;
    private LinearLayout tagContainer;
    private TextView tvLikeNum;
    private TitleBar titleBar;
    private BannerView bannerView;
    private Shop mShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        Utils.setMStatusStyle(this);
        Intent intent = getIntent();
        if (intent != null) {
            mShop = intent.getParcelableExtra("shop");
        }
        initView();
        initData();
    }

    private void initView() {
        tvTitle = findViewById(R.id.shop_detail_title);
        tvSubTitle = findViewById(R.id.shop_detail_subtitle);
        tvCreateTime = findViewById(R.id.shop_detail_create_time);
        tvCreateUserName = findViewById(R.id.shop_detail_user_name);
        ivAvatar = findViewById(R.id.shop_detail_user_avatar);
        tagContainer = findViewById(R.id.shop_detail_tags);
        tvLikeNum = findViewById(R.id.shop_detail_like_num);
        titleBar = findViewById(R.id.shop_detail_title_bar);
        bannerView = findViewById(R.id.shop_detail_banner_view);
    }

    private void initData() {
        if (mShop == null) {
            return;
        }
        tvTitle.setText(mShop.getTitle());
        tvSubTitle.setText(mShop.getSubTitle());
        tvCreateUserName.setText(mShop.getUserName());
        tvCreateTime.setText(mShop.getCreateTime());
        tvLikeNum.setText(mShop.getLikeNum());
        titleBar.setTitle("拼单信息详情");
        titleBar.setOnBackClickListener(new TitleBar.OnBackClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        tagContainer.addView(buildTextView("#" + mShop.getBrand()));
        tagContainer.addView(buildTextView("#" + mShop.getType()));
        tagContainer.addView(buildTextView("#" + mShop.getWebSite()));
        setBean(mShop.getImageUrlList());
    }

    private TextView buildTextView(String text) {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.tv_shop_detail_tag_bg);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dip2pxInt(this, 23));
        layoutParams.setMargins(0, 0, 20, 0);
        textView.setPadding(Utils.dip2pxInt(this, 5), 0, Utils.dip2pxInt(this, 5), 0);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(this.getResources().getColor(R.color.color_d43030));
        textView.setTag(text);
        return textView;
    }

    private void setBean(final List<String> beans) {
        bannerView.setPages(new BannerView.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
//                imageView.setScaleType(ImageView.ScaleType.CENTER);
                return imageView;
            }

            @Override
            public void updateUI(Context context, final View view, int position, String entity) {
                AVFile file = new AVFile("img.jpg", entity, new HashMap<String, Object>());
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (e == null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ((ImageView)view).setImageBitmap(bitmap);
                        }
                    }
                });
            }
        }, beans);
    }
}
