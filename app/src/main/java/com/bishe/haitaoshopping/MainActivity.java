package com.bishe.haitaoshopping;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bishe.haitaoshopping.home.CreateShopActivity;
import com.bishe.haitaoshopping.home.HomeFragment;
import com.bishe.haitaoshopping.message.MessageFragment;
import com.bishe.haitaoshopping.personal.PersonalFragment;
import com.bishe.haitaoshopping.promotion.PromotionFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View placeHolderStatusBar;
    private TextView btnAdd;

    private int[] tabTitleRes = {R.string.tab_home, R.string.tab_promotions, R.string.tab_message, R.string.tab_personal};
    private List<Fragment> mFragments;

    private ValueAnimator valueAnimator;

    private static final int DEFAULT_SELECT_TAB_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setStatusBar(this);
    }

    private void init() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        placeHolderStatusBar = findViewById(R.id.place_holder_statusBar);
        btnAdd = findViewById(R.id.btn_add);
        initPlaceHolder();

        initFragments();

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        initTabLayout();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateShopActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initTabLayout() {
        tabLayout.getTabAt(DEFAULT_SELECT_TAB_INDEX).select();//设置首页默认选中
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.tab_bottom_item_view, null);
            tabLayout.getTabAt(i).setCustomView(view);
            TextView tv = view.findViewById(R.id.tv_bottom_name);
            tv.setText(tabTitleRes[i]);
            if (i == DEFAULT_SELECT_TAB_INDEX) {
                setBottomTabSelectedStyle(tv);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tv = view.findViewById(R.id.tv_bottom_name);
//                setBottomTabSelectedStyle(tv);
                tv.setTextColor(getResources().getColor(R.color.tab_text_selected));
                doTabStateChangeAnimation(15, 17, tv);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tv = view.findViewById(R.id.tv_bottom_name);
                setBottomTabNormalStyle(tv);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new PromotionFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new PersonalFragment());
    }

    private void initPlaceHolder() {
        placeHolderStatusBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) placeHolderStatusBar.getLayoutParams();
        lp.height = Utils.getStatusBarHeight(this);
        placeHolderStatusBar.setLayoutParams(lp);
    }

    private void doTabStateChangeAnimation(float scale1, float scale2, final TextView tv) {
        valueAnimator = ValueAnimator.ofFloat(scale1, scale2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) valueAnimator.getAnimatedValue();
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, scale);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(180);
        valueAnimator.start();
    }

    private void setBottomTabSelectedStyle(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.tab_text_selected));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
    }

    private void setBottomTabNormalStyle(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.tab_text_normal));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
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
