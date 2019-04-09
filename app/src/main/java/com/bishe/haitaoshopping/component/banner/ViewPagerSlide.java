package com.bishe.haitaoshopping.component.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yhviews on 2019/4/9.
 */


public class ViewPagerSlide extends ViewPager {
    //是否可以进行滑动
    private boolean isSlide = false;

    public void setSlide(boolean slide) {
        isSlide = slide;
    }

    public ViewPagerSlide(Context context) {
        super(context);
    }

    public ViewPagerSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isSlide){
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //super.onTouchEvent(ev); //不行,
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
        if (isSlide){
            return super.onTouchEvent(ev);
        }else {
            return true;// 可行,消费,拦截事件
        }
    }
}

