package com.bishe.haitaoshopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bishe.haitaoshopping.chatkit.LCChatKit;
import com.bishe.haitaoshopping.chatkit.activity.LCIMConversationListFragment;
import com.bishe.haitaoshopping.home.CreateShopActivity;
import com.bishe.haitaoshopping.home.HomeFragment;
import com.bishe.haitaoshopping.personal.LoginActivity;
import com.bishe.haitaoshopping.personal.PersonalFragment;
import com.bishe.haitaoshopping.promotion.PromotionFragment;

import java.util.ArrayList;
import java.util.List;


import static com.bishe.haitaoshopping.Constant.DEFAULT_SELECT_TAB_INDEX;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View placeHolderStatusBar;
    private TextView btnAdd;

    private int[] tabTitleRes = {R.string.tab_home, R.string.tab_promotions, R.string.tab_message, R.string.tab_personal};
    private List<Fragment> mFragments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setStatusBar(this);
        login();
    }

    /**
     * 如果用户已登录, 连接即时通讯服务区
     */
    private void login() {
        if (Utils.checkLoginState()) {
            LCChatKit.getInstance().open(Utils.getUserName(), new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {}
            });
        }
    }

    private void init() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        placeHolderStatusBar = findViewById(R.id.place_holder_statusBar);
        btnAdd = findViewById(R.id.btn_add);
        initPlaceHolder();

        initFragments();

        initTabLayout();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.checkLoginState()) {
                    Intent intent = new Intent(MainActivity.this, CreateShopActivity.class);
                    startActivityForResult(intent, Constant.REQUEST_CODE_CREATE_SHOP);
                } else {
                    Utils.showToast(MainActivity.this, "请先登录~");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragments.get(0).onActivityResult(requestCode, resultCode, data);
    }

    private void initTabLayout() {
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(DEFAULT_SELECT_TAB_INDEX).select();//设置首页默认选中
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.tab_bottom_item_view, null);
            tabLayout.getTabAt(i).setCustomView(view);
            TextView tv = view.findViewById(R.id.tv_bottom_name);
            tv.setText(tabTitleRes[i]);
            if (i == DEFAULT_SELECT_TAB_INDEX) {
                Utils.setBottomTabSelectedStyle(this, tv);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tv = view.findViewById(R.id.tv_bottom_name);
                tv.setTextColor(getResources().getColor(R.color.tab_text_selected));
                Utils.doTabStateChangeAnimation(15, 17, tv);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tv = view.findViewById(R.id.tv_bottom_name);
                Utils.setBottomTabNormalStyle(MainActivity.this, tv);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new PromotionFragment());
        mFragments.add(new LCIMConversationListFragment());
        mFragments.add(new PersonalFragment());
    }

    private void initPlaceHolder() {
        placeHolderStatusBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) placeHolderStatusBar.getLayoutParams();
        lp.height = Utils.getStatusBarHeight(this);
        placeHolderStatusBar.setLayoutParams(lp);
    }


    private void setStatusBar(Activity activity) {
        int version = android.os.Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.KITKAT && version < Build.VERSION_CODES.M) {
            //Android4.4到5.0处理方法:设置应用全屏,这样会导致应用与statusbar重叠,所以需要再设置占位view
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            placeHolderStatusBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //android6.0以上,可以设置statusbar的字体颜色
            placeHolderStatusBar.setBackgroundColor(getResources().getColor(R.color.white));
            Utils.setMStatusStyle(this);
        }
    }


}
