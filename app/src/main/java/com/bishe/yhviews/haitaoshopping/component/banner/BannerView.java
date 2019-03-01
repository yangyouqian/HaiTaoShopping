package com.bishe.yhviews.haitaoshopping.component.banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bishe.yhviews.haitaoshopping.R;

import java.util.List;


/**
 * Created by yhviews on 2019/2/27.
 */

public class BannerView<T> extends FrameLayout {

    private Context mContext;
    private ViewPager mBannerViewPager;
    private LinearLayout mIndicatorLayout;

    private BannerPagerAdapter mAdapter;
    private Drawable mIndicatorSelectDrawable;
    private Drawable mIndicatorUnSelectDrawable;

    private OnPageClickListener mOnPageClickListener;
    private int mBannerCount;
    private long mIntervalTime;

    private Handler mTimeHandler = new Handler();
    private Runnable mTurningTask = new Runnable() {
        @Override
        public void run() {
            if (mBannerViewPager != null) {
                int nextPage = mBannerViewPager.getCurrentItem() + 1;
                mBannerViewPager.setCurrentItem(nextPage);
                mTimeHandler.postDelayed(mTurningTask, mIntervalTime);
            }
        }
    };

    public BannerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setPages(ViewCreator<T> creator, List<T> data) {
        mAdapter = new BannerPagerAdapter<T>(mContext, data, creator);
        if (mOnPageClickListener != null) {
            mAdapter.setOnPageClickListener(mOnPageClickListener);
        }
        mBannerViewPager.setAdapter(mAdapter);
        if (data != null) {
            mBannerCount = data.size();
            initIndicator(mBannerCount);
        }
        setCurrentItem(0);
        updateIndicator();
    }

    public void startTurning(long intervalTime) {
        this.mIntervalTime = intervalTime;
        mTimeHandler.postDelayed(mTurningTask, intervalTime);
    }

    public void setCurrentItem(int position) {
        if (position >= 0 && position < mBannerCount) {
            mBannerViewPager.setCurrentItem(position + 1);
        }
    }

    public void setIndicatorRes(int selectRes, int unSelectRes) {
        mIndicatorSelectDrawable = mContext.getResources().getDrawable(selectRes);
        mIndicatorUnSelectDrawable = mContext.getResources().getDrawable(unSelectRes);
        updateIndicator();
    }

    public void setOnPageClickListener(OnPageClickListener mOnPageClickListener) {
        this.mOnPageClickListener = mOnPageClickListener;
    }

    public int getCurrentItem() {
        return getActualPosition(mBannerViewPager.getCurrentItem());
    }

    public int getCount() {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return 0;
        }
        return mAdapter.getCount() - 2;
    }

    private void init(Context context) {
        mContext = context;
        mIndicatorSelectDrawable = mContext.getResources().getDrawable(R.drawable.banner_shape_point_select);
        mIndicatorUnSelectDrawable = mContext.getResources().getDrawable(R.drawable.banner_shape_point_unselect);
        addBannerViewPager();
        addIndicatorLayout();
    }

    private void addIndicatorLayout() {
        mIndicatorLayout = new LinearLayout(mContext);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mIndicatorLayout.setGravity(Gravity.CENTER);
        int margin = (int) mContext.getResources().getDimension(R.dimen.home_banner_indicator_margin);
        lp.setMargins(margin, 0, margin, margin);
        this.addView(mIndicatorLayout, lp);
    }

    private void addBannerViewPager() {
        mBannerViewPager = new ViewPager(mContext);
        mBannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                updateIndicator();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //这里的position的范围是 0~adapter.getCount-1
                int position = mBannerViewPager.getCurrentItem();
                //SCROLL_STATE_SETTLING:动画正执行,切换到这个item,
                // 在这个切换到这个状态前,instantiateItem方法会执行,说明这个方法是会预加载下一个要切换的item
                //整体流程就是 instantiateItem()->SCROLL_STATE_SETTLING->SCROLL_STATE_IDLE

                if (state == ViewPager.SCROLL_STATE_IDLE) {//切换已经完成,已滑动到下一个item
                    if (position == 0) {
                        mBannerViewPager.setCurrentItem(mAdapter.getCount() - 1, true);
                    } else if (position == mAdapter.getCount() - 1) {
                        mBannerViewPager.setCurrentItem(1, true);
                    }
                }
            }
        });

        this.addView(mBannerViewPager);
    }

    private void initIndicator(int bannerCount) {
        mIndicatorLayout.removeAllViews();
        if (bannerCount > 0) {
            for (int i = 0; i < bannerCount; i++) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(5, 0, 5, 0);
                mIndicatorLayout.addView(imageView, lp);
            }
        }
    }

    private void updateIndicator() {
        int count = mIndicatorLayout.getChildCount();
        int currentPageItem = getCurrentItem();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                ImageView imageView = (ImageView) mIndicatorLayout.getChildAt(i);
                if (i == currentPageItem) {
                    imageView.setImageDrawable(mIndicatorSelectDrawable);
                } else {
                    imageView.setImageDrawable(mIndicatorUnSelectDrawable);
                }
            }
        }
    }

    private int getActualPosition(int position) {
        if (position == 0) {
            return getCount() - 1;//滑动到0,应该跳转到实际最后一个item
        } else if (position == getCount() + 1) {
            return 0;//滑动到最后一个,应该跳转到实际第一个item
        }
        return position - 1;
    }

    public interface OnPageClickListener<T> {
        void onPageClick(int position, T t);
    }

    public interface ViewCreator<T> {
        View createView(Context context, int position);

        void updateUI(Context context, View view, int position, T t);
    }
}
