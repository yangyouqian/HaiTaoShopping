package com.bishe.haitaoshopping.promotion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bishe.haitaoshopping.component.banner.ViewCreator;

import java.util.List;

/**
 * Created by yhviews on 2019/4/11.
 */

public class ListPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List mData;
    private ViewCreator mViewCreator;
    private SparseArray<View> views = new SparseArray<>();

    public ListPagerAdapter(Context mContext, List mData, ViewCreator mViewCreator) {
        this.mContext = mContext;
        this.mData = mData;
        this.mViewCreator = mViewCreator;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.d("adapter", "destroyItem " + position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d("adapter", "instantiateItem " + position);
        View view = views.get(position);
        if (view == null) {
            view = mViewCreator.createView(mContext, position);
            views.put(position, view);
        }
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        mViewCreator.updateUI(mContext, view, position, mData.get(position));
        container.addView(view);
        return view;
    }
}
