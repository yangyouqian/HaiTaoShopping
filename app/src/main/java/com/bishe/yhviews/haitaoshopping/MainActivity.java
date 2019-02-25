package com.bishe.yhviews.haitaoshopping;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.bishe.yhviews.haitaoshopping.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabTitleRes = {R.string.tab_home, R.string.tab_promotions, R.string.tab_message, R.string.tab_personal};
    private List<Fragment> mFragments;

    private ValueAnimator valueAnimator;

    private static final int DEFAULT_SELECT_TAB_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAndroidNativeLightStatusBar(this, true);
        init();
    }

    private void init() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        initFragments();

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        initTabLayout();
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
                setBottomTabSelectedStyle(tv);
                doTabStateChangeAnimation(1, 1.1f, tv);
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

    private void doTabStateChangeAnimation(float scale1, float scale2, final TextView tv) {
        valueAnimator = ValueAnimator.ofFloat(scale1, scale2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) valueAnimator.getAnimatedValue();
                tv.setScaleX(scale);
                tv.setScaleY(scale);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(350);
        valueAnimator.start();
    }

    private void setBottomTabSelectedStyle(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.tab_text_selected));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    private void setBottomTabNormalStyle(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.tab_text_normal));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
    }


    private void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

}
